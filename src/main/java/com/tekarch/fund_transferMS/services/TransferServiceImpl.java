package com.tekarch.fund_transferMS.services;

//import com.tekarch.fund_transferMS.models.Account;
import com.tekarch.fund_transferMS.models.FundTransfer;
//import com.tekarch.fund_transferMS.repositories.AccountRepository;
import com.tekarch.fund_transferMS.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
//@AllArgsConstructor
public class TransferServiceImpl implements TransferService {

    private static final Logger logger = LogManager.getLogger(TransferServiceImpl.class);
    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private final RestTemplate restTemplate;
//    private final String USER_MANAGEMENT_MS_URL = "http://localhost:8081/users";
//    private final String ACCOUNT_MS_URL = "http://localhost:8082/accounts";


    public TransferServiceImpl(TransferRepository transferRepository, RestTemplate restTemplate) {
        this.transferRepository = transferRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public FundTransfer initiateTransfer(Integer senderAccountId, Integer receiverAccountId, BigDecimal amount) {

        // Validate sender and receiver accounts using Account Microservice
        String accountServiceUrl = "http://localhost:8082/accounts/";
        Boolean senderValid = restTemplate.getForObject(accountServiceUrl + senderAccountId + "/exists", Boolean.class);
        Boolean receiverValid = restTemplate.getForObject(accountServiceUrl + receiverAccountId + "/exists", Boolean.class);

        if (!Boolean.TRUE.equals(senderValid) || !Boolean.TRUE.equals(receiverValid)) {
            throw new IllegalArgumentException("Invalid sender or receiver account");
        }
        // Create and save the transfer record
        FundTransfer fundTransfer = new FundTransfer();
        fundTransfer.setSenderAccountId(senderAccountId);
        fundTransfer.setReceiverAccountId(receiverAccountId);
        fundTransfer.setAmount(amount);
        fundTransfer.setStatus("pending");
        return transferRepository.save(fundTransfer);
    }

    @Override
    public void completeTransfer(Long transferId) {
        Optional<FundTransfer> transferOpt = transferRepository.findById(transferId);
        if (transferOpt.isEmpty()) {
            throw new IllegalArgumentException("Transfer not found");
        }

        FundTransfer fundTransfer = transferOpt.get();

        // Call Account Microservice to transfer the amount
        String accountServiceUrl = "http://localhost:8082/accounts";
        restTemplate.postForObject(accountServiceUrl, fundTransfer, Void.class);

        fundTransfer.setStatus("completed");
        fundTransfer.setCompleted_at(LocalDateTime.now());
        transferRepository.save(fundTransfer);
    }

    @Override
    public FundTransfer getTransferDetails(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
    }



}
