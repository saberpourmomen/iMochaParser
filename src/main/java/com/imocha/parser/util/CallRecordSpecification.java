package com.imocha.parser.util;

import com.imocha.parser.dto.CallType;
import com.imocha.parser.model.CallRecord;
import org.springframework.data.jpa.domain.Specification;

public class CallRecordSpecification {

    private CallRecordSpecification() {}

    public static Specification<CallRecord> build(
            String account,
            CallType type) {

        Specification<CallRecord> spec = Specification.allOf();

        if (account != null) {
            spec = spec.and(accountEquals(account));
        }

        if (type != null) {
            spec = spec.and(typeEquals(type));
        }

        return spec;
    }

    private static Specification<CallRecord> accountEquals(String account) {
        return (root, query, cb) ->
                cb.equal(root.get("accountNumber"), account);
    }

    private static Specification<CallRecord> typeEquals(CallType type) {
        return (root, query, cb) ->
                cb.equal(root.get("callType"), type);
    }

}
