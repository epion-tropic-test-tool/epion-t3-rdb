/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.command.runner.StoreRdbQueryResultRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

/**
 * RDBに対してクエリーを実行して値を保持するコマンド処理.
 *
 * @since 0.0.3
 * @author takashno
 */
@Getter
@Setter
@CommandDefinition(id = "StoreRdbQueryResult", runner = StoreRdbQueryResultRunner.class)
public class StoreRdbQueryResult extends Command {

    /**
     * RDB接続設定参照.
     */
    @NotEmpty
    private String rdbConnectConfigRef;

}
