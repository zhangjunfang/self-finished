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
package com.sun.xml.ws.transport.http.servlet;

import javax.servlet.ServletContext;

import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.HttpAdapterList;
import com.sun.xml.ws.transport.http.TG_DeploymentDescriptorParser.AdapterFactory;

/**
 * List (and a factory) of {@link ServletAdapter}.
 *
 * @author Jitendra Kotamraju
 */
public class TG_ServletAdapterList extends HttpAdapterList<TG_ServletAdapter> implements AdapterFactory<TG_ServletAdapter> {
    private final ServletContext context;

    /**
     * Keeping it for GlassFishv2 compatibility. Move to
     * {@link #ServletAdapterList(ServletContext) }
     *
     * @deprecated
     */
    @Deprecated
    public TG_ServletAdapterList() {
        context = null;
    }

    public TG_ServletAdapterList(ServletContext ctxt) {
        this.context = ctxt;
    }

    /* package */ ServletContext getServletContext() {
        return context;
    }
    
    
    protected TG_ServletAdapter createHttpAdapter(String name, String urlPattern, WSEndpoint<?> endpoint) {
        return new TG_ServletAdapter(name, urlPattern, endpoint, this);
    }
}