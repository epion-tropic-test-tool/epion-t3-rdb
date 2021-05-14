/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.common.type.AssertStatus;
import com.epion_t3.core.common.util.JsonUtils;
import com.epion_t3.core.common.util.YamlUtils;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.bean.AssertRdbDataResult;
import com.epion_t3.rdb.bean.AssertResultColumnDataType;
import com.epion_t3.rdb.bean.AssertResultColumnValue;
import com.epion_t3.rdb.bean.AssertResultRow;
import com.epion_t3.rdb.bean.AssertResultTable;
import com.epion_t3.rdb.bean.AssertTargetTable;
import com.epion_t3.rdb.command.model.AssertRdbData;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.type.DataSetType;
import com.epion_t3.rdb.util.DataSetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.UnknownDataType;
import org.slf4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * RDBに対してデータセットをインポート実行処理.
 *
 * @author takashno
 */
@Slf4j
public class AssertRdbDataRunner extends AbstractCommandRunner<AssertRdbData> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(AssertRdbData command, Logger logger) throws Exception {

        var result = new AssertRdbDataResult();
        result.setAssertStatus(AssertStatus.OK);

        // 期待値DataSet
        var expected = getExpectedDataSet(command, logger);

        // 結果値DataSet
        var actual = getActualDataSet(command, logger);

        var tables = (List<AssertTargetTable>) null;
        if (command.getTables() != null) {
            tables = command.getTables();
        } else if (StringUtils.isNotEmpty(command.getTablesConfigPath())) {
            var tableConfigPath = Paths.get(getScenarioDirectory(), command.getTablesConfigPath());
            if (!Files.exists(tableConfigPath)) {
                throw new SystemException(RdbMessages.RDB_ERR_0028, tableConfigPath.toString());
            }
            if (StringUtils.endsWith(command.getTablesConfigPath(), "yaml")
                    || StringUtils.endsWith(command.getTablesConfigPath(), "yml")) {
                tables = YamlUtils.getInstance().unmarshal(tableConfigPath);
            } else if (StringUtils.endsWith(command.getTablesConfigPath(), "json")) {
                tables = JsonUtils.getInstance().unmarshal(tableConfigPath);
            } else {
                throw new SystemException(RdbMessages.RDB_ERR_0030, command.getTablesConfigPath());
            }
        } else {
            throw new SystemException(RdbMessages.RDB_ERR_0027, command.getTablesConfigPath());
        }

