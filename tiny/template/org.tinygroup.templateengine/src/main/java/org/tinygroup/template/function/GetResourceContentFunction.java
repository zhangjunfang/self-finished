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
package org.tinygroup.template.function;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

/**
 * Created by luoguo on 2014/6/9.
 */
public class GetResourceContentFunction extends AbstractTemplateFunction{

    public GetResourceContentFunction() {
        super("read,readContent");
    }




    public Object execute(Template template,TemplateContext context,Object... parameters) throws TemplateException {
        String encode=super.getTemplateEngine().getEncode();
        String path=null;
        if(parameters.length==0||!(parameters[0] instanceof String)){
            notSupported(parameters);
        }else{
            path= (String) parameters[0];
        }
        if(parameters.length>2){
            encode=parameters[1].toString();
        }

        return getTemplateEngine().getResourceContent(path,encode);
    }

}

