package com.tekarch.fund_transferMS.controllers;

import com.tekarch.fund_transferMS.models.FundTransfer;
import com.tekarch.fund_transferMS.services.TransferServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fund-transfers")
public class TransferController {

    private static final Logger logger = LogManager.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferServiceImpl transferServiceImpl;

    public TransferController(TransferServiceImpl transferServiceImpl){
        this.transferServiceImpl = transferServiceImpl;
    }

    @PostMapping("/initiate")
    public ResponseEntity<FundTransfer> initiateTransfer(
            @RequestParam Integer senderAccountId,
            @RequestParam Integer receiverAccountId,
            @RequestParam BigDecimal amount) {

        FundTransfer fundTransfer = transferServiceImpl.initiateTransfer(senderAccountId, receiverAccountId, amount);
        return ResponseEntity.ok(fundTransfer);

    }

    @PostMapping("/{transferId}/complete")
    public ResponseEntity<Void> completeTransfer(@PathVariable Long transferId) {
        transferServiceImpl.completeTransfer(transferId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<FundTransfer> getTransferDetails(@PathVariable Long transferId) {
        FundTransfer transfer = transferServiceImpl.getTransferDetails(transferId);
        return ResponseEntity.ok(transfer);
    }

        @ExceptionHandler
        public ResponseEntity<String> respondWithError (Exception e){
            logger.error("Exception Occurred. Details : {} ", e.getMessage());
            return new ResponseEntity<>("Exception Occurred. More Info : " +
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
