package com.imocha.parser.service;

import com.imocha.parser.dto.AccountCostDto;
import com.imocha.parser.dto.CallType;
import com.imocha.parser.dto.CallTypeCostDto;
import com.imocha.parser.dto.DailyCostDto;
import com.imocha.parser.repository.CallRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CallRecordRepository callRecordRepository;

    public List<AccountCostDto> byAccount() {
        return callRecordRepository.totalCostPerAccount();
    }

    public List<CallTypeCostDto> byCallType() {
        return callRecordRepository.totalCostPerCallType();
    }

    public
    List<DailyCostDto> byDay() {
        return callRecordRepository.totalCostPerDay().stream()
                .map(row -> new DailyCostDto(
                        ((java.sql.Date) row[0]).toLocalDate(),
                        (BigDecimal) row[1]
                ))
                .toList();
    }

}
