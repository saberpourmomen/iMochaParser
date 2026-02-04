package com.imocha.parser.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyCostDto(LocalDate date, BigDecimal totalCost) {
}
