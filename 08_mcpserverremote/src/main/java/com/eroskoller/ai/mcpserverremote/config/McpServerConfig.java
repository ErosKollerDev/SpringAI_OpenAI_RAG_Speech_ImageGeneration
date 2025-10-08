package com.eroskoller.ai.mcpserverremote.config;



import com.eroskoller.ai.mcpserverremote.tool.HelpDeskTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class McpServerConfig {

    @Bean
    List<ToolCallback> toolCallbacks(HelpDeskTools helpDeskTools){
        return List.of(ToolCallbacks.from(helpDeskTools));
    }


}
