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
package org.tinygroup.convert.common;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ByteArrayToUTCDate implements Converter<byte[], Date> {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "yyyyMMdd-hh:mm:ss");
    private SimpleDateFormat simpleDateFormatWithMS = new SimpleDateFormat(
            "yyyyMMdd-hh:mm:ss.SSS");

    public Date convert(byte[] inputData) throws ConvertException {
        String str = new String(inputData);
        try {
            if (str.indexOf('.') > 0) {
                return simpleDateFormatWithMS.parse(str);
            } else {
                return simpleDateFormat.parse(str);
            }
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

}
