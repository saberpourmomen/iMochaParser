package com.imocha.parser.dto;

import java.math.BigDecimal;

public record AccountCostDto(String accountNumber, BigDecimal totalCost) {
}
