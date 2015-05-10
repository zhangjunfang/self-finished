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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.istack.Nullable;

/**
 * The JAX-WS dispatcher servlet.
 *
 * <p>
 * It really just forwards processing to {@link TG_WSServletDelegate}.
 *
 * @author WS Development Team
 */
public class TG_WSServlet extends HttpServlet {
    private TG_WSServletDelegate delegate = null;
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        delegate = getDelegate(servletConfig);
       
    }

    /**
     * Gets the {@link TG_WSServletDelegate} that we will be forwarding the requests to.
     *
     * @return
     *      null if the deployment have failed and we don't have the delegate.
     */
    protected @Nullable TG_WSServletDelegate getDelegate(ServletConfig servletConfig) {
        return (TG_WSServletDelegate) servletConfig.getServletContext().getAttribute(JAXWS_RI_RUNTIME_INFO);
    }

    
    protected void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException {
    	if (delegate != null) {
            delegate.doPost(request,response,getServletContext());
        }
    }

    
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
    	if (delegate != null) {
            delegate.doGet(request,response,getServletContext());
        }
    }

    
    protected void doPut( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (delegate != null) {
            delegate.doPut(request,response,getServletContext());
        }
    }

    
    protected void doDelete( HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        if (delegate != null) {
            delegate.doDelete(request,response,getServletContext());
        }
    }

    
    protected void doHead( HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (delegate != null) {
            delegate.doHead(request,response,getServletContext());
        }
    }

    /**
     * {@link TG_WSServletDelegate}.
     */
    public static final String JAXWS_RI_RUNTIME_INFO =
        "com.sun.xml.ws.server.http.servletDelegate";
    public static final String JAXWS_RI_PROPERTY_PUBLISH_WSDL =
        "com.sun.xml.ws.server.http.publishWSDL";
    public static final String JAXWS_RI_PROPERTY_PUBLISH_STATUS_PAGE =
        "com.sun.xml.ws.server.http.publishStatusPage";

}
