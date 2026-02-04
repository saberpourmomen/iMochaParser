package com.imocha.parser.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CallRecordDto {

    private String id;

    private String accountNumber;

    private String callerNumber;

    private String calleeNumber;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private CallType callType;

    private BigDecimal cost;
}
