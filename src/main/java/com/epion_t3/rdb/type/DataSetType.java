/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * DataSet種別.
 *
 * @author takashno
 */
@Getter
@AllArgsConstructor
public enum DataSetType {

    CSV("csv"),

    XML("xml"),

    FLAT_XML("flatXml"),

    EXCEL("excel");

    /**
     * 値.
     */
    private String value;

    /**
     * 値から列挙子を取得.
     *
     * @param value
     * @return
     */
    public static DataSetType valueOfByValue(final String value) {
        return Arrays.stream(values()).filter(x -> x.value.equals(value)).findFirst().orElse(null);
    }

}
