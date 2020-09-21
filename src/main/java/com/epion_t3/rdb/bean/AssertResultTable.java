/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.bean;

import com.epion_t3.core.common.type.AssertStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * テーブルのアサーション結果.
 *
 * @author takashno
 */
@Getter
@Setter
public class AssertResultTable implements Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * スキーマ.
     */
    private String schema;

    /**
     * 物理名
     */
    private String name;

    /**
     * レコード数アサート.
     */
    private AssertStatus recordNumAssert = AssertStatus.WAIT;

    /**
     * カラム数アサート結果.
     */
    private AssertStatus columnNumAssert = AssertStatus.WAIT;

    /**
     * カラムインデックスアサート結果.
     */
    private AssertStatus columnIndexAssert = AssertStatus.WAIT;

    /**
     * カラム型アサート結果.
     */
    private AssertStatus columnDataTypeAssert = AssertStatus.WAIT;

    /**
     * 行アサート結果.
     */
    private AssertStatus rowAssert = AssertStatus.WAIT;

    /**
     * 型.
     */
    private List<AssertResultColumnDataType> types = new ArrayList<>();

    /**
     * 行.
     */
    private List<AssertResultRow> rows = new ArrayList<>();

    /**
     * OK行数.
     */
    private Integer okRowCount = 0;

    public void addOkRowCount() {
        okRowCount++;
    }

    /**
     * NG行数.
     */
    private Integer ngRowCount = 0;

    public void addNgRowCount() {
        ngRowCount++;
    }

}
