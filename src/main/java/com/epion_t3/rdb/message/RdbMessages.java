package com.epion_t3.rdb.message;

import com.epion_t3.core.message.Messages;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RDB関連のメッセージ定義.
 *
 * @author takashno
 */
@Getter
@AllArgsConstructor
public enum RdbMessages implements Messages {

    RDB_ERR_0001("com.zomu.t.epion.t3.rdb.err.0001"),
    RDB_ERR_0002("com.zomu.t.epion.t3.rdb.err.0002"),
    RDB_ERR_0003("com.zomu.t.epion.t3.rdb.err.0003"),
    RDB_ERR_0004("com.zomu.t.epion.t3.rdb.err.0004"),
    RDB_ERR_0005("com.zomu.t.epion.t3.rdb.err.0005"),
    RDB_ERR_0006("com.zomu.t.epion.t3.rdb.err.0006"),
    RDB_ERR_0007("com.zomu.t.epion.t3.rdb.err.0007"),
    RDB_ERR_0008("com.zomu.t.epion.t3.rdb.err.0008"),
    RDB_ERR_0009("com.zomu.t.epion.t3.rdb.err.0009"),
    RDB_ERR_0010("com.zomu.t.epion.t3.rdb.err.0010"),
    RDB_ERR_0011("com.zomu.t.epion.t3.rdb.err.0011"),
    RDB_ERR_0012("com.zomu.t.epion.t3.rdb.err.0012"),
    RDB_ERR_0013("com.zomu.t.epion.t3.rdb.err.0013"),
    RDB_ERR_0014("com.zomu.t.epion.t3.rdb.err.0014"),
    RDB_ERR_0015("com.zomu.t.epion.t3.rdb.err.0015"),
    RDB_ERR_0016("com.zomu.t.epion.t3.rdb.err.0016"),
    RDB_ERR_0017("com.zomu.t.epion.t3.rdb.err.0017"),
    RDB_ERR_0018("com.zomu.t.epion.t3.rdb.err.0018"),
    RDB_ERR_0019("com.zomu.t.epion.t3.rdb.err.0019"),
    ;

    /**
     * メッセージコード.
     */
    private String messageCode;
}
