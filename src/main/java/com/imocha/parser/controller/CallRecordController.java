package com.imocha.parser.controller;

import com.imocha.parser.dto.CallRecordDto;
import com.imocha.parser.dto.CallType;
import com.imocha.parser.service.CallRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/call-record")
public class CallRecordController {

    private final CallRecordService callRecordService;

    @GetMapping("/search")
    public Page<CallRecordDto> search(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) CallType type,
            Pageable pageable) {

        return callRecordService.search(account, type, pageable);
    }

}
