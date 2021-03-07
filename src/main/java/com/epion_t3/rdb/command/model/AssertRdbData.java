/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.bean.AssertTargetTable;
import com.epion_t3.rdb.command.reporter.AssertRdbDataReporter;
import com.epion_t3.rdb.command.runner.AssertRdbDataRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

import java.util.List;

@Getter
@Setter
@CommandDefinition(id = "AssertRdbData", runner = AssertRdbDataRunner.class, assertCommand = true, reporter = AssertRdbDataReporter.class)
public class AssertRdbData extends Command {

    /**
     * 期待値データセットのパス.
     */
    @NotEmpty
    private String expectedDataSetPath;

    /**
     * 期待値データセット種別.
     */
    @NotEmpty
    private String expectedDataSetType = "excel";

    /**
     * 結果値データセットを取得したFlowID.
     */
    @NotEmpty
    private String actualFlowId;

    /**
     * 結果値データセット種別.
     */
    @NotEmpty
    private String actualDataSetType = "excel";

    /**
     * テーブル指定.
     */
    private List<AssertTargetTable> tables;

}
