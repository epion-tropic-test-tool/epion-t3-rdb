/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.configuration.model;

import com.epion_t3.core.common.annotation.CustomConfigurationDefinition;
import com.epion_t3.core.common.bean.scenario.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * RDBへの接続定義.
 *
 * @author takashno
 */
@Getter
@Setter
@CustomConfigurationDefinition(id = "RdbConnectionConfiguration")
public class RdbConnectionConfiguration extends Configuration {

    /**
     * ドライバクラス名.
     */
    private String driverClassName;

    /**
     * JDBC接続URL.
     */
    private String url;

    /**
     * ユーザ名.
     */
    private String username;

    /**
     * パスワード.
     */
    private String password;

    /**
     * スキーマ.
     */
    private String schema;

    /**
     * RDB種別.
     */
    private String rdbKind;

    /**
     * データベース名.
     * 
     * @since 0.0.4
     */
    private String dbName;

}
