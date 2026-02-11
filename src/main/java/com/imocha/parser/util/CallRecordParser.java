package com.imocha.parser.util;

import com.imocha.parser.dto.CallType;
import com.imocha.parser.exception.ParseException;
import com.imocha.parser.model.CallRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CallRecordParser {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static CallRecord parseLine(String line) {
        List<String> parts =getParts(line);

        return CallRecord.builder()
                .accountNumber(parts.get(0))
                .callerNumber(parts.get(1))
                .calleeNumber(parts.get(2))
                .startTime(parseDate(parts.get(3),"startTime"))
                .endTime(parseDate(parts.get(4),"endTime"))
                .callType(parseCallType(parts.get(5)))
                .cost(parseCost(parts.get(6)))
                .build();
    }
    private static List<String> getParts(String line){
        List<String> result=new ArrayList<>();
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }
        result.add(parts[0].trim());
        result.add(parts[1].trim());
        result.add(parts[2].trim());
        result.add(parts[3].substring(0, 14).trim());
        result.add(parts[3].substring(14, 28).trim());
        result.add(parts[3].substring(28, 29).trim());
        result.add(parts[3].substring(29).trim());
        return result;
    }

    private static LocalDateTime parseDate(String value, String field) {
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid " + field + ": " + value);
        }
    }

    private static CallType parseCallType(String value) {
        try {
            return CallType.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid call type: " + value);
        }
    }

    private static BigDecimal parseCost(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid cost: " + value);
        }
    }

}
