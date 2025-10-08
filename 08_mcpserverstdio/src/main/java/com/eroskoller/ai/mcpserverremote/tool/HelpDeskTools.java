package com.eroskoller.ai.mcpserverremote.tool;


import com.eroskoller.ai.mcpserverremote.entity.HelpDeskTicket;
import com.eroskoller.ai.mcpserverremote.model.TicketRequest;
import com.eroskoller.ai.mcpserverremote.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HelpDeskTools {


    private final HelpDeskTicketService helpDeskService;

    @Tool(name = "createTicket", description = "Create a Support ticket")
    String createTicket(@ToolParam(description = "Details to create a Support ticket")
                        TicketRequest ticketRequest) {

//        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket helpDeskServiceTicket = this.helpDeskService.createTicket(ticketRequest, ticketRequest.getUsername());
        log.info("Ticket created successfully for user: {} with id: {}",ticketRequest.getUsername(), helpDeskServiceTicket.getId());
        return "Ticket created successfully with id: %s".formatted(helpDeskServiceTicket.getId());
    }



    @Tool(name = "getTicketStatus", description = "Fetch the status of tickets based on a given status")
    List<HelpDeskTicket> getTicketStatus(@ToolParam(description =
            "Username to fetch the status of the help desk tickets") String username) {
        log.info("Getting tickets for user: {}", username);
        return this.helpDeskService.getTicketsByUsername(username);
    }


}
