package com.epion_t3.rdb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RdbType {

    MYSQL("mysql"),

    POSTGRESQL("postgresql"),

    ORACLE("oracle");

    private String value;

    /**
     * 値から列挙子を取得.
     *
     * @param value
     * @return
     */
    public static RdbType valueOfByValue(final String value) {
        return Arrays.stream(values()).filter(x -> x.value.equals(value)).findFirst().orElse(null);
    }
}
