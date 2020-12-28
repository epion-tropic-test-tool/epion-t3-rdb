/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.bean.TargetTable;
import com.epion_t3.rdb.command.model.ExportRdbData;
import com.epion_t3.rdb.command.model.StoreRdbQueryResult;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.message.RdbMessages;
import com.epion_t3.rdb.type.DataSetType;
import com.epion_t3.rdb.util.RdbAccessUtils;
import com.epion_t3.rdb.writer.XlsxDataSetWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.dataset.xml.XmlDataSetWriter;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * RDBに対してクエリーを実行して値を保持するコマンド実行処理.
 *
 * @since 0.0.3
 * @author takashno
 */
@Slf4j
public class StoreRdbQueryResultRunner extends AbstractCommandRunner<StoreRdbQueryResult> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(StoreRdbQueryResult command, Logger logger) throws Exception {

        // クエリー文字列を取得
        String query = command.getValue();

        // クエリーは必須
        if (StringUtils.isEmpty(query)) {
            throw new SystemException(RdbMessages.RDB_ERR_0001);
        }

        // 複数行の場合は、セミコロンで区切られている
        String[] queries = query.split(";");

        if (queries.length > 1) {
            // TODO: エラー
            log.error("too many queries...");
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
            var queryResultList = new ArrayList<>();
            var rowCount = iTable.getRowCount();
            var metaData = iTable.getTableMetaData();
            for (int i = 0; i < rowCount; i++) {
                var queryResultRecordMap = new LinkedHashMap<String, Object>();
                for (Column col : metaData.getColumns()) {
                    var value = iTable.getValue(i, col.getColumnName());
                    log.debug("column : {}, value : {}", col.getColumnName(), value == null ? "null" : value);
                    queryResultRecordMap.put(col.getColumnName(), value);
                }
                queryResultList.add(queryResultRecordMap);
            }
            setVariable(command.getTarget(), queryResultList);
        } catch (DatabaseUnitException e) {
            log.debug("Error Occurred...", e);
            throw new SystemException(e, RdbMessages.RDB_ERR_0011);
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
