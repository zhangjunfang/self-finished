package org.tinygroup.databasebuinstaller.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.database.customesql.CustomSqlProcessor;

public class CustomSqlInstallProcessor extends AbstractInstallProcessor {

    private CustomSqlProcessor customSqlProcessor;

    public void setCustomSqlProcessor(CustomSqlProcessor customSqlProcessor) {
        this.customSqlProcessor = customSqlProcessor;
    }

    public List<String> getDealSqls(String language, Connection con) throws SQLException {
        List<String> customSqls = new ArrayList<String>();

        //根据顺序加载自定义sql
        customSqls.addAll(customSqlProcessor.getCustomSqls(CustomSqlProcessor.BEFORE, CustomSqlProcessor.STANDARD_SQL_TYPE));
        customSqls.addAll(customSqlProcessor.getCustomSqls(CustomSqlProcessor.BEFORE, language));

        customSqls.addAll(customSqlProcessor.getCustomSqls(CustomSqlProcessor.AFTER, CustomSqlProcessor.STANDARD_SQL_TYPE));
        customSqls.addAll(customSqlProcessor.getCustomSqls(CustomSqlProcessor.AFTER, language));

        return customSqls;
    }

}
