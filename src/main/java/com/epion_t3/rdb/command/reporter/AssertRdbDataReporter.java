/* Copyright (c) 2017-2021 Nozomu Takashima. */
package com.epion_t3.rdb.command.reporter;

import com.epion_t3.core.command.reporter.impl.AbstractThymeleafCommandReporter;
import com.epion_t3.core.common.bean.ExecuteCommand;
import com.epion_t3.core.common.context.ExecuteContext;
import com.epion_t3.core.common.bean.ExecuteFlow;
import com.epion_t3.core.common.bean.ExecuteScenario;
import com.epion_t3.rdb.bean.AssertRdbDataResult;
import com.epion_t3.rdb.command.model.AssertRdbData;

import java.util.Map;

public class AssertRdbDataReporter extends AbstractThymeleafCommandReporter<AssertRdbData, AssertRdbDataResult> {
    @Override
    public String templatePath() {
        return "assert-rdb-data-report";
    }

    @Override
    public void setVariables(Map<String, Object> variable, AssertRdbData command, AssertRdbDataResult commandResult,
            ExecuteContext executeContext, ExecuteScenario executeScenario, ExecuteFlow executeFlow,
            ExecuteCommand executeCommand) {

        variable.put("tables", commandResult.getTables());
    }
}
