/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * アサート対象テーブル.
 *
 * @author takashno
 */
@Getter
@Setter
public class AssertTargetTable implements Serializable {

    /**
     * デフォルトシリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * テーブル名.
     */
    private String table;

    /**
     * 無視カラムリスト.
     */
    private List<String> ignoreColumns = new ArrayList<>();
}
