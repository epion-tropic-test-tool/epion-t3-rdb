/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.command.model;

import com.epion_t3.core.common.annotation.CommandDefinition;
import com.epion_t3.core.common.bean.scenario.Command;
import com.epion_t3.rdb.command.runner.ImportRdbDataRunner;
import lombok.Getter;
import lombok.Setter;
import org.apache.bval.constraints.NotEmpty;

/**
 * @author takashno
 */
@Getter
@Setter
@CommandDefinition(id = "ImportRdbData", runner = ImportRdbDataRunner.class)
public class ImportRdbData extends Command {

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
     * オペレーション.
     */
    private String operation = "clean_insert";

    /**
     * バインドを行うか.
     */
    private boolean bind = false;
}
