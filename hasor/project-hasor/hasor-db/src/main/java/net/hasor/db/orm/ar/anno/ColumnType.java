/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.db.orm.ar.anno;
/**
 * 列类型
 * @version : 2015年2月18日
 * @author 赵永春(zyc@hasor.net)
 */
public enum ColumnType {
    /**列定义只来源于字段*/
    FieldOnly,
    /**列定义只来源于Column注解*/
    ColumnOnly,
}