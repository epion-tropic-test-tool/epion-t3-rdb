/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;

/**
 * RDB種別.
 */
@Getter
@AllArgsConstructor
public enum RdbType {

    /**
     * MySQL.
     */
    MYSQL("mysql"),

    /**
     * PostgreSQL.
     */
    POSTGRESQL("postgresql"),

    /**
     * Oracle.
     */
    ORACLE("oracle"),

    /**
     * Snowflake.
     * 
     * @since 0.0.4
     */
    SNOWFLAKE("snowflake");

    /**
     * 値.
     */
    private String value;

    /**
     * 値から列挙子を取得.
     *
     * @param value 値
     * @return {@link RdbType}
     */
    public static RdbType valueOfByValue(@NonNull final String value) {
        return Arrays.stream(values()).filter(x -> x.value.equals(value.toLowerCase())).findFirst().orElse(null);
    }
}
