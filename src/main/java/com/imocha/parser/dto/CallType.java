package com.imocha.parser.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CallType {
    P("P","phone call"),
    S ("S","SMS"),
    M ("M","multimedia message");
    final String type;
    final String description;
}
