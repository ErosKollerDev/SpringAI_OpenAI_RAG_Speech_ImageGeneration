package com.eros.demo.openai.model;

import lombok.Data;

@Data
public class TicketRequest {

    private String username;
    private String issue;
    private String description;
    private String status;

}
