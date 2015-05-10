package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.SchemaProvider;

/**
 * Created by luoguo on 2014/11/9.
 */
public abstract class AbstractSchemaProvider implements SchemaProvider {
	
    public String getResourceResolve(String resourceResolve, String protocal) {
        String resource = resourceResolve;
        if (resource.startsWith(protocal)) {
            resource = resource.substring(protocal.length());
        }
        if (resource.startsWith(FileSchemaProvider.FILE_PROTOCOL)) {
            resource = resource.substring(FileSchemaProvider.FILE_PROTOCOL.length());
        }
        return resource;
    }
}
