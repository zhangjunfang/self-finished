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
package com.sun.xml.ws.server.sei;

import javax.xml.namespace.QName;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.client.sei.MethodHandler;
import com.sun.xml.ws.model.AbstractSEIModelImpl;
import com.sun.xml.ws.model.JavaMethodImpl;
import com.sun.xml.ws.server.TG_WSEndpointImpl;
import com.sun.xml.ws.util.QNameMap;
import com.sun.xml.ws.wsdl.DispatchException;

/**
 * This pipe is used to invoke SEI based endpoints.
 *
 * @author Jitendra Kotamraju
 */
public class TG_SEIInvokerTube extends SEIInvokerTube {

    /**
     * For each method on the port interface we have
     * a {@link MethodHandler} that processes it.
     */
    private final WSBinding binding;
    private final AbstractSEIModelImpl model;

    //store WSDL Operation to EndpointMethodHandler map
    private final QNameMap<TG_EndpointMethodHandler> wsdlOpMap;
    public TG_SEIInvokerTube(AbstractSEIModelImpl model,Invoker invoker, WSBinding binding) {
        super(model, invoker, binding);
        this.binding = binding;
        this.model = model;
        wsdlOpMap = new QNameMap<TG_EndpointMethodHandler>();
        for(JavaMethodImpl jm: model.getJavaMethods()) {
            wsdlOpMap.put(jm.getOperation().getName(),new TG_EndpointMethodHandler(this,jm,binding));
        }
    }

    /**
     * This binds the parameters for SEI endpoints and invokes the endpoint method. The
     * return value, and response Holder arguments are used to create a new {@link Message}
     * that traverses through the Pipeline to transport.
     */
    public @NotNull NextAction processRequest(@NotNull Packet req) {
        QName wsdlOp;
        try {
            wsdlOp = ((TG_WSEndpointImpl) getEndpoint()).getOperationDispatcher().getWSDLOperationQName(req);
            Packet res = wsdlOpMap.get(wsdlOp).invoke(req);
            assert res != null;
            return doReturnWith(res);
        } catch (DispatchException e) {
            return doReturnWith(req.createServerResponse(e.fault, model.getPort(), null, binding));
        }
    }

    public @NotNull NextAction processResponse(@NotNull Packet response) {
        throw new IllegalStateException("InovkerPipe's processResponse shouldn't be called.");
    }

    public @NotNull NextAction processException(@NotNull Throwable t) {
        throw new IllegalStateException("InovkerPipe's processException shouldn't be called.");
    }

}
