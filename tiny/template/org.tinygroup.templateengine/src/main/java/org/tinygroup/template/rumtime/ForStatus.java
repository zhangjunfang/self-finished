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

/**
 * #for 内部状态指示器.
 *
 * @author Guoqiang Chen
 */
public interface ForStatus {

    /**
     * foreach 计数器，从 1 开始
     */
     int getIndex();

    /**
     * 获取循环总数.
     *
     * <p>如果对 Iterator 进行循环，或者对非 Collection 的 Iterable 进行循环，则返回 -1。<p>
     *
     * @since 1.1.3
     */
     int getSize();

    /**
     * 是否第一个元素.
     *
     * @since 1.1.3
     */
     boolean isFirst();

    /**
     * 是否最后一个元素.
     *
     * @since 1.1.3
     */
     boolean isLast();

    /**
     * 是否第奇数个元素.
     *
     * @since 1.2.0
     */
     boolean isOdd();

    /**
     * 是否第偶数个元素.
     *
     * @since 1.2.0
     */
     boolean isEven();
}
