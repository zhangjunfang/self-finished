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
package org.tinygroup.database.config.sequence;

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("sequence")
public class Sequence extends BaseObject {
	
	@XStreamAsAttribute
	@XStreamAlias("data-type")
	private String dataType;

	@XStreamAlias("increment-by")
	@XStreamAsAttribute
	private int incrementBy;

	@XStreamAlias("start-with")
	@XStreamAsAttribute
	private int startWith;
	@XStreamAlias("value-config")
	private ValueConfig valueConfig;
	@XStreamAsAttribute
	private boolean cycle;
	@XStreamAlias("seq-cache-config")
	private SeqCacheConfig seqCacheConfig;
	@XStreamAsAttribute
	private boolean order;

	public int getIncrementBy() {
		return incrementBy;
	}

	public void setIncrementBy(int incrementBy) {
		this.incrementBy = incrementBy;
	}

	public int getStartWith() {
		return startWith;
	}

	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}

	public ValueConfig getValueConfig() {
		return valueConfig;
	}

	public void setValueConfig(ValueConfig valueConfig) {
		this.valueConfig = valueConfig;
	}

	public boolean isCycle() {
		return cycle;
	}

	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}

	public SeqCacheConfig getSeqCacheConfig() {
		return seqCacheConfig;
	}

	public void setSeqCacheConfig(SeqCacheConfig seqCacheConfig) {
		this.seqCacheConfig = seqCacheConfig;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public boolean isOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

}
