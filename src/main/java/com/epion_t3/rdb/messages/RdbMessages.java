/* Copyright (c) 2017-2020 Nozomu Takashima. */
package com.epion_t3.rdb.messages;

import com.epion_t3.core.message.Messages;

/**
 * rdb用メッセージ定義Enum.<br>
 *
 * @author epion-t3-devtools
 */
public enum RdbMessages implements Messages {

    /** DataSetのインポートに失敗しました. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0010("com.zomu.t.epion.t3.rdb.err.0010"),

    /** 1カラムのみを取得するクエリーを指定してください。 */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0021("com.zomu.t.epion.t3.rdb.err.0021"),

    /** RDBアクセスに失敗したため、DataSetのエクスポートに失敗しました. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0011("com.zomu.t.epion.t3.rdb.err.0011"),

    /** 1レコードのみを取得するクエリーを指定してください。 */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0022("com.zomu.t.epion.t3.rdb.err.0022"),

    /** 指定できるQueryは1つです。複数のクエリーを指定しないでください。 */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0020("com.zomu.t.epion.t3.rdb.err.0020"),

    /** Scriptのパスが指定されていません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0003("com.zomu.t.epion.t3.rdb.err.0003"),

    /** RDBへの接続先定義のRDB種別が不正です.対応するRDBの値を設定してください.RDB種別：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0014("com.zomu.t.epion.t3.rdb.err.0014"),

    /** Scriptのパスが存在しません.パス：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0004("com.zomu.t.epion.t3.rdb.err.0004"),

    /** RDBへのコネクションの確率に失敗しました. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0015("com.zomu.t.epion.t3.rdb.err.0015"),

    /** Queryが指定されていません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0001("com.zomu.t.epion.t3.rdb.err.0001"),

    /** RDBへの接続先定義は必須です. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0012("com.zomu.t.epion.t3.rdb.err.0012"),

    /** Queryの実行時にエラーが発生しました. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0002("com.zomu.t.epion.t3.rdb.err.0002"),

    /** RDBへの接続先定義のRDB種別は必須です. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0013("com.zomu.t.epion.t3.rdb.err.0013"),

    /** DataSetの種別が解決できません.種別：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0007("com.zomu.t.epion.t3.rdb.err.0007"),

    /** 結果値を参照するためのFlowIDが指定されていません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0018("com.zomu.t.epion.t3.rdb.err.0018"),

    /** CSVによるDataSetには現状対応していません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0008("com.zomu.t.epion.t3.rdb.err.0008"),

    /** カラムの型が解決できません.テーブル：{0},カラム：{1} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0019("com.zomu.t.epion.t3.rdb.err.0019"),

    /** DataSetのパスが指定されていません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0005("com.zomu.t.epion.t3.rdb.err.0005"),

    /** DataSetの読み込みに失敗しました.パス：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0016("com.zomu.t.epion.t3.rdb.err.0016"),

    /** DataSetのパスが存在しません.パス：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0006("com.zomu.t.epion.t3.rdb.err.0006"),

    /** 期待値のDataSetのパスが指定されていません. */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0017("com.zomu.t.epion.t3.rdb.err.0017"),

    /** インポート用のオペレーションではありません.オペレーション：{0} */
    RDB_COM_ZOMU_T_EPION_T3_RDB_ERR_0009("com.zomu.t.epion.t3.rdb.err.0009");

    /** メッセージコード */
    private String messageCode;

    /**
     * プライベートコンストラクタ<br>
     *
     * @param messageCode メッセージコード
     */
    private RdbMessages(final String messageCode) {
        this.messageCode = messageCode;
    }

    /**
     * messageCodeを取得します.<br>
     *
     * @return messageCode
     */
    public String getMessageCode() {
        return this.messageCode;
    }
}
