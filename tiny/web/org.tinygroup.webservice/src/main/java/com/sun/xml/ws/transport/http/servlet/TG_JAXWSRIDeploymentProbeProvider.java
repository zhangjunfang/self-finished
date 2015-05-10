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

import org.glassfish.external.probe.provider.annotations.ProbeProvider;
import org.glassfish.external.probe.provider.annotations.Probe;
import org.glassfish.external.probe.provider.annotations.ProbeParam;

/**
 * sun-jaxws.xml deployment probe. A registered listener get to listen the emited
 * sun-jaxws.xml deployment/undepolyment events.
 *
 * @author Jitendra Kotamraju
 */
@ProbeProvider(moduleProviderName="glassfish", moduleName="webservices", probeProviderName="deployment-ri")
public class TG_JAXWSRIDeploymentProbeProvider {

    @Probe(name="deploy", hidden=true)
    public void deploy(@ProbeParam("adapter")TG_ServletAdapter adpater) {
        // intentionally left empty.
    }

    @Probe(name="undeploy", hidden=true)
    public void undeploy(@ProbeParam("adapter")TG_ServletAdapter adapter) {
        // intentionally left empty.
    }
    
}