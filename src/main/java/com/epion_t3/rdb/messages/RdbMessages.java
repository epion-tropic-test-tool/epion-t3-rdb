/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.messages;

import com.epion_t3.core.message.Messages;

/**
 * rdb用メッセージ定義Enum.<br>
 *
 * @author epion-t3-devtools
 */
public enum RdbMessages implements Messages {

    /** 結果値を参照するためのFlowIDが指定されていません. */
    RDB_ERR_0018("com.epion_t3.rdb.err.0018"),

    /** カラムの型が解決できません.テーブル : {0},カラム : {1} */
    RDB_ERR_0019("com.epion_t3.rdb.err.0019"),

    /** DataSetの読み込みに失敗しました.パス : {0} */
    RDB_ERR_0016("com.epion_t3.rdb.err.0016"),

    /** 期待値のDataSetのパスが指定されていません. */
    RDB_ERR_0017("com.epion_t3.rdb.err.0017"),

    /** RDBへの接続先定義のRDB種別が不正です.対応するRDBの値を設定してください.RDB種別 : {0} */
    RDB_ERR_0014("com.epion_t3.rdb.err.0014"),

    /** RDBへのコネクションの確率に失敗しました. */
    RDB_ERR_0015("com.epion_t3.rdb.err.0015"),

    /** RDBへの接続先定義は必須です. */
    RDB_ERR_0012("com.epion_t3.rdb.err.0012"),

    /** RDBへの接続先定義のRDB種別は必須です. */
    RDB_ERR_0013("com.epion_t3.rdb.err.0013"),

    /** DataSetのインポートに失敗しました. */
    RDB_ERR_0010("com.epion_t3.rdb.err.0010"),

    /** RDBアクセスに失敗したため、DataSetのエクスポートに失敗しました. */
    RDB_ERR_0011("com.epion_t3.rdb.err.0011"),

    /** DataSetの種別が解決できません.種別：{0} */
    RDB_ERR_0007("com.epion_t3.rdb.err.0007"),

    /** CSVによるDataSetには現状対応していません. */
    RDB_ERR_0008("com.epion_t3.rdb.err.0008"),

    /** DataSetのパスが指定されていません. */
    RDB_ERR_0005("com.epion_t3.rdb.err.0005"),

    /** DataSetのパスが存在しません.パス：{0} */
    RDB_ERR_0006("com.epion_t3.rdb.err.0006"),

    /** Scriptのパスが指定されていません. */
    RDB_ERR_0003("com.epion_t3.rdb.err.0003"),

    /** テーブルの設定ファイルが見つかりません. テーブル設定ファイル : {0} */
    RDB_ERR_0025("com.epion_t3.rdb.err.0025"),

    /** Scriptのパスが存在しません.パス：{0} */
    RDB_ERR_0004("com.epion_t3.rdb.err.0004"),

    /** 未対応のDataSetの種別です. 種別 : {0} */
    RDB_ERR_0026("com.epion_t3.rdb.err.0026"),

    /** Queryが指定されていません. */
    RDB_ERR_0001("com.epion_t3.rdb.err.0001"),

    /** 指定されたスクリプトは空です. スクリプト : {0} */
    RDB_ERR_0023("com.epion_t3.rdb.err.0023"),

    /** Queryの実行時にエラーが発生しました. */
    RDB_ERR_0002("com.epion_t3.rdb.err.0002"),

    /** テーブルの設定ファイルは「yaml」「yml」「json」のいずれかで指定して下さい. テーブル設定ファイル : {0} */
    RDB_ERR_0024("com.epion_t3.rdb.err.0024"),

    /** 1カラムのみを取得するクエリーを指定してください. */
    RDB_ERR_0021("com.epion_t3.rdb.err.0021"),

    /** 1レコードのみを取得するクエリーを指定してください. */
    RDB_ERR_0022("com.epion_t3.rdb.err.0022"),

    /** 指定できるQueryは1つです.複数のクエリーを指定しないでください. */
    RDB_ERR_0020("com.epion_t3.rdb.err.0020"),

    /** インポート用のオペレーションではありません.オペレーション：{0} */
    RDB_ERR_0009("com.epion_t3.rdb.err.0009");

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
