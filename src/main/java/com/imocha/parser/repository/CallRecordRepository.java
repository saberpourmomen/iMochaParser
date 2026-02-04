package com.imocha.parser.repository;

import com.imocha.parser.dto.AccountCostDto;
import com.imocha.parser.dto.CallTypeCostDto;
import com.imocha.parser.dto.DailyCostDto;
import com.imocha.parser.model.CallRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRecordRepository extends JpaRepository<CallRecord, String>, JpaSpecificationExecutor<CallRecord> {

    @Query("""
                SELECT new com.imocha.parser.dto.AccountCostDto(c.accountNumber, SUM(c.cost))
                FROM CallRecord c
                GROUP BY c.accountNumber
            """)
    List<AccountCostDto> totalCostPerAccount();

    @Query("""
                SELECT new com.imocha.parser.dto.CallTypeCostDto(
                    c.callType,
                    SUM(c.cost)
                )
                FROM CallRecord c
                GROUP BY c.callType
            """)
    List<CallTypeCostDto> totalCostPerCallType();

    @Query(value = """
                SELECT c.start_time::date AS date, SUM(c.cost) AS total_cost
                FROM call_record c
                GROUP BY c.start_time::date
            """, nativeQuery = true)
    List<Object[]> totalCostPerDay();

}
