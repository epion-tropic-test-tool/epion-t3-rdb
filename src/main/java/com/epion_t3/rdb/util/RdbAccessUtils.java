/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.util;

import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.handler.SnowflakeMetadataHandler;
import com.epion_t3.rdb.messages.RdbMessages;
import com.epion_t3.rdb.type.RdbType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author takashno
 */
@Slf4j
public final class RdbAccessUtils {

    /**
     * シングルトンインスタンス.
     */
    private static final RdbAccessUtils instance = new RdbAccessUtils();

    /**
     * プライベートコンストラクタ.
     */
    private RdbAccessUtils() {
    }

    /**
     * シングルトンインスタンスを取得.
     *
     * @return
     */
    public static RdbAccessUtils getInstance() {
        return instance;
    }

    /**
     * データソースを取得.
     *
     * @param rdbConnectionConfiguration RDB接続設定
     * @return {@link DataSource}
     */
    public DataSource getDataSource(RdbConnectionConfiguration rdbConnectionConfiguration) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(rdbConnectionConfiguration.getDriverClassName());
        dataSource.setUrl(rdbConnectionConfiguration.getUrl());
        dataSource.setUsername(rdbConnectionConfiguration.getUsername());
        dataSource.setPassword(rdbConnectionConfiguration.getPassword());
        return dataSource;
    }

    public IDatabaseConnection getDatabaseConnection(RdbConnectionConfiguration rdbConnectionConfiguration) {
        DataSource dataSource = getDataSource(rdbConnectionConfiguration);
        return getDatabaseConnection(rdbConnectionConfiguration, dataSource);
    }

    public IDatabaseConnection getDatabaseConnection(RdbConnectionConfiguration rdbConnectionConfiguration,
            DataSource dataSource) {

        if (StringUtils.isEmpty(rdbConnectionConfiguration.getRdbKind())) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0013);
        }

        String schema = rdbConnectionConfiguration.getSchema();

        RdbType rdbType = RdbType.valueOfByValue(rdbConnectionConfiguration.getRdbKind());

        IDatabaseConnection conn = null;

        try {

            if (rdbType != null) {
                switch (rdbType) {
                case ORACLE:
                    if (StringUtils.isNotEmpty(schema)) {
                        conn = new DatabaseDataSourceConnection(dataSource, schema);
                    } else {
                        conn = new DatabaseDataSourceConnection(dataSource);
                    }
                    DatabaseConfig configOracle = conn.getConfig();
                    configOracle.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
                    break;
                case MYSQL:
                    if (StringUtils.isNotEmpty(schema)) {
                        conn = new DatabaseDataSourceConnection(dataSource, schema);
                    } else {
                        conn = new DatabaseDataSourceConnection(dataSource);
                    }
                    DatabaseConfig configMysql = conn.getConfig();
                    configMysql.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
                    configMysql.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
                    break;
                case POSTGRESQL:
                    if (StringUtils.isNotEmpty(schema)) {
                        conn = new DatabaseDataSourceConnection(dataSource, schema);
                    } else {
                        conn = new DatabaseDataSourceConnection(dataSource);
                    }
                    DatabaseConfig configPostgre = conn.getConfig();
                    configPostgre.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                            new PostgresqlDataTypeFactory());
                    break;
                case SNOWFLAKE:
                    if (StringUtils.isNotEmpty(schema)) {
                        conn = new DatabaseDataSourceConnection(dataSource, schema);
                    } else {
                        conn = new DatabaseDataSourceConnection(dataSource);
                    }
                    DatabaseConfig configSnowflake = conn.getConfig();
                    configSnowflake.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());
                    configSnowflake.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
                            new SnowflakeMetadataHandler(rdbConnectionConfiguration.getDbName()));
                    break;
                default:
                    throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0014,
                            rdbConnectionConfiguration.getRdbKind());
                }
                return conn;
            } else {
                throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0014,
                        rdbConnectionConfiguration.getRdbKind());
            }

        } catch (SQLException e) {
            throw new SystemException(RdbMessages.RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0015);
        }

    }
}
