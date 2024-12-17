package com.tekarch.fund_transferMS.services;

import com.tekarch.fund_transferMS.models.FundTransfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferService {

    FundTransfer initiateTransfer(Integer senderAccountId, Integer receiverAccountId, BigDecimal amount);
    public void completeTransfer(Long transferId);
    FundTransfer getTransferDetails(Long transferId);

}
