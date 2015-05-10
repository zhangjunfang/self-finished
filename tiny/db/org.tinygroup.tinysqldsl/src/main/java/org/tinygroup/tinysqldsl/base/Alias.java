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
 * 别名
 * @author renhui
 */
public class Alias {
    /**
     * 别名
     */
    private String name;
    /**
     * 是否带as关键字
     */
    private boolean withAs = false;

    public Alias() {

    }

    public Alias(String name) {
        this.name = name;
    }

    public Alias(String name, boolean withAs) {
        this.name = name;
        this.withAs = withAs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWithAs() {
        return withAs;
    }

    public void setWithAs(boolean withAs) {
        this.withAs = withAs;
    }


    public String toString() {
        return (withAs ? " AS " : " ") + name;
    }
}