        // アサート対象のテーブル情報をループ
        for (Object t : tables) {

            var assertTargetTable = (AssertTargetTable) null;

            if (AssertTargetTable.class.isAssignableFrom(t.getClass())) {
                assertTargetTable = (AssertTargetTable) t;
            } else if (LinkedHashMap.class.isAssignableFrom(t.getClass())) {
                var castedMap = (LinkedHashMap<String, Object>) t;
                assertTargetTable = new AssertTargetTable();
                assertTargetTable.setTable((String) castedMap.get("table"));
                assertTargetTable.setIgnoreColumns((List<String>) castedMap.get("ignoreColumns"));
            }

            if (assertTargetTable.getIgnoreColumns() == null) {
                assertTargetTable.setIgnoreColumns(new ArrayList<>());
            }

            // 結果オブジェクト生成
            var assertResultTable = new AssertResultTable();
            result.getTables().add(assertResultTable);
            assertResultTable.setName(assertTargetTable.getTable());

            logger.debug("RdbDataAssert -> Table: {}", assertTargetTable.getTable());

            // 期待値Table
            var expectedTable = expected.getTable(assertTargetTable.getTable());
            // 期待値TableMetaData
            var expectedMetaData = expectedTable.getTableMetaData();

            // 結果値Table
            var actualTable = actual.getTable(assertTargetTable.getTable());
            // 結果値TableMetaData
            var actualMetaData = actualTable.getTableMetaData();

            // ----------------------------------------
            // カラム数アサート
            // ----------------------------------------
            if (expectedMetaData.getColumns().length != actualMetaData.getColumns().length) {
                // カラム数不一致
                assertResultTable.setColumnNumAssert(AssertStatus.NG);
                result.setAssertStatus(AssertStatus.NG);
                log.debug("!NG! table:{}, expectedColumnNum:{}, actualColumnNum:{}", assertTargetTable.getTable(),
                        expectedMetaData.getColumns().length, actualMetaData.getColumns().length);
                continue;
            } else {
                assertResultTable.setColumnNumAssert(AssertStatus.OK);
            }

            // ----------------------------------------
            // 型アサート
            // ----------------------------------------
            var dataTypeMap = new HashMap<String, DataType>();
            for (Column expectedCol : expectedMetaData.getColumns()) {

                // 期待値カラムのインデックスを取得
                int expectedColIndex = -999;
                expectedColIndex = expectedMetaData.getColumnIndex(expectedCol.getColumnName());

                // 結果値カラムのインデックスを取得（期待値カラム名と一致する結果値カラムを取得）
                int actualColIndex = -111;
                actualColIndex = actualMetaData.getColumnIndex(expectedCol.getColumnName());

                if (expectedColIndex != actualColIndex) {
                    // カラム順序不一致
                    assertResultTable.setColumnIndexAssert(AssertStatus.NG);
                    result.setAssertStatus(AssertStatus.NG);
                    log.debug("!NG! table:{}, column:{}, expectedIndex:{}, actualIndex:{}",
                            assertTargetTable.getTable(), expectedCol.getColumnName(), expectedColIndex,
                            actualColIndex);
                    break;
                } else {
                    assertResultTable.setColumnIndexAssert(AssertStatus.OK);
                }

                AssertResultColumnDataType dataType = new AssertResultColumnDataType();
                assertResultTable.getTypes().add(dataType);

                // カラム順序が一致したため、結果値カラムを取得する
                Column actualCol = actualMetaData.getColumns()[actualColIndex];

                // 型を設定
                dataType.setExpected(expectedCol.getDataType());
                dataType.setActual(actualCol.getDataType());

                if (UnknownDataType.class.isAssignableFrom(expectedCol.getDataType().getClass())) {
                    // 期待値カラムの型が不明なため、結果値カラムの型を使用
                    dataTypeMap.put(expectedCol.getColumnName(), actualCol.getDataType());
                    dataType.setStatus(AssertStatus.OK);
                } else {
                    if (UnknownDataType.class.isAssignableFrom(actualCol.getDataType().getClass())) {
                        // 結果値カラムの型が不明なため、期待値カラムの型を使用
                        dataTypeMap.put(expectedCol.getColumnName(), expectedCol.getDataType());
                        dataType.setStatus(AssertStatus.OK);
                    } else {

                        // 型の確認を行う
                        if (!expectedCol.getDataType()
                                .getClass()
                                .isAssignableFrom(actualCol.getDataType().getClass())) {
                            // 型の不一致
                            dataType.setStatus(AssertStatus.NG);
                            result.setAssertStatus(AssertStatus.NG);
                            log.debug("!NG! table:{}, column:{}, expectedType:{}, actualType:{}",
                                    assertTargetTable.getTable(), expectedCol.getColumnName(),
                                    expectedCol.getDataType().getSqlTypeName(),
                                    actualCol.getDataType().getSqlTypeName());
                            continue;
                        }
                    }
                }
            }

            if (assertResultTable.getTypes().stream().anyMatch(x -> x.getStatus() == AssertStatus.NG)) {
                assertResultTable.setColumnDataTypeAssert(AssertStatus.NG);
            } else {
                assertResultTable.setColumnDataTypeAssert(AssertStatus.OK);
            }

            // この時点でアサーションが失敗していれば終了する
            if (result.getAssertStatus() == AssertStatus.NG) {
                return result;
            }

            // ----------------------------------------
            // 件数アサート
            // ----------------------------------------
            if (expectedTable.getRowCount() != actualTable.getRowCount()) {
                assertResultTable.setRecordNumAssert(AssertStatus.NG);
                result.setAssertStatus(AssertStatus.NG);
            } else {
                assertResultTable.setRecordNumAssert(AssertStatus.OK);
            }

            // ----------------------------------------
            // データアサート
            // ----------------------------------------
//            List<String> assertColumns = Arrays.stream(expectedMetaData.getColumns())
//                    .filter(x -> !assertTargetTable.getIgnoreColumns().contains(x.getColumnName()))
//                    .map(x -> x.getColumnName())
//                    .collect(Collectors.toList());

            for (int rowCount = 0; (rowCount < expectedTable.getRowCount()
                    && rowCount < actualTable.getRowCount()); rowCount++) {

                var assertResultRow = new AssertResultRow();
                assertResultTable.getRows().add(assertResultRow);

                for (var column : expectedMetaData.getColumns()) {

                    var assertColumn = new AssertResultColumnValue();
                    assertColumn.setName(column.getColumnName());
                    assertColumn.setIgnore(assertTargetTable.getIgnoreColumns().contains(column.getColumnName()));
                    assertResultRow.getColumns().add(assertColumn);

                    // 期待値
                    Object expectedValue = expectedTable.getValue(rowCount, column.getColumnName());
                    assertColumn.setExpected(expectedValue);

                    // 結果値
                    Object actualValue = actualTable.getValue(rowCount, column.getColumnName());
                    assertColumn.setActual(actualValue);

                    if (dataTypeMap.containsKey(column.getColumnName())) {
                        var dataType = dataTypeMap.get(column.getColumnName());

                        if (dataType.compare(expectedValue, actualValue) == 0) {
                            // OK
                            assertColumn.setStatus(AssertStatus.OK);
                            assertResultRow.addOkColumnCount();
                            log.debug("[OK] table:{}, column:{}, expected:{}, actual:{}", assertTargetTable.getTable(),
                                    column, expectedValue, actualValue);
                        } else {
                            // NG
                            assertColumn.setStatus(AssertStatus.NG);
                            if (!assertTargetTable.getIgnoreColumns().contains(column.getColumnName())) {
                                assertResultRow.addNgColumnCount();
                                result.setAssertStatus(AssertStatus.NG);
                            }
                            log.debug("!NG! table:{}, column:{}, expected:{}, actual:{}", assertTargetTable.getTable(),
                                    column, expectedValue, actualValue);
                        }

                    } else {
                        // カラムの型が解決できない場合はエラー
                        throw new SystemException(RdbMessages.RDB_ERR_0019, assertTargetTable.getTable(), column);
                    }
                }

                if (assertResultRow.getColumns()
                        .stream()
                        .anyMatch(x -> !x.isIgnore() && x.getStatus() == AssertStatus.NG)) {
                    assertResultTable.addNgRowCount();
                    assertResultRow.setRowAssert(AssertStatus.NG);
                } else {
                    assertResultTable.addOkRowCount();
                    assertResultRow.setRowAssert(AssertStatus.OK);
                }
            }

            // テーブルに対して1行でもアサートエラーがあればNGとみなす
            assertResultTable.setRowAssert(assertResultTable.getNgRowCount() > 0 ? AssertStatus.NG : AssertStatus.OK);

        }

