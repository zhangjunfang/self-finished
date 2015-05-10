package org.tinygroup.dbrouter.impl.shardrule;

import java.util.List;

import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.operators.relational.EqualsTo;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;
import org.tinygroup.jsqlparser.statement.update.Update;


public class ShardRuleMatchWithSections {

    private List<Section> sections;
    private String tableName;
    private String fieldName;
    private Partition partition;
    private Object[] preparedParams;

    public ShardRuleMatchWithSections(List<Section> sections, String tableName,
                                      String fieldName, Partition partition, Object[] preparedParams) {
        super();
        this.sections = sections;
        this.tableName = tableName;
        this.fieldName = fieldName;
        this.partition = partition;
        this.preparedParams = preparedParams;
    }

    public boolean insertMatch(Statement statement) {
        Insert insert = (Insert) statement;
        int paramIndex = 0;
        if (tableName.equalsIgnoreCase(insert.getTable().getName())) {
            ItemsList itemsList = insert.getItemsList();
            if (itemsList instanceof ExpressionList) {
                List<Expression> expressions = ((ExpressionList) itemsList)
                        .getExpressions();
                for (int i = 0; i < insert.getColumns().size(); i++) {
                    Column column = insert.getColumns().get(i);
                    Expression expression = expressions.get(i);
                    if (column.getColumnName().equalsIgnoreCase(
                            fieldName)) {
                        if (expression instanceof LongValue) {
                            LongValue longValue = (LongValue) expression;
                            if (isInScope(sections, longValue.getValue())) {
                                return true;
                            }
                        } else if (expression instanceof JdbcParameter) {
                            Long value = (Long) preparedParams[paramIndex];
                            if (isInScope(sections, value)) {
                                return true;
                            }
                        }
                    }
                    if (expression instanceof JdbcParameter) {
                        paramIndex++;
                    }
                }
            }
        }
        return false;
    }

    public boolean updateMatch(Statement statement) {
        Update update = (Update) statement;
        List<Expression> expressions = update.getExpressions();
        int paramIndex = 0;
        for (Expression expression : expressions) {
            if (expression instanceof JdbcParameter) {
                paramIndex++;
            }
        }
        List<Table> tables = update.getTables();
        if (tables != null) {
            for (Table table : tables) {
                if (tableName.equalsIgnoreCase(table.getName())) {
                    return getWhereExpression(paramIndex, update.getWhere(), partition, preparedParams);
                }
            }
        }
        return false;
    }

    public boolean deleteMatch(Statement statement) {
        Delete delete = (Delete) statement;
        if (tableName.equalsIgnoreCase(delete.getTable().getName())) {
            return getWhereExpression(0, delete.getWhere(), partition, preparedParams);
        }
        return false;
    }

    public boolean selectMatch(Statement statement) {
        Select select = (Select) statement;
        SelectBody body = select.getSelectBody();
        if (body instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) body;
            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                if (tableName.equalsIgnoreCase(table.getName())) {
                    return getWhereExpression(0, plainSelect.getWhere(),
                            partition, preparedParams);
                }
            }
        }
        return false;
    }

    private boolean getWhereExpression(int paramIndex, Expression where, Partition partition, Object... preparedParams) {
        if (where == null) {
            return true;
        }
        return getEqualsToExpression(paramIndex, where, partition, preparedParams);

    }

    private boolean getEqualsToExpression(int paramIndex, Expression where, Partition partition, Object... preparedParams) {
        if (where instanceof EqualsTo) {
            EqualsTo equalsTo = (EqualsTo) where;
            Expression leftExpression = equalsTo.getLeftExpression();
            Expression rightExpression = equalsTo.getRightExpression();
            if (leftExpression instanceof Column) {
                Column column = (Column) leftExpression;
                if (column.getColumnName().equalsIgnoreCase(fieldName)) {
                    if (rightExpression instanceof LongValue) {
                        LongValue longValue = (LongValue) rightExpression;
                        if (isInScope(sections, longValue.getValue())) {
                            return true;
                        }
                    } else if (rightExpression instanceof JdbcParameter) {
                        Long value = (Long) preparedParams[paramIndex];
                        if (isInScope(sections, value)) {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }

    private boolean isInScope(List<Section> sections, long value) {
        for (Section section : sections) {
            if (section.getStart() > value) {
                return false;
            }
            if (isInScope(section, value)) {
                return true;
            }
        }
        return false;

    }

    private boolean isInScope(Section section, long value) {
        return section.getStart() <= value && value <= section.getEnd();

    }

}
