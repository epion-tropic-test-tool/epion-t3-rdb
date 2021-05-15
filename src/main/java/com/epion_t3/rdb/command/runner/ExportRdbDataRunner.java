/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.common.util.JsonUtils;
import com.epion_t3.core.common.util.YamlUtils;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.bean.TargetTable;
import com.epion_t3.rdb.command.model.ExportRdbData;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.type.DataSetType;
import com.epion_t3.rdb.util.RdbAccessUtils;
import com.epion_t3.rdb.writer.XlsxDataSetWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.dataset.xml.XmlDataSetWriter;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * RDBに対してデータセットをインポート実行処理.
 *
 * @author takashno
 */
@Slf4j
public class ExportRdbDataRunner extends AbstractCommandRunner<ExportRdbData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(ExportRdbData command, Logger logger) throws Exception {

        // 接続先設定を参照
        var rdbConnectionConfiguration = referConfiguration(command.getRdbConnectConfigRef());

        // データセット種別
        var dataSetType = DataSetType.valueOfByValue(command.getDataSetType());

        // データセット種別が解決できなかった場合はエラー
        if (dataSetType == null) {
            throw new SystemException(RdbMessages.RDB_ERR_0007, command.getDataSetType());
        }

        var tables = (List<TargetTable>) null;
        if (command.getTables() != null) {
            tables = command.getTables();
        } else if (StringUtils.isNotEmpty(command.getTablesConfigPath())) {
            var tableConfigPath = Paths.get(getScenarioDirectory(), command.getTablesConfigPath());
            if (!Files.exists(tableConfigPath)) {
                throw new SystemException(RdbMessages.RDB_ERR_0025, tableConfigPath.toString());
            }
            if (StringUtils.endsWith(command.getTablesConfigPath(), "yaml")
                    || StringUtils.endsWith(command.getTablesConfigPath(), "yml")) {
                tables = YamlUtils.getInstance().unmarshal(tableConfigPath);
            } else if (StringUtils.endsWith(command.getTablesConfigPath(), "json")) {
                tables = JsonUtils.getInstance().unmarshal(tableConfigPath);
            } else {
                throw new SystemException(RdbMessages.RDB_ERR_0029, command.getTablesConfigPath());
            }
        } else {
            throw new SystemException(RdbMessages.RDB_ERR_0024, command.getTablesConfigPath());
        }

        // データセット読み込み
        var iDataSet = (IDataSet) null;

        var conn = (IDatabaseConnection) null;
        try {
            // コネクションを取得
            conn = RdbAccessUtils.getInstance()
                    .getDatabaseConnection((RdbConnectionConfiguration) rdbConnectionConfiguration);

            // クエリーデータセットを作成
            iDataSet = new QueryDataSet(conn);

            // 対象テーブルを登録
            for (Object t : tables) {
                if (TargetTable.class.isAssignableFrom(t.getClass())) {
                    var castedTargetTable = (TargetTable) t;
                    ((QueryDataSet) iDataSet).addTable(castedTargetTable.getTable(), castedTargetTable.getQuery());
                } else if (LinkedHashMap.class.isAssignableFrom(t.getClass())) {
                    var castedMap = (LinkedHashMap<String, Object>) t;
                    ((QueryDataSet) iDataSet).addTable((String) castedMap.get("table"),
                            (String) castedMap.get("query"));
                }
            }

            var encoding = StringUtils.isEmpty(command.getEncoding()) ? System.getProperty("file.encoding")
                    : command.getEncoding();
            // 文字コードチェック
            if (!Charset.isSupported(encoding)) {
                throw new SystemException(RdbMessages.RDB_ERR_0031, encoding);
            }

            // データセットの種類によって出力処理を行う
            switch (dataSetType) {
            case CSV:
                // TODO
                // iDataSet = new CsvDataSet(dataSetPath.toFile());
                // break;
                throw new SystemException(RdbMessages.RDB_ERR_0008);
            case XML:
                Path xmlPath = getEvidencePath("export.xml");
                try (var os = new FileOutputStream(xmlPath.toFile());) {
                    var writer = new XmlDataSetWriter(os, encoding);
                    writer.write(iDataSet);
                }
                registrationFileEvidence(xmlPath);
                break;
            case FLAT_XML:
                Path flatXmlPath = getEvidencePath("export_flat.xml");
                try (var os = new FileOutputStream(flatXmlPath.toFile());) {
                    var writer = new FlatXmlWriter(os, encoding);
                    writer.write(iDataSet);
                }
                registrationFileEvidence(flatXmlPath);
                break;
            case EXCEL:
                Path xlsxPath = getEvidencePath("export.xlsx");
                try (var os = new FileOutputStream(xlsxPath.toFile())) {
                    var writer = new XlsxDataSetWriter();
                    writer.write(iDataSet, os);
                }
                registrationFileEvidence(xlsxPath);
                break;
            default:
                throw new SystemException(RdbMessages.RDB_ERR_0026, command.getDataSetType());
            }
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
