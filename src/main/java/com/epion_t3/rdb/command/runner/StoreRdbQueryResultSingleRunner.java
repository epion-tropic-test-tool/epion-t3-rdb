/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.command.model.StoreRdbQueryResult;
import com.epion_t3.rdb.command.model.StoreRdbQueryResultSingle;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.util.RdbAccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * RDBに対してクエリーを実行して値を保持するコマンド実行処理. 1レコード1カラムのみの取得という制限があるコマンド.
 *
 * @author takashno
 * @since 0.0.3
 */
@Slf4j
public class StoreRdbQueryResultSingleRunner extends AbstractCommandRunner<StoreRdbQueryResultSingle> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(StoreRdbQueryResultSingle command, Logger logger) throws Exception {

        // クエリー文字列を取得
        String query = command.getValue();

        // クエリーは必須
        if (StringUtils.isEmpty(query)) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0001);
        }

        // 複数行の場合は、セミコロンで区切られている
        String[] queries = query.split(";");

        if (queries.length > 1) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0020);
        }

        // 接続先設定を参照
        RdbConnectionConfiguration rdbConnectionConfiguration = referConfiguration(command.getRdbConnectConfigRef());

        // データセット読み込み
        QueryDataSet iDataSet = null;

        IDatabaseConnection conn = null;
        try {
            // コネクションを取得
            conn = RdbAccessUtils.getInstance().getDatabaseConnection(rdbConnectionConfiguration);

            // クエリーデータセットを作成
            iDataSet = new QueryDataSet(conn);
            iDataSet.addTable("QUERY", queries[0]);
            var iTable = iDataSet.getTable("QUERY");
            var metaData = iTable.getTableMetaData();

            if (metaData.getColumns().length > 1) {
                throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0021);
            }

            if (iTable.getRowCount() > 1) {
                throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0022);
            }

            var col = metaData.getColumns()[0];
            var value = iTable.getValue(0, col.getColumnName());
            log.debug("column : {}, value : {}", col.getColumnName(), value == null ? "null" : value);
            setVariable(command.getTarget(), value);

        } catch (DatabaseUnitException e) {
            log.debug("Error Occurred...", e);
            throw new SystemException(e, RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0011);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Ignore
                    log.trace("Error Occurred... -> Ignore", e);
                }
            }
        }

        return CommandResult.getSuccess();

    }

}
