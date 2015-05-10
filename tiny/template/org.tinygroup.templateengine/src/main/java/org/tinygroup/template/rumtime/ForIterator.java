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

import org.tinygroup.commons.tools.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;


public final class ForIterator implements Iterator, ForStatus {
    private Iterator iterator = null;
    private int index = 0;
    private int size = 0;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ForIterator() {
    }

    public ForIterator(Object object) {
        if (null == object) {
        } else if (object instanceof Map) {
            Map map = (Map) object;
            iterator = map.entrySet().iterator();
            size = map.size();
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            iterator = collection.iterator();
            size = collection.size();
        } else if (object instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) object;
            ArrayList<?> list = Collections.list(enumeration);
            iterator = list.iterator();
            size = list.size();
        } else if (object.getClass().isEnum()) {
            List<?> itemList = Arrays.asList(((Class<?>) object).getEnumConstants());
            iterator = itemList.iterator();
            size = itemList.size();
        } else if (object.getClass().isArray()) {
            iterator = new ArrayIterator(object);
            size = ArrayUtil.arrayLength(object);
        } else {
            iterator = new SingletonIterator(object);
            size = 1;
        }
    }

    public boolean hasNext() {
        if (iterator == null) {
            return false;
        }
        return iterator.hasNext();
    }

    public Object next() {
        index++;
        return iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int getIndex() {
        return index;
    }


    public int getSize() {
        return size;
    }


    public boolean isFirst() {
        return index == 1;
    }


    public boolean isLast() {
        return !iterator.hasNext();
    }


    public boolean isOdd() {
        return (index & 2) != 0;
    }


    public boolean isEven() {
        return (index % 2) == 0;
    }


    /**
     * Created by luoguo on 2014/6/5.
     */
    static class SingletonIterator implements Iterator {
        private Object object;

        public SingletonIterator(Object object) {
            this.object = object;
        }

        public boolean hasNext() {
            return object != null;
        }

        public Object next()  {
            Object result = object;
            object = null;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static class ArrayIterator implements Iterator {
        private final Object object;
        private int size = 0;
        private int index = 0;

        public ArrayIterator(Object object) {
            this.object = object;
            size = ArrayUtil.arrayLength(object);
        }

        public boolean hasNext() {
            return index < size;
        }

        public Object next()  {
            return Array.get(object, index++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
