/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.command.model.ImportRdbData;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.type.DataSetType;
import com.epion_t3.rdb.type.OperationType;
import com.epion_t3.rdb.util.DataSetUtils;
import com.epion_t3.rdb.util.RdbAccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

/**
 * RDBに対してデータセットをインポート実行処理.
 *
 * @author takashno
 */
@Slf4j
public class ImportRdbDataRunner extends AbstractCommandRunner<ImportRdbData> {

    @Override
    public CommandResult execute(ImportRdbData command, Logger logger) throws Exception {

        // 接続先設定を参照
        RdbConnectionConfiguration rdbConnectionConfiguration = referConfiguration(command.getRdbConnectConfigRef());

        // DataSetの配置パスを取得
        String dataSet = command.getValue();

        // DataSetの配置パスは必須
        if (StringUtils.isEmpty(dataSet)) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0005);
        }

        // DataSetの配置パスを解決
        Path dataSetPath = Paths.get(getCommandBelongScenarioDirectory(), dataSet);

        // DataSetの配置パスが存在しなかった場合はエラー
        if (Files.notExists(dataSetPath)) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0006, dataSetPath.toString());
        }

        // データセット種別
        DataSetType dataSetType = DataSetType.valueOfByValue(command.getDataSetType());

        // データセット種別が解決できなかった場合はエラー
        if (dataSetType == null) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0007, command.getDataSetType());
        }

        // データセット読み込み
        IDataSet iDataSet = DataSetUtils.getInstance().readDataSet(dataSetPath, dataSetType);

        // バインド
        if (command.isBind()) {
            iDataSet = new ReplacementDataSet(iDataSet);

            // プロファイル定数
            for (Map.Entry<String, String> entry : getProfileConstants().entrySet()) {
                ((ReplacementDataSet) iDataSet).addReplacementObject(String.format("${%s}", entry.getKey()),
                        entry.getValue());
            }

            // グローバル変数
            for (Map.Entry<String, Object> entry : getGlobalScopeVariables().entrySet()) {
                ((ReplacementDataSet) iDataSet).addReplacementObject(
                        String.format("${%s.%s}", "global", entry.getKey()), entry.getValue().toString());
            }

            // シナリオ変数
            for (Map.Entry<String, Object> entry : getScenarioScopeVariables().entrySet()) {
                ((ReplacementDataSet) iDataSet).addReplacementObject(
                        String.format("${%s.%s}", "com/epion_t3/core/common/bean/scenario", entry.getKey()),
                        entry.getValue().toString());
            }

            // Flow変数
            for (Map.Entry<String, Object> entry : getFlowScopeVariables().entrySet()) {
                ((ReplacementDataSet) iDataSet).addReplacementObject(String.format("${%s.%s}", "flow", entry.getKey()),
                        entry.getValue().toString());
            }

        }

        // オペレーションを特定
        OperationType operationType = OperationType.valueOfByValue(command.getOperation().toLowerCase());

        switch (operationType) {
        case INSERT:
        case CLEAN_INSERT:
        case UPDATE:
        case REFRESH:
            IDatabaseConnection conn = null;
            try {
                // コネクションを取得
                conn = RdbAccessUtils.getInstance().getDatabaseConnection(rdbConnectionConfiguration);

                // オペレーション実行
                operationType.getOperation().execute(conn, iDataSet);

            } catch (SQLException | DatabaseUnitException e) {
                log.debug("Error Occurred...", e);
                throw new SystemException(e, RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0010);
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
            break;
        default:
            // データインポートとは関係のないオペレーションのためエラー
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0009, operationType.getValue());
        }

        return CommandResult.getSuccess();

    }

}
