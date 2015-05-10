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
package org.tinygroup.tinysqldsl.expression;

import org.tinygroup.tinysqldsl.base.StatementSqlBuilder;

/**
 * Every number without a point or an exponential format is a LongValue
 */
public class LongValue implements Expression {

    private long value;
    private String stringValue;

    public LongValue(final String value) {
        String val = value;
        if (val.charAt(0) == '+') {
            val = val.substring(1);
        }
        this.value = Long.parseLong(val);
        this.stringValue = val;
    }

    public LongValue(long value) {
        this.value = value;
        stringValue = String.valueOf(value);
    }

    public LongValue() {
        super();
    }


    public long getValue() {
        return value;
    }

    public void setValue(long d) {
        value = d;
        stringValue = d + "";
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String string) {
        stringValue = string;
    }


    public String toString() {
        return getStringValue();
    }

	public void builder(StatementSqlBuilder builder) {
		builder.appendSql(stringValue);
	}
}
