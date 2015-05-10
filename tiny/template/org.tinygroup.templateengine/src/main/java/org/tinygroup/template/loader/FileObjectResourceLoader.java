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
package org.tinygroup.template.loader;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateException;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * Created by luoguo on 2014/6/9.
 */
public class FileObjectResourceLoader extends AbstractResourceLoader<FileObject> {
    private FileObject root = null;
    
    private Map<String, FileObject> caches=new HashMap<String, FileObject>();

    public FileObject getRootFileObject() {
        return root;
    }

    public FileObjectResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName, String root) {
        this(templateExtName, layoutExtName, macroLibraryExtName, VFS.resolveFile(root));
    }

    public FileObjectResourceLoader(String templateExtName, String layoutExtName, String macroLibraryExtName, FileObject root) {
        super(templateExtName, layoutExtName, macroLibraryExtName);
        this.root = root;
        setCheckModified(true);
    }

    public Template createTemplate(FileObject fileObject) throws TemplateException {
        if (fileObject != null) {
        	caches.put(fileObject.getPath(), fileObject);//缓存起来
            return loadTemplate(fileObject, getClassLoader());
        }
        return null;
    }

    protected Template loadTemplateItem(final String path) throws TemplateException {
        return createTemplate(root.getFileObject(path));
    }


    public boolean isModified(String path) {
    	FileObject fileObject=caches.get(path);
    	if(fileObject==null){
    		 fileObject = getFileObject(path);
    	}
        if (fileObject == null) {
            return true;//还是null,则认为是新增的，返回true
        }
        return fileObject.isModified();
    }

    public void resetModified(String path) {
        FileObject fileObject = getFileObject(path);
        if (fileObject != null) {
            fileObject.resetModified();
        }
    }

    private FileObject getFileObject(String path) {
        return root.getFileObject(path);
    }

    public String getResourceContent(String path, String encode) throws TemplateException {
        FileObject fileObject = root.getFileObject(path);
        if (fileObject != null) {
            try {
                return IOUtils.readFromInputStream(fileObject.getInputStream(), encode);
            } catch (Exception e) {
                throw new TemplateException(e);
            }
        }
        return null;
    }

    private Template loadTemplate(FileObject fileObject, ClassLoader classLoader) throws TemplateException {
        try {
            Template template = ResourceCompilerUtils.compileResource(classLoader, IOUtils.readFromInputStream(fileObject.getInputStream(), getTemplateEngine().getEncode()),getTemplateEngine().getEngineId(), fileObject.getPath());
            addTemplate(template);
            return template;
        } catch (Exception e) {
            throw new TemplateException(e);
        }
    }

}
