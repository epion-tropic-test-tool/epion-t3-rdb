/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.bean;

import com.epion_t3.core.command.bean.AssertCommandResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * アサートRDBData結果.
 *
 * @author takashno
 */
@Getter
@Setter
public class AssertRdbDataResult extends AssertCommandResult {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * アサート結果テーブル.
     */
    private List<AssertResultTable> tables = new ArrayList<>();

}