        return result;

    }

    /**
     * 期待値DataSet読込.
     *
     * @param command コマンド
     * @param logger ロガー
     * @return 期待値DataSet
     */
    private IDataSet getExpectedDataSet(AssertRdbData command, Logger logger) {

        // DataSetの配置パスを取得
        var dataSet = command.getExpectedDataSetPath();

        // DataSetの配置パスは必須
        if (StringUtils.isEmpty(dataSet)) {
            throw new SystemException(RdbMessages.RDB_ERR_0005);
        }

        // DataSetの配置パスを解決
        var dataSetPath = Paths.get(getCommandBelongScenarioDirectory(), dataSet);

        // DataSetの配置パスが存在しなかった場合はエラー
        if (Files.notExists(dataSetPath)) {
            throw new SystemException(RdbMessages.RDB_ERR_0006, dataSetPath.toString());
        }

        // データセット種別
        var dataSetType = DataSetType.valueOfByValue(command.getExpectedDataSetType());

        // データセット種別が解決できなかった場合はエラー
        if (dataSetType == null) {
            throw new SystemException(RdbMessages.RDB_ERR_0007, command.getExpectedDataSetType());
        }

        // データセット読み込み
        return DataSetUtils.getInstance().readDataSet(dataSetPath, dataSetType);

    }

    /**
     * 結果値DataSet読込.
     *
     * @param command コマンド
     * @param logger ロガー
     * @return 結果値DataSet
     */
    private IDataSet getActualDataSet(AssertRdbData command, Logger logger) {

        // 結果値参照のFlowIDを取得
        String flowId = command.getActualFlowId();

        // DataSetの配置パスは必須
        if (StringUtils.isEmpty(flowId)) {
            throw new SystemException(RdbMessages.RDB_ERR_0018);
        }

        // 結果値DataSetの配置パスを解決
        var dataSetPath = referFileEvidence(flowId);

        // DataSetの配置パスが存在しなかった場合はエラー
        if (Files.notExists(dataSetPath)) {
            throw new SystemException(RdbMessages.RDB_ERR_0006, dataSetPath.toString());
        }

        // データセット種別
        var dataSetType = DataSetType.valueOfByValue(command.getActualDataSetType());

        // データセット種別が解決できなかった場合はエラー
        if (dataSetType == null) {
            throw new SystemException(RdbMessages.RDB_ERR_0007, command.getActualDataSetType());
        }

        // データセット読み込み
        return DataSetUtils.getInstance().readDataSet(dataSetPath, dataSetType);
    }

}
