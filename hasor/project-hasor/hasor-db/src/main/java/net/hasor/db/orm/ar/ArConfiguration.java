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
package net.hasor.db.orm.ar;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.hasor.db.orm.ar.anno.AllowPolicy;
import net.hasor.db.orm.ar.anno.Table;
import org.more.util.BeanUtils;
import org.more.util.StringUtils;
/**
 * 用来表示数据库。
 * @version : 2014年10月27日
 * @author 赵永春(zyc@hasor.net)
 */
public final class ArConfiguration {
    private static ConcurrentHashMap<Class<?>, Sechma> sechmaMap = new ConcurrentHashMap<Class<?>, Sechma>();
    //
    protected Identify getIdentify(Sechma sechma) {
        //  Auto-generated method stub
        return null;
    }
    //
    public Sechma loadSechma(Class<?> sechmaType) {
        Sechma sechma = sechmaMap.get(sechmaType);
        if (sechma == null) {
            sechma = parseSechma(sechmaType);
            sechmaMap.putIfAbsent(sechmaType, sechma);
        }
        return sechmaMap.get(sechmaType);
    }
    private static Sechma parseSechma(Class<?> sechmaType) {
        Table table = sechmaType.getAnnotation(Table.class);
        if (table == null) {
            return null;
        }
        //
        String tableName = StringUtils.isBlank(table.tableName()) ? sechmaType.getSimpleName() : table.tableName();
        Sechma sechma = new Sechma(tableName.toUpperCase());
        sechma.setIgnoreUnset(table.ignoreUnset());
        //
        List<Field> propNames = BeanUtils.findALLFields(sechmaType);
        for (Field propField : propNames) {
            Class<?> propType = propField.getType();
            Column col = new Column(propField.getName(), InnerArUtils.javaTypeToSqlType(propType));
            col.setBeanField(propField);
            col.setIgnoreUnset(sechma.isIgnoreUnset());
            //
            net.hasor.db.orm.ar.anno.Column column = propField.getAnnotation(net.hasor.db.orm.ar.anno.Column.class);
            if (column != null) {
                col.setMaxSize(column.size());
                col.setName(column.column());
                AllowPolicy policy = column.policy();
                col.setAllowInsert(policy.insert());
                col.setAllowUpdate(policy.update());
                col.setAllowDeleteWhere(policy.deleteWhere());
                col.setAllowUpdateWhere(policy.updateWhere());
                col.setIgnoreUnset(policy.ignoreUnset());
                col.setEmpty(column.isNull());
            }
            //
            sechma.addColumn(col);
        }
        //
        return sechma;
    }
}