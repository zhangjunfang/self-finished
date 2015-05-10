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

import java.util.AbstractList;


public class RangeList extends AbstractList<Integer> {
    private final int start;
    private final int step;
    private final int size;

    public RangeList(int start, int stop, int step) {
        this.start = start;
        this.step = step;

        size = (stop - start) / step + 1;
    }

    public RangeList(Object start, Object stop) {
        Number startNumber= (Number) start;
        Number endNumber= (Number) stop;
        this.start = startNumber.intValue();
        this.step = startNumber.intValue() < endNumber.intValue() ? 1 : -1;

        size = (endNumber.intValue() - startNumber.intValue()) / step + 1;
    }


    public Integer get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return start + index * step;
    }

    public int size() {
        return size;
    }
}