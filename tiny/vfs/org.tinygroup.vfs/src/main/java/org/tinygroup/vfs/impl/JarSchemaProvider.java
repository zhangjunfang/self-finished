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
package org.tinygroup.vfs.impl;

import java.io.File;

import org.tinygroup.vfs.FileObject;

public class JarSchemaProvider extends AbstractSchemaProvider {

    public static final String JAR = ".jar";
    public static final String JAR_PROTOCOL = "jar:";

    public FileObject resolver(String resourceResolve) {
         return new JarFileObject(this,getResourceResolve(resourceResolve,JAR_PROTOCOL));
    }

    public boolean isMatch(String resource) {
        String lowerCase = resource.toLowerCase();
        //jar包比较特殊：无法简单根据resource判断，存在D:/lib/a.jar这种用法，和扩展协议oss://file1/a.jar无法从语法上区分。
        return lowerCase.startsWith(JAR_PROTOCOL) || (lowerCase.endsWith(JAR) && new File(resource).exists());
    }

    public String getSchema() {
        return JAR_PROTOCOL;
    }

}
