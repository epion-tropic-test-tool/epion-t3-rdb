/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.command.runner.StoreRdbQueryResultSingleRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

/**
 * RDBに対してクエリーを実行して値を保持するコマンド処理. 1レコード1カラムのみの取得という制限があるコマンド.
 *
 * @since 0.0.3
 * @author takashno
 */
@Getter
@Setter
@CommandDefinition(id = "StoreRdbQueryResultSingle", runner = StoreRdbQueryResultSingleRunner.class)
public class StoreRdbQueryResultSingle extends Command {

    /**
     * RDB接続設定参照.
     */
    @NotEmpty
    private String rdbConnectConfigRef;

}
