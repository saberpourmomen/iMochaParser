package com.imocha.parser.mapper;

import com.imocha.parser.dto.CallRecordDto;
import com.imocha.parser.model.CallRecord;

public class CallRecordMapper {

    public static CallRecordDto toDto(CallRecord model){
        return CallRecordDto.builder()
                .id(model.getId())
                .accountNumber(model.getAccountNumber())
                .callerNumber(model.getCallerNumber())
                .calleeNumber(model.getCalleeNumber())
                .startTime(model.getStartTime())
                .endTime(model.getEndTime())
                .callType(model.getCallType())
                .cost(model.getCost())
        .build();
    }

    public static CallRecord toModel(CallRecordDto dto){
        return CallRecord.builder()
                .id(dto.getId())
                .accountNumber(dto.getAccountNumber())
                .callerNumber(dto.getCallerNumber())
                .calleeNumber(dto.getCalleeNumber())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .callType(dto.getCallType())
                .cost(dto.getCost())
                .build();
    }

}
