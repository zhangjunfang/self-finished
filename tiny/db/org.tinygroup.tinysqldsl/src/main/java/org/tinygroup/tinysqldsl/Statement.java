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
package org.tinygroup.tinysqldsl;

import java.util.List;

/**
 * 语句接口
 * Created by luoguo on 2015/3/11.
 */
public interface Statement {
    /**
     * 返回语句对应的SQL
     *
     * @return
     */
    String sql();

    /**
     * 语句的标识，仅用于日志记录时更清晰
     * @param id
     */
    void id(String id);

    /**
     * 返回语句中值的列表（对应于?）
     *
     * @return
     */
    List<Object> getValues();
}
