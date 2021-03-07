/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.util;

import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.type.DataSetType;
import com.epion_t3.rdb.writer.XlsxDataSetWriter;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

import java.io.*;
import java.nio.file.Path;

public final class DataSetUtils {

    /**
     * シングルトンインスタンス.
     */
    private static final DataSetUtils instance = new DataSetUtils();

    /**
     * プライベートコンストラクタ.
     */
    private DataSetUtils() {
        // Do Nothing...
    }

    /**
     * シングルトンインスタンスを取得.
     *
     * @return シングルトンインスタンス
     */
    public static DataSetUtils getInstance() {
        return instance;
    }

    /**
     * DataSet読込.
     *
     * @param path パス
     * @param dataSetType DataSet種別
     * @return DataSet
     */
    public IDataSet readDataSet(Path path, DataSetType dataSetType) {

        // データセット種別が解決できなかった場合はエラー
        if (dataSetType == null) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0007, dataSetType);
        }

        // データセット読み込み
        IDataSet iDataSet = null;

        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            switch (dataSetType) {
            case CSV:
                // TODO
                // iDataSet = new CsvDataSet(dataSetPath.toFile());
                // break;
                throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0008);
            case XML:
                iDataSet = new XmlDataSet(fis);
                break;
            case FLAT_XML:
                FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                builder.setDtdMetadata(true);
                builder.setColumnSensing(true);
                iDataSet = builder.build(fis);
                break;
            case EXCEL:
                iDataSet = new XlsDataSet(fis);
                break;
            default:
                // ありえない
                throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0007, dataSetType);
            }
            return iDataSet;
        } catch (IOException | DataSetException e) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0016, path.toString());
        }
    }

}
