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

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Module;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WebModule;
import com.sun.xml.ws.transport.http.HttpAdapter;
import com.sun.xml.ws.transport.http.WSHTTPConnection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * {@link HttpAdapter} for servlets.
 *
 * <p>
 * This is a thin wrapper around {@link HttpAdapter} with some description
 * specified in the deployment (in particular those information are related
 * to how a request is routed to a {@link TG_ServletAdapter}.
 *
 * <p>
 * This class implements {@link BoundEndpoint} and represent the
 * servlet-{@link WSEndpoint} association for {@link }
 *
 */
public final class TG_ServletAdapter extends HttpAdapter implements BoundEndpoint {
    final String name;

    protected TG_ServletAdapter(String name, String urlPattern, WSEndpoint endpoint, TG_ServletAdapterList owner) {
        super(endpoint, owner, urlPattern);
        this.name = name;
        // registers itself with the container
        Module module = endpoint.getContainer().getSPI(Module.class);
        if(module==null)
            LOGGER.warning("Container "+endpoint.getContainer()+" doesn't support "+Module.class);
        else {
            module.getBoundEndpoints().add(this);
        }

    }

    public ServletContext getServletContext() {
        return ((TG_ServletAdapterList)owner).getServletContext();
    }

    /**
     * Gets the name of the endpoint as given in the <tt>sun-jaxws.xml</tt>
     * deployment descriptor.
     */
    public String getName() {
        return name;
    }


    @NotNull
    public URI getAddress() {
        WebModule webModule = endpoint.getContainer().getSPI(WebModule.class);
        if(webModule==null)
            // this is really a bug in the container implementation
            throw new WebServiceException("Container "+endpoint.getContainer()+" doesn't support "+WebModule.class);

        return getAddress(webModule.getContextPath());
    }

    public @NotNull URI getAddress(String baseAddress) {
        String adrs = baseAddress+getValidPath();
        try {
            return new URI(adrs);
        } catch (URISyntaxException e) {
            // this is really a bug in the container implementation
            throw new WebServiceException("Unable to compute address for "+endpoint,e);
        }
    }

    /**
     * Convenient method to return a port name from {@link WSEndpoint}.
     *
     * @return
     *      null if {@link WSEndpoint} isn't tied to any paritcular port.
     */
    public QName getPortName() {
        WSDLPort port = getEndpoint().getPort();
        if(port==null)  return null;
        else            return port.getName();
    }

    /**
     * Version of {@link #handle(WSHTTPConnection)}
     * that takes convenient parameters for servlet.
     *
     * @param context Servlet Context
     * @param request Servlet Request
     * @param response Servlet Response
     * @throws IOException when there is i/o error in handling request
     */
    public void handle(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WSHTTPConnection connection = new ServletConnectionImpl(this,context,request,response);
        super.handle(connection);
    }

    /**
     * @param context Servlet Context
     * @param request Servlet Request
     * @param response Servlet Response
     * @throws IOException when there is i/o error in handling request
     *
     * @deprecated
     *      Use {@link #handle(ServletContext, HttpServletRequest, HttpServletResponse)}
     */
    public void publishWSDL(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WSHTTPConnection connection = new ServletConnectionImpl(this,context,request,response);
        super.handle(connection);
    }

    public String toString() {
        return super.toString()+"[name="+name+']';
    }

    private static final Logger LOGGER = Logger.getLogger(TG_ServletAdapter.class.getName());
}
