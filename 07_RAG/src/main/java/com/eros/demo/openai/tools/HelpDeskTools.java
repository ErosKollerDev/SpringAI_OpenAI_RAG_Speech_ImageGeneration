package com.eros.demo.openai.tools;


import com.eros.demo.openai.entity.HelpDeskTicket;
import com.eros.demo.openai.model.TicketRequest;
import com.eros.demo.openai.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelpDeskTools {


    private final HelpDeskService helpDeskService;

    @Tool(name = "createTicket", description = "Create a Support ticket", returnDirect = true)
    String createTicket(@ToolParam(description = "Details to create a Support ticket ") TicketRequest ticketRequest, ToolContext toolContext) {

        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket helpDeskServiceTicket = this.helpDeskService.createTicket(ticketRequest, username);
        log.info("Ticket created successfully for user: {} with id: {}",username, helpDeskServiceTicket.getId());
        return "Ticket created successfully with id: %s".formatted(helpDeskServiceTicket.getId());
    }

    @Tool(name = "getTicketsByUsername", description = "Get Support tickets by username")
    List<HelpDeskTicket> getTicketsByUsername(ToolContext toolContext) {
        log.info("Getting tickets for user: {}", toolContext.getContext().get("username"));
        String username = (String) toolContext.getContext().get("username");
        return this.helpDeskService.getTicketsByUsername(username);
    }


}
