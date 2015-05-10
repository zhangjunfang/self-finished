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
package org.tinygroup.context2object.impl;

import org.tinygroup.context2object.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectDateTypeConverter implements TypeConverter<String, Date> {
    private static Map<Pattern, SimpleDateFormat> formatMap = new HashMap<Pattern, SimpleDateFormat>();
    private static Map<Pattern, SimpleDateFormat> formatMap2 = new HashMap<Pattern, SimpleDateFormat>();
    static {
        Pattern patternByEn = Pattern.compile("\\d{1,4}[-]\\d{1,2}[-]\\d{1,2}",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern patternByZh = Pattern.compile("\\d{1,4}[年]\\d{1,2}[月]\\d{1,2}[日]",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern patternByEnTimestamp = Pattern.compile("\\d{1,4}[-]\\d{1,2}[-]\\d{1,2}(\\s)*\\d{1,2}(:)\\d{1,2}(:)\\d{1,2}",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Pattern patternByZhTimestamp = Pattern.compile("\\d{1,4}[年]\\d{1,2}[月]\\d{1,2}[日](\\s)*\\d{1,2}(:)\\d{1,2}(:)\\d{1,2}",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        formatMap2.put(patternByEn, new SimpleDateFormat("yyyy-MM-dd"));
        formatMap2.put(patternByZh, new SimpleDateFormat("yyyy年MM月dd日"));
        formatMap.put(patternByEnTimestamp, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formatMap.put(patternByZhTimestamp, new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"));
    }


    public Class<String> getSourceType() {
        return String.class;
    }

    public Class<Date> getDestinationType() {
        return Date.class;
    }

    public Date getObject(String value) {
        try {
            for (Pattern pattern : formatMap.keySet()) {
                Matcher matcher = pattern.matcher(value);
                if (matcher.find()) {
                    return formatMap.get(pattern).parse(value);
                }
            }
            for (Pattern pattern : formatMap2.keySet()) {
                Matcher matcher = pattern.matcher(value);
                if (matcher.find()) {
                    return formatMap2.get(pattern).parse(value);
                }
            }
            throw new RuntimeException("不能识别的日期格式：" + value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
