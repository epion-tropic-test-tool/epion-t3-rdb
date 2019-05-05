package com.epion_t3.rdb.command.runner;

import com.epion_t3.core.command.bean.CommandResult;
import com.epion_t3.core.command.runner.impl.AbstractCommandRunner;
import com.epion_t3.core.exception.SystemException;
import com.epion_t3.rdb.command.model.ExecuteRdbQuery;
import com.epion_t3.rdb.configuration.model.RdbConnectionConfiguration;
import com.epion_t3.rdb.message.RdbMessages;
import com.epion_t3.rdb.util.RdbAccessUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * RDBに対してクエリー実行処理．
 *
 * @author takashno
 */
@Slf4j
public class ExecuteRdbQueryRunner extends AbstractCommandRunner<ExecuteRdbQuery> {

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandResult execute(ExecuteRdbQuery command, Logger logger) throws Exception {

        // 接続先は必須
        if (StringUtils.isEmpty(command.getRdbConnectConfigRef())) {
            throw new SystemException(RdbMessages.RDB_ERR_0002);
        }

        // 接続先設定を参照
        RdbConnectionConfiguration rdbConnectionConfiguration =
                referConfiguration(command.getRdbConnectConfigRef());

        // クエリー文字列を取得
        String query = command.getValue();

        // クエリーは必須
        if (StringUtils.isEmpty(query)) {
            throw new SystemException(RdbMessages.RDB_ERR_0001);
        }

        // 複数行の場合は、セミコロンで区切られている
        String[] queries = query.split(";");

        // データソースを取得
        DataSource dataSource = RdbAccessUtils.getInstance().getDataSource(rdbConnectionConfiguration);

        try (Connection conn = dataSource.getConnection()) {
            for (String q : queries) {
                if (StringUtils.isNotEmpty(q)) {
                    try (PreparedStatement statement = conn.prepareStatement(q)) {
                        log.trace("execute query -> {}", q);
                        statement.execute(q);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SystemException(RdbMessages.RDB_ERR_0002, e);
        }

        return CommandResult.getSuccess();
    }

}
