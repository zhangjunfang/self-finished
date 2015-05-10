/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.template.rumtime;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.rumtime.convert.*;
import org.tinygroup.template.rumtime.operator.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作符工具类
 * 之所以起一个字母，是为了生成的代码短一些
 * Created by luoguo on 2014/6/5.
 */
public final class O {
    private O() {
    }

    private static Map<String, Operator> operationMap = new HashMap<String, Operator>();
    private static Map<String, OperatorWithContext> operationWithContextMap = new HashMap<String, OperatorWithContext>();
    private static Converter[][] converters = new Converter[7][7];
    private static Map<Class, Integer> typeMap = new HashMap<Class, Integer>();

    static {
        typeMap.put(Byte.class, 0);
        typeMap.put(Character.class, 1);
        typeMap.put(Integer.class, 2);
        typeMap.put(Long.class, 3);
        typeMap.put(Float.class, 4);
        typeMap.put(Double.class, 5);
        typeMap.put(BigDecimal.class, 6);

        addConverter(new ByteCharacter());
        addConverter(new ByteInteger());
        addConverter(new ByteFloat());
        addConverter(new ByteDouble());
        addConverter(new ByteBigDecimal());

        addConverter(new CharacterInteger());
        addConverter(new CharacterFloat());
        addConverter(new CharacterDouble());
        addConverter(new CharacterBigDecimal());

        addConverter(new IntegerLong());
        addConverter(new IntegerFloat());
        addConverter(new IntegerDouble());
        addConverter(new IntegerBigDecimal());

        addConverter(new LongFloat());
        addConverter(new LongDouble());
        addConverter(new LongBigDecimal());

        addConverter(new FloatDouble());
        addConverter(new FloatBigDecimal());

        addConverter(new DoubleBigDecimal());
        //数学操作
        addOperator(new AddOperator());
        addOperator(new SubtractOperator());
        addOperator(new MultiplyOperator());
        addOperator(new DevideOperator());
        addOperator(new XorOperator());
        addOperator(new AdOperator());
        addOperator(new OrOperator());
        addOperator(new ModOperator());
        //简化三元操作符
        addOperator(new SimpleConditionOperator());
        //一元操作符
        addOperator(new LeftAddOperator());
        addOperator(new LeftSubtractOperator());
        addOperator(new LeftPlusPlusOperator());
        addOperator(new LeftSubtractSubtractOperator());
        addOperator(new RightPlusPlusOperator());
        addOperator(new RightSubtractSubtractOperator());
        addOperator(new LeftLiteralOperator());
        addOperator(new ComplementOperator());
        addOperator(new LeftNotOperator());
        //逻辑比较符
        addOperator(new EqualsOperator());
        addOperator(new NotEqualsOperator());
        addOperator(new LessEqualsOperator());
        addOperator(new LessOperator());
        addOperator(new BigOperator());
        addOperator(new BigEqualsOperator());
        //移位运算符
        addOperator(new ShlOperator());
        addOperator(new ShrOperator());
        addOperator(new Shr2Operator());
    }

    public static Object convert(Object object, Class sourceType, Class destType) {
        return converters[typeMap.get(sourceType)][typeMap.get(destType)].convert(object);
    }

    public static int compare(Class type1, Class type2) {
        int index1 = typeMap.get(type1);
        int index2 = typeMap.get(type2);
        if (index1 == index2) {
            return 0;
        }
        if (index1 > index2) {
            return 1;
        } else {
            return -1;
        }
    }

    public static boolean isNumber(Class type) {
        return typeMap.containsKey(type);
    }

    public static void addConverter(Converter converter) {
        converters[typeMap.get(converter.getSourceType())][typeMap.get(converter.getDestType())] = converter;
    }

    public static void addOperator(Operator operator) {
        operationMap.put(operator.getOperation(), operator);
    }

    public static void addOperator(OperatorWithContext operator) {
        operationWithContextMap.put(operator.getOperation(), operator);
    }

    public static Object e(String op, Object... parameters) throws TemplateException {
        Operator operator = operationMap.get(op);
        if (operator == null) {
            throw new TemplateException("找不对对应于：" + op + "的处理器。");
        }
        return operator.operation(parameters);
    }

    public static Object ce(TemplateContext context, String op,String name, Object value) throws TemplateException {
        OperatorWithContext operator = operationWithContextMap.get(op);
        if (operator == null) {
            throw new TemplateException("找不对对应于：" + op + "的处理器。");
        }
        return operator.operation(context,name, value);
    }
}
