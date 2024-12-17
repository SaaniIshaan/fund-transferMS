package com.tekarch.fund_transferMS.models;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fund_transfer")
public class FundTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long transferId;

    @ManyToOne(fetch = FetchType.LAZY) // Relationship with Account for sender
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Integer senderAccountId;

    @ManyToOne(fetch = FetchType.LAZY) // Relationship with Account for receiver
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private Integer receiverAccountId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 20, nullable = false)
    private String status = "pending";

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" )
    private LocalDateTime initiated_at;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime completed_at;

 //   @ManyToOne
 //   @JoinColumn(name = "account_id")
 //   private Account account;
}
