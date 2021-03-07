/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.command.runner.ExecuteRdbQueryRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

/**
 * RDBに対するクエリーを実行するコマンド処理.
 *
 * @author takashno
 */
@Getter
@Setter
@CommandDefinition(id = "ExecuteRdbQuery", runner = ExecuteRdbQueryRunner.class)
public class ExecuteRdbQuery extends Command {

    /**
     * RDB接続設定参照.
     */
    @NotEmpty
    private String rdbConnectConfigRef;

}
