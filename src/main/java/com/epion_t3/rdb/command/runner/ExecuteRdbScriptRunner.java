/* Copyright (c) 2017-2020 Nozomu Takashima. */
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

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0002);
        }

        // 接続先設定を参照
        RdbConnectionConfiguration rdbConnectionConfiguration = referConfiguration(command.getRdbConnectConfigRef());

        // スクリプトパスを取得
        String script = command.getValue();

        // クエリーは必須
        if (StringUtils.isEmpty(script)) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0003);
        }

        // スクリプトパスを解決
        Path scriptPath = Paths.get(getCommandBelongScenarioDirectory(), script);

        // スクリプトパスが存在しなかった場合はエラー
        if (Files.notExists(scriptPath)) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0004, scriptPath.toString());
        }

        // スクリプトの内容を読み込み
        String scriptContents = new String(Files.readAllBytes(scriptPath), Charset.forName("UTF-8"));

        // スクリプトの内容に対してバインド処理
        // クエリーの場合は直接YAMLに記載されるためバインドは前段で行われているが、
        // スクリプト（SQL）の場合はファイルの中身を読み込まなければならないため
        scriptContents = bind(scriptContents);

        // スクリプトを分割
        String[] queries = scriptContents.split(";");

        // データソースを取得
        DataSource dataSource = RdbAccessUtils.getInstance().getDataSource(rdbConnectionConfiguration);

        try (Connection conn = dataSource.getConnection()) {
            for (String q : queries) {
                if (StringUtils.isNotEmpty(q)) {
                    try (PreparedStatement statement = conn.prepareStatement(q)) {
                        log.trace("execute query -> {}", q);
                        statement.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0002, e);
        }

        return CommandResult.getSuccess();
    }

}
