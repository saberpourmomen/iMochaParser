package com.imocha.parser.dto;

import java.math.BigDecimal;

public record CallTypeCostDto(CallType callType, BigDecimal totalCost) {
}
