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
package org.tinygroup.cepcorenettysc;

import org.jboss.netty.channel.ChannelHandler;
import org.tinygroup.net.Server;
import org.tinygroup.net.ServerHandler;
import org.tinygroup.net.coder.hessian.HessianDecoder;
import org.tinygroup.net.coder.hessian.HessianEncoder;

public class EventServer extends Server {

	public EventServer(int port) {
		super(port);
	}
	
	
	public void stop(){
		//停止自己的监听端口及该端口来自外部的连接
		super.stop();
		//停止自己对外的连接
		//对于sc来说，此处是sc向as ar发起的连接 用于发布注册时的数据
		//对于as ar来说，此处是空
		((EventServerHandler)getHandler()).stop();
	}

	public ChannelHandler getHandler() {
		ServerHandler serverHandler = new EventServerHandler();
		serverHandler.setServer(this);
		return serverHandler;
	}

	public ChannelHandler getEncoder() {
		// return new ObjectEncoder();
		 return new HessianEncoder();
//		return new KryoEncoder();
	}

	public ChannelHandler getDecoder() {
		// return new ObjectDecoder(ClassResolvers.cacheDisabled(null));
		// return new KryoDecoder();
		return new HessianDecoder();
	}

}
