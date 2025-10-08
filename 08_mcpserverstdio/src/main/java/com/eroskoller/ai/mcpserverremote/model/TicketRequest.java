package com.eroskoller.ai.mcpserverremote.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketRequest {
    private String username;
    private String issue;
}
