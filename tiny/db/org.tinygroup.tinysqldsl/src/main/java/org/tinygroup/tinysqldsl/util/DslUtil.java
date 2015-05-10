/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinysqldsl.util;

import org.tinygroup.tinysqldsl.select.OrderByElement;

import java.util.List;

/**
 * 工具类
 *
 * @author renhui
 *
 */
public class DslUtil {

    public static String orderByToString(List<OrderByElement> orderByElements) {
        return orderByToString(false, orderByElements);
    }

    public static String orderByToString(boolean oracleSiblings,
                                         List<OrderByElement> orderByElements) {
        return getFormattedList(orderByElements,
                oracleSiblings ? "ORDER SIBLINGS BY" : "ORDER BY");
    }

    public static String getFormattedList(List<?> list, String expression) {
        return getFormattedList(list, expression, true, false);
    }

    public static String getFormattedList(List<?> list, String expression,
                                          boolean useComma, boolean useBrackets) {
        String sql = getStringList(list, useComma, useBrackets);

        if (sql.length() > 0) {
            if (expression.length() > 0) {
                sql = " " + expression + " " + sql;
            } else {
                sql = " " + sql;
            }
        }

        return sql;
    }

    /**
     * List the toString out put of the objects in the List comma separated. If
     * the List is null or empty an empty string is returned.
     *
     * The same as getStringList(list, true, false)
     *
     * @see #getStringList(List, boolean, boolean)
     * @param list
     *            list of objects with toString methods
     * @return comma separated list of the elements in the list
     */
    public static String getStringList(List<?> list) {
        return getStringList(list, true, false);
    }

    /**
     * List the toString out put of the objects in the List that can be comma
     * separated. If the List is null or empty an empty string is returned.
     *
     * @param list
     *            list of objects with toString methods
     * @param useComma
     *            true if the list has to be comma separated
     * @param useBrackets
     *            true if the list has to be enclosed in brackets
     * @return comma separated list of the elements in the list
     */
    public static String getStringList(List<?> list, boolean useComma,
                                       boolean useBrackets, String comma) {
        StringBuffer buffer = new StringBuffer();
        if (!useComma) {
            comma = "";
        }
        if (list != null) {
            if (useBrackets) {
                buffer.append("(");
            }

            for (int i = 0; i < list.size(); i++) {
                buffer.append(list.get(i)).append(
                        (i < list.size() - 1) ? comma : "");
            }

            if (useBrackets) {
                buffer.append(")");
            }
        }

        return buffer.toString();
    }

    public static String getStringList(List<?> list, boolean useComma,
                                       boolean useBrackets) {
        return getStringList(list, useComma, useBrackets, ",");
    }

}
