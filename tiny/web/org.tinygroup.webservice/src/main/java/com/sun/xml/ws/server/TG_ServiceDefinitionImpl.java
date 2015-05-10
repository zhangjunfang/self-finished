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
package com.sun.xml.ws.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.server.SDDocument;
import com.sun.xml.ws.api.server.SDDocumentFilter;
import com.sun.xml.ws.api.server.ServiceDefinition;

/**
 * {@link ServiceDefinition} implementation.
 *
 * <p>
 * You construct a {@link TG_ServiceDefinitionImpl} by first constructing
 * a list of {@link SDDocumentImpl}s.
 *
 * @author Kohsuke Kawaguchi
 */
public final class TG_ServiceDefinitionImpl implements ServiceDefinition {
    private final List<TG_SDDocumentImpl> docs;

    private final Map<String,TG_SDDocumentImpl> bySystemId;
    private final @NotNull TG_SDDocumentImpl primaryWsdl;

    /**
     * Set when {@link WSEndpointImpl} is created.
     */
    /*package*/ TG_WSEndpointImpl<?> owner;

    /*package*/ final List<SDDocumentFilter> filters = new ArrayList<SDDocumentFilter>();

    /**
     * @param docs
     *      List of {@link SDDocumentImpl}s to form the description.
     *      There must be at least one entry.
     *      The first document is considered {@link #getPrimary() primary}.
     */
    public TG_ServiceDefinitionImpl(List<TG_SDDocumentImpl> docs, @NotNull TG_SDDocumentImpl primaryWsdl) {
        assert docs.contains(primaryWsdl);
        this.docs = docs;
        this.primaryWsdl = primaryWsdl;

        this.bySystemId = new HashMap<String, TG_SDDocumentImpl>(docs.size());
        for (TG_SDDocumentImpl doc : docs) {
            bySystemId.put(doc.getURL().toExternalForm(),doc);

            assert doc.owner==null;
            doc.owner = this;
        }
    }

    /**
     * The owner is set when {@link WSEndpointImpl} is created.
     */
    /*package*/ void setOwner(TG_WSEndpointImpl<?> owner) {
        assert owner!=null && this.owner==null;
        this.owner = owner;
    }

    public @NotNull SDDocument getPrimary() {
        return primaryWsdl;
    }

    public void addFilter(SDDocumentFilter filter) {
        filters.add(filter);
    }

    public Iterator<SDDocument> iterator() {
        return (Iterator)docs.iterator();
    }

    /**
     * @see #getBySystemId(String)
     */
    public SDDocument getBySystemId(URL systemId) {
        return getBySystemId(systemId.toString());
    }

    /**
     * Gets the {@link SDDocumentImpl} whose {@link SDDocumentImpl#getURL()}
     * returns the specified value.
     *
     * @return
     *      null if none is found.
     */
    public TG_SDDocumentImpl getBySystemId(String systemId) {
        return bySystemId.get(systemId);
    }
}
