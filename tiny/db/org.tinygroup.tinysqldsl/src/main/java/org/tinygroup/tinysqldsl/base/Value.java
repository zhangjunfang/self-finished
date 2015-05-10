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
package org.tinygroup.tinysqldsl.base;

/**
 * 插入、更新时的值对象
 * Created by luoguo on 2015/3/11.
 */
public class Value {
    /**
     * 列信息
     */
    private Column column;
    /**
     * 对应的值
     */
    private Object value;

    public Value(Column column, Object value) {
        this.column = column;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public Column getColumn() {
        return column;
    }

}