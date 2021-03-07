/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.handler;

import lombok.AllArgsConstructor;
import org.dbunit.database.DefaultMetadataHandler;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Snowflake用のDBUnit拡張 - DBメタデータハンドラ.
 */
@AllArgsConstructor
public class SnowflakeMetadataHandler extends DefaultMetadataHandler {

    private String dbName;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String schemaName, String[] tableType) throws SQLException {
        return metaData.getTables(this.dbName, schemaName, "%", tableType);
    }
}
