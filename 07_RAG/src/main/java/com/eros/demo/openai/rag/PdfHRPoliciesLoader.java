package com.eros.demo.openai.rag;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PdfHRPoliciesLoader {

    private final VectorStore vectorStore;

    @Value( "classpath:Eazybytes_HR_Policies.pdf")
    Resource policyFile;

    public PdfHRPoliciesLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadPdfPolicies() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
        List<Document> documentList = tikaDocumentReader.get();
        List<Document> split = TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build().split(documentList);
        this.vectorStore.add(split);
    }

}
