package com.eros.demo.openai.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "helpdesk_ticket")
public class HelpDeskTicket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String issue;
    private String description;
    private String status;

    private LocalDateTime createdDate;
    //When this ticket is spected to be resolved.
    //“Estimated Time of Arrival”
    private LocalDateTime eta;


}
