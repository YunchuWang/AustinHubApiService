package com.austinhub.apiservice.repository;

import com.austinhub.apiservice.model.po.Transaction;
import com.braintreegateway.Transaction.Status;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "select * from Transaction tran where status in :statusList", nativeQuery = true)
    List<Transaction> findByStatus(@Param("statusList") Set<Status> statusList);

    @Modifying
    @Query(value =
            "update Transaction tran set tran.status = :status where tran.id = "
                    + ":tranId")
    void updateStatus(@Param("tranId") Integer tranId, @Param("status") Status status);
}
