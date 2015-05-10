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

/**
 * 功能说明: zip协议
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-29 <br>
 * <br>
 */
public class ZipSchemaProvider extends AbstractSchemaProvider{

    public static final String ZIP = ".zip";
    public static final String ZIP_PROTOCAL = "zip:";

    public boolean isMatch(String resource) {
        String lowerCase = resource.toLowerCase();
        //zip包比较特殊：无法简单根据resource判断，存在D:/lib/a.zip这种用法，和扩展协议oss://file1/a.zip无法从语法上区分。
        return lowerCase.startsWith(ZIP_PROTOCAL) || (lowerCase.endsWith(ZIP) && new File(resource).exists());
    }

    public String getSchema() {
        return ZIP_PROTOCAL;
    }

    public FileObject resolver(String resourceResolve) {
        String resource = resourceResolve;
        if (resource.startsWith(ZIP_PROTOCAL)) {
            resource = resource.substring(ZIP_PROTOCAL.length());
        }
        if (resource.startsWith(FileSchemaProvider.FILE_PROTOCOL)) {
            resource = resource.substring(FileSchemaProvider.FILE_PROTOCOL.length());
        }
        return new ZipFileObject(this, getResourceResolve(resourceResolve,ZIP_PROTOCAL));
    }

}
