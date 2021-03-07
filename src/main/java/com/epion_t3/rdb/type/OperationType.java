/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dbunit.operation.DatabaseOperation;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OperationType {

    INSERT("insert", DatabaseOperation.INSERT),

    CLEAN_INSERT("clean_insert", DatabaseOperation.CLEAN_INSERT),

    DELETE("delete", DatabaseOperation.DELETE),

    DELETE_ALL("delete_all", DatabaseOperation.DELETE_ALL),

    UPDATE("update", DatabaseOperation.UPDATE),

    TRUNCATE_TABLE("truncate_table", DatabaseOperation.TRUNCATE_TABLE),

    REFRESH("refresh", DatabaseOperation.REFRESH),

    NONE("none", DatabaseOperation.NONE);

    /**
     * 値.
     */
    private String value;

    /**
     * オペレーション.
     */
    private DatabaseOperation operation;

    /**
     * 値から列挙子を取得.
     *
     * @param value
     * @return
     */
    public static OperationType valueOfByValue(final String value) {
        return Arrays.stream(values()).filter(x -> x.value.equals(value)).findFirst().orElse(null);
    }
}
