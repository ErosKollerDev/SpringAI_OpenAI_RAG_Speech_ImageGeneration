package com.eros.demo.openai.service;


import com.eros.demo.openai.entity.HelpDeskTicket;
import com.eros.demo.openai.model.TicketRequest;
import com.eros.demo.openai.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class HelpDeskService {


    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketRequest, String username) {

        HelpDeskTicket helpDeskTicket = HelpDeskTicket.builder()
                .issue(ticketRequest.getIssue())
                .description(ticketRequest.getDescription())
                .username(username)
                .status("OPEN")
                .createdDate(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();
        return helpDeskTicketRepository.save(helpDeskTicket);
    }

    public List<HelpDeskTicket> getTicketsByUsername(String username) {
        return helpDeskTicketRepository.findByUsername(username);
    }

}
