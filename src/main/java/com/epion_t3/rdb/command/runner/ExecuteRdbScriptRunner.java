/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.command.model.ExecuteRdbScript;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.util.RdbAccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * RDBに対してスクリプト（SQL）実行処理．
 *
 * @author takashno
 */
@Slf4j
public class ExecuteRdbScriptRunner extends AbstractCommandRunner<ExecuteRdbScript> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(ExecuteRdbScript command, Logger logger) throws Exception {

        // 接続先は必須
        if (StringUtils.isEmpty(command.getRdbConnectConfigRef())) {
            throw new SystemException(RdbMessages.RDB_ERR_0002);
        }

        // 接続先設定を参照
        RdbConnectionConfiguration rdbConnectionConfiguration = referConfiguration(command.getRdbConnectConfigRef());

        // スクリプトパスを取得
        var script = command.getValue();

        // クエリーは必須
        if (StringUtils.isEmpty(script)) {
            throw new SystemException(RdbMessages.RDB_ERR_0003);
        }

        // スクリプトパスを解決
        var scriptPath = Paths.get(getCommandBelongScenarioDirectory(), script);

        // スクリプトパスが存在しなかった場合はエラー
        if (Files.notExists(scriptPath)) {
            throw new SystemException(RdbMessages.RDB_ERR_0004, scriptPath.toString());
        }

        // スクリプトの内容を読み込み
        var scriptContents = Files.readString(scriptPath);

        // スクリプトを読み込んだ結果空出会った場合はエラー（想定外の挙動である可能性が高いため）
        if (StringUtils.isEmpty(scriptContents)) {
            throw new SystemException(RdbMessages.RDB_ERR_0023, scriptPath.toString());
        }

        // スクリプトの内容に対してバインド処理
        // クエリーの場合は直接YAMLに記載されるためバインドは前段で行われているが、
        // スクリプト（SQL）の場合はファイルの中身を読み込まなければならないため
        scriptContents = bind(scriptContents);

        // スクリプトを分割
        var queries = scriptContents.split("(?m);$");

        // データソースを取得
        var dataSource = RdbAccessUtils.getInstance().getDataSource(rdbConnectionConfiguration);

        try (var conn = dataSource.getConnection()) {
            for (var q : queries) {
                if (StringUtils.isNotEmpty(q) && StringUtils.isNotEmpty(q.trim())) {
                    q = bind(q);
                    try (var statement = conn.prepareStatement(q)) {
                        log.trace(collectLoggingMarker(), "execute query -> {}", q);
                        statement.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException(e, RdbMessages.RDB_ERR_0002);
        }

        return CommandResult.getSuccess();
    }

}
