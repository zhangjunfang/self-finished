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
package org.tinygroup.jspengine.util;

import java.util.Date;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * Fast date formatter that caches recently formatted date information
 * and uses it to avoid too-frequent calls to the underlying
 * formatter.  Note: breaks fieldPosition param of format(Date,
 * StringBuffer, FieldPosition).  If you care about the field
 * position, call the underlying DateFormat directly.
 *
 * @author Stan Bailes
 * @author Alex Chaffee
 */
public class FastDateFormat extends DateFormat {

    private DateFormat df;
    private long lastSec = -1;
    private StringBuffer sb = new StringBuffer();
    private FieldPosition fp = new FieldPosition(DateFormat.MILLISECOND_FIELD);
    
    public FastDateFormat(DateFormat df) {
        this.df = df;
    }

    public Date parse(String text, ParsePosition pos) {
	return df.parse(text, pos);
    }

    /**
     * Note: breaks functionality of fieldPosition param. Also:
     * there's a bug in SimpleDateFormat with "S" and "SS", use "SSS"
     * instead if you want a msec field.
     */
    public StringBuffer format(Date date, StringBuffer toAppendTo,
			       FieldPosition fieldPosition) {
        long dt = date.getTime();
        long ds = dt / 1000;
        if (ds != lastSec) {
            sb.setLength(0);
            df.format(date, sb, fp);
            lastSec = ds;
        } else {
	    // munge current msec into existing string
            int ms = (int)(dt % 1000);
            int pos = fp.getEndIndex();
	    int begin = fp.getBeginIndex();
	    if (pos > 0) {
		if (pos > begin)
		    sb.setCharAt(--pos, Character.forDigit(ms % 10, 10));
		ms /= 10;
		if (pos > begin)
		    sb.setCharAt(--pos, Character.forDigit(ms % 10, 10));
		ms /= 10;
		if (pos > begin)
		    sb.setCharAt(--pos, Character.forDigit(ms % 10, 10));
	    }
        }
	toAppendTo.append(sb.toString());
	return toAppendTo;
    }

}
