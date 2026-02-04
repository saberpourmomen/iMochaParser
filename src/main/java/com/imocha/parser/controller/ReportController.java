package com.imocha.parser.controller;

import com.imocha.parser.dto.*;
import com.imocha.parser.service.CallRecordService;
import com.imocha.parser.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/account")
    public ResponseEntity<List<AccountCostDto>> byAccount() {
        return ResponseEntity.ok(reportService.byAccount());
    }

    @GetMapping("/caller-type")
    public ResponseEntity
            <List<CallTypeCostDto>> byCallType() {
        return ResponseEntity.ok(reportService.byCallType());
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyCostDto>> byDay() {
        return ResponseEntity.ok(reportService.byDay());
    }

}
