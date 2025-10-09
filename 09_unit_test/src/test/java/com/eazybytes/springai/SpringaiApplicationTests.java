package com.eazybytes.springai;

import com.eazybytes.springai.controller.ChatController;
import org.junit.jupiter.api.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "spring.ai.openai.api-key=${OPENAI_API_KEY:test-key}",
        "logging.level.org.springframework.ai=DEBUG"
})
@SpringBootTest
class SpringaiApplicationTests {

    @Autowired
    private ChatController chatController;
    @Autowired
    private ChatModel chatModel;

    private ChatClient chatClient;
    private RelevancyEvaluator relevancyEvaluator;

    private FactCheckingEvaluator factCheckingEvaluator;

    @Value("${test.relevancy.min-score:0.7}")
    private float minRelevancyScore = 0.5f;

    @Value("classpath:/promptTemplates/hrPolicy.st")
    private Resource hrPolicyTemplate;


    @BeforeEach
    void setup() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor());
        this.chatClient = chatClientBuilder.build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.factCheckingEvaluator = new FactCheckingEvaluator(chatClientBuilder);
    }

    @Test
    @DisplayName("Should return basic relevant response for geography question")
    @Timeout(30)
    void evaluateChatControllerResponseRelevancy() {
        //Given this
        String question = "Where is the capital of France?";
        //When that
        String aiAnswer = this.chatController.chat(question);
        //Do this
        EvaluationResponse evaluate = this.relevancyEvaluator.evaluate(new EvaluationRequest(question, aiAnswer));
        boolean pass = evaluate.isPass();
        //Test that
        Assertions.assertTrue(pass);
        //Assert that
        Assertions.assertAll(
                () -> assertThat(aiAnswer).isNotBlank(),
                () -> assertThat(aiAnswer).contains("Paris"),
                () -> assertThat(aiAnswer).contains("France"),
                () -> assertThat(evaluate.getScore()).isGreaterThanOrEqualTo(minRelevancyScore),
                () -> assertThat(evaluate.isPass())
                        .withFailMessage("\n================\n" +
                                "Evaluation failed: %s\nQuestion: %s\nResponse: %s\n" +
                                "================", evaluate, question, aiAnswer).isTrue());
    }


    @Test
    @DisplayName("Should return fact check response for question")
    @Timeout(30)
    void evaluateFactAccuracyForGravityQuestions() {
        //Given this
        String question = "Who discovery the law of universal gravitation?";
        //When that
        String aiAnswer = this.chatController.chat(question);
        //Do this
        EvaluationResponse evaluate = this.factCheckingEvaluator.evaluate(new EvaluationRequest(question, aiAnswer));
        //Assert that
        Assertions.assertAll(
                () -> assertThat(aiAnswer).isNotBlank(),
                () -> assertThat(evaluate.isPass())
                        .withFailMessage("\n================\n" +
                                "FactCheck failed: %s\nQuestion: %s\nResponse: %s\n" +
                                "================", evaluate, question, aiAnswer).isTrue()
        );
    }


    @Test
    @DisplayName("Should return evaluation using HR Policy Resource and (Rag) Context")
    @Timeout(130)
    void evaluateHrPolicyAnswerWithRagContext() throws IOException {


        String hrPolicyStuffing = this.hrPolicyTemplate.getContentAsString(StandardCharsets.UTF_8);
        //Given this
        String question = "How many paid leaves do employees get annually?";
        //When that
        String aiAnswer =  this.chatController.promptStuffing(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                question,
                List.of(new Document(hrPolicyStuffing)),
                aiAnswer
        );

        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        // Then
        Assertions.assertAll(
                () -> assertThat(aiAnswer).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage("""
                        ========================================
                        The response was not considered factually accurate.
                        Question: %s
                        Response: %s
                        Context: %s
                        ========================================
                        """, question, aiAnswer, hrPolicyStuffing)
                        .isTrue());
    }


}
