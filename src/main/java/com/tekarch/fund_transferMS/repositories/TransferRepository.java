package com.tekarch.fund_transferMS.repositories;

import com.tekarch.fund_transferMS.models.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<FundTransfer, Long> {
    FundTransfer findByTransferId(Long transferId);
}
