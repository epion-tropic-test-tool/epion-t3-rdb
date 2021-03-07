/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.bean.TargetTable;
import com.epion_t3.rdb.command.runner.ExportRdbDataRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

import java.util.List;

/**
 * RDBデータのエクスポートコマンド.
 *
 * @author takashno
 */
@Getter
@Setter
@CommandDefinition(id = "ExportRdbData", runner = ExportRdbDataRunner.class)
public class ExportRdbData extends Command {

    /**
     * RDB接続設定参照.
     */
    @NotEmpty
    private String rdbConnectConfigRef;

    /**
     * データセット種別.
     */
    @NotEmpty
    private String dataSetType = "excel";

    /**
     * テーブル指定.
     */
    private List<TargetTable> tables;

}
