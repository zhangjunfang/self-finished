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
package org.tinygroup.tinysqldsl.select;


/**
 * A fetch clause in the form FETCH (FIRST | NEXT) row_count (ROW | ROWS) ONLY
 */
public class Fetch {

    private long rowCount;
    private boolean fetchJdbcParameter = false;
    private boolean isFetchParamFirst = false;
    private String fetchParam = "ROW";

    public Fetch(long rowCount, boolean fetchJdbcParameter,
                 boolean isFetchParamFirst) {
        this(rowCount, fetchJdbcParameter, isFetchParamFirst, "ROW");
    }

    public Fetch(long rowCount, boolean fetchJdbcParameter,
                 boolean isFetchParamFirst, String fetchParam) {
        super();
        this.rowCount = rowCount;
        this.fetchJdbcParameter = fetchJdbcParameter;
        this.isFetchParamFirst = isFetchParamFirst;
        this.fetchParam = fetchParam;
    }

    public static Fetch fetchWithFirstRow(long rowCount) {
        return new Fetch(rowCount, false, true);
    }

    public static Fetch fetchWithFirstRowParam(long rowCount) {
        return new Fetch(rowCount, true, true);
    }

    public static Fetch fetchWithFirstRows(long rowCount) {
        return new Fetch(rowCount, false, true, "ROWS");
    }

    public static Fetch fetchWithFirstRowsParam(long rowCount) {
        return new Fetch(rowCount, true, true, "ROWS");
    }

    public static Fetch fetchWithNextRow(long rowCount) {
        return new Fetch(rowCount, false, false);
    }

    public static Fetch fetchWithNextRowParam(long rowCount) {
        return new Fetch(rowCount, true, false);
    }

    public static Fetch fetchWithNextRows(long rowCount) {
        return new Fetch(rowCount, false, false, "ROWS");
    }

    public static Fetch fetchWithNextRowsParam(long rowCount) {
        return new Fetch(rowCount, true, false, "ROWS");
    }

    public long getRowCount() {
        return rowCount;
    }

    public void setRowCount(long l) {
        rowCount = l;
    }

    public boolean isFetchJdbcParameter() {
        return fetchJdbcParameter;
    }

    public String getFetchParam() {
        return fetchParam;
    }

    public boolean isFetchParamFirst() {
        return isFetchParamFirst;
    }

    public void setFetchJdbcParameter(boolean b) {
        fetchJdbcParameter = b;
    }

    public void setFetchParam(String s) {
        this.fetchParam = s;
    }

    public void setFetchParamFirst(boolean b) {
        this.isFetchParamFirst = b;
    }

    @Override
    public String toString() {
        return " FETCH " + (isFetchParamFirst ? "FIRST" : "NEXT") + " "
                + (fetchJdbcParameter ? "?" : rowCount + "") + " " + fetchParam
                + " ONLY";
    }
}
