/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.bean;

import com.epion_t3.core.common.type.AssertStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * アサート結果行.
 *
 * @author takashno
 */
@Getter
@Setter
public class AssertResultRow implements Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 行アサート結果.
     */
    private AssertStatus rowAssert = AssertStatus.WAIT;

    /**
     * アサート結果カラム値リスト.
     */
    private List<AssertResultColumnValue> columns = new ArrayList<>();

    /**
     * OKカラム数.
     */
    private Integer okColumnCount = 0;

    public void addOkColumnCount() {
        okColumnCount++;
    }

    /**
     * NGカラム数.
     */
    private Integer ngColumnCount = 0;

    public void addNgColumnCount() {
        ngColumnCount++;
    }

}
