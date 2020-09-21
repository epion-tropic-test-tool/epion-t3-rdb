/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.bean;

import com.epion_t3.core.common.type.AssertStatus;
import lombok.Getter;
import lombok.Setter;
import org.dbunit.dataset.datatype.DataType;

import java.io.Serializable;

/**
 * アサート結果カラム値.
 *
 * @author takashno
 */
@Getter
@Setter
public class AssertResultColumnDataType implements Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 期待値.
     */
    private DataType expected;

    /**
     * 結果値.
     */
    private DataType actual;

    /**
     * アサート結果.
     */
    private AssertStatus status;

}
