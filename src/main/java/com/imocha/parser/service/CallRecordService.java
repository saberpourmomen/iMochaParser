package com.imocha.parser.service;

import com.imocha.parser.dto.CallRecordDto;
import com.imocha.parser.dto.CallType;
import com.imocha.parser.mapper.CallRecordMapper;
import com.imocha.parser.model.CallRecord;
import com.imocha.parser.repository.CallRecordRepository;
import com.imocha.parser.util.CallRecordSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CallRecordService {

    private final CallRecordRepository callRecordRepository;

    public Page<CallRecordDto> search(String account, CallType type, Pageable pageable) {

            Specification<CallRecord> spec =
                    CallRecordSpecification.build(account, type);

            return callRecordRepository.findAll(spec, pageable)
                    .map(CallRecordMapper::toDto);
    }
}
