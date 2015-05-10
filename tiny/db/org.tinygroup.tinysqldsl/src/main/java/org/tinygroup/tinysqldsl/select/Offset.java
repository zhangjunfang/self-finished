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
 * An offset clause in the form OFFSET offset or in the form OFFSET offset (ROW
 * | ROWS)
 */
public class Offset {

    private long offset;
    private boolean offsetJdbcParameter = false;
    private String offsetParam = null;

    public Offset(long offset, boolean offsetJdbcParameter, String offsetParam) {
        super();
        this.offset = offset;
        this.offsetJdbcParameter = offsetJdbcParameter;
        this.offsetParam = offsetParam;
    }

    public static Offset offsetRow(long offset) {
        return new Offset(offset, false, "ROW");
    }

    public Offset offsetRows(long offset) {
        return new Offset(offset, false, "ROWS");
    }

    public Offset offsetRowWithParam(long offset) {
        return new Offset(offset, true, "ROW");
    }

    public Offset offsetRowsWithParam(long offset) {
        return new Offset(offset, true, "ROWS");
    }

    public long getOffset() {
        return offset;
    }

    public String getOffsetParam() {
        return offsetParam;
    }

    public void setOffset(long l) {
        offset = l;
    }

    public void setOffsetParam(String s) {
        offsetParam = s;
    }

    public boolean isOffsetJdbcParameter() {
        return offsetJdbcParameter;
    }

    public void setOffsetJdbcParameter(boolean b) {
        offsetJdbcParameter = b;
    }

    @Override
    public String toString() {
        return " OFFSET " + (offsetJdbcParameter ? "?" : offset)
                + (offsetParam != null ? " " + offsetParam : "");
    }
}
