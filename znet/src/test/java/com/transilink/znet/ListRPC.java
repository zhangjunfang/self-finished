/**
 * 
 */
package com.transilink.znet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ocean
 *
 */
public class ListRPC {

	public static void main(String[] args) throws Exception {
		/**
		 * 实现实现restful 风格路由机制
		 * 
		 * 路由包含：命名空间，所在的组以及版本号
		 * 
		 * */  
		List<Rs> list = new ArrayList<Rs>(20);
		MyRemotingServer myRemotingServer=new MyRemotingServer(80);
		Route  route=new Route();
		route.setGroup(new Group("group", Calendar.getInstance()));
		route.setNameSpace(new NameSpace("nameSpace", Calendar.getInstance()));
		route.setVersion(new Version("1.1",Calendar.getInstance()));
		myRemotingServer.setRoute(route);
		list.add(myRemotingServer);
		MyRemotingServer2 myRemotingServer2=new MyRemotingServer2(8080);
		Route  route2=new Route();
		route2.setGroup(new Group("group2", Calendar.getInstance()));
		route2.setNameSpace(new NameSpace("nameSpace2", Calendar.getInstance()));
		route2.setVersion(new Version("1.1.2",Calendar.getInstance()));
		myRemotingServer2.setRoute(route2);
		list.add(myRemotingServer2);
		for (Rs remotingServer : list) {
			remotingServer.testxxx();
			remotingServer.start();
		}
	}

}
