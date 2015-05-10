package org.tinygroup.tinysqldsl.base;

import org.tinygroup.tinysqldsl.expression.FragmentExpressionSql;
import org.tinygroup.tinysqldsl.formitem.FragmentFromItemSql;
import org.tinygroup.tinysqldsl.selectitem.FragmentSelectItemSql;

/**
 * 保存sql片段的对象
 * @author renhui
 *
 */
public class FragmentSql {
    /**
     * sql片段
     */
    private String fragment;

    public FragmentSql(String fragment) {
        super();
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment;
    }

    public static FragmentSelectItemSql fragmentSelect(String fragment) {
        return new FragmentSelectItemSql(fragment);
    }

    public static FragmentFromItemSql fragmentFrom(String fragment) {
        return new FragmentFromItemSql(fragment);
    }

    public static Condition fragmentCondition(String fragment, Object... values) {
        FragmentExpressionSql fragmentExpressionSql = new FragmentExpressionSql(fragment);
        return new Condition(fragmentExpressionSql, values);
    }

    @Override
    public String toString() {
        return fragment;
    }
}
