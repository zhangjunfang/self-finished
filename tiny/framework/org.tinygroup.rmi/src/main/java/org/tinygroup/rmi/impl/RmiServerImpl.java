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
package org.tinygroup.rmi.impl;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.rmi.ConnectTrigger;
import org.tinygroup.rmi.RmiServer;

public final class RmiServerImpl extends UnicastRemoteObject implements
		RmiServer {

	private static final long serialVersionUID = -8847587819458611248L;

	private final static Logger logger = LoggerFactory
			.getLogger(RmiServerImpl.class);

	int port = DEFAULT_RMI_PORT;
	String hostName = "localhost";
	int remotePort = DEFAULT_RMI_PORT;
	String remoteHostName = "";

	// private ValidateThread validateThread = new ValidateThread();
	Registry registry = null;
	Registry remoteRegistry = null;
	RmiServer remoteServer = null;
	Map<String, Remote> registeredRemoteObjectMap = new HashMap<String, Remote>();
	Map<String, Remote> registeredLocalObjectMap = new HashMap<String, Remote>();
	HeartThread heartThread = new HeartThread();
	Map<String, List<ConnectTrigger>> triggers = new HashMap<String, List<ConnectTrigger>>();

	public RmiServerImpl() throws RemoteException {
		this("localhost", DEFAULT_RMI_PORT);
	}

	public RmiServerImpl(int port) throws RemoteException {
		this("localhost", port);
	}

	public RmiServerImpl(String hostName, int port) throws RemoteException {
		this(hostName, port, null, 0);
	}

	public RmiServerImpl(String hostName, int port, String remoteHostName,
			int remotePort) throws RemoteException {
		if (hostName != null && !"".equals(hostName)) {
			this.hostName = hostName;
		}
		this.port = port;
		this.remoteHostName = remoteHostName;
		this.remotePort = remotePort;
		
		try {
			getRemoteRegistry();
			bindThis();
			if (remoteRegistry != null) {
				startHeart();
			}

		} catch (RemoteException e) {
			logger.errorMessage("连接远端服务器时发生异常", e);
			startHeart();
		}
		getRegistry();

	}

	private void startHeart() {
		heartThread.start();
	}

	private void bindThis() throws RemoteException {
		if (remoteServer != null) {
			remoteServer.registerRemoteObject(this,
					getKeyName(hostName, port + ""));

		}
	}

	public Registry getRegistry() throws RemoteException {
		if (registry == null) {
			try {
				registry = LocateRegistry.getRegistry(hostName, port);
				registry.list();
			} catch (Exception e) {
				try {
					registry = LocateRegistry.createRegistry(port);
				} catch (RemoteException e1) {
					throw new RuntimeException(e1);
				}
			}
		}
		try {
			registry.rebind(getKeyName(hostName, port + ""), this);
		} catch (AccessException e) {
			throw new RuntimeException(e);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}

		return registry;
	}

	public Registry getRemoteRegistry() throws RemoteException {

		if (remoteHostName == null || "".equals(remoteHostName)) {
			return null;
		}
		System.setProperty("java.rmi.server.hostname", remoteHostName);
		remoteRegistry = LocateRegistry.getRegistry(remoteHostName, remotePort);
		try {
			remoteServer = (RmiServer) remoteRegistry.lookup(getKeyName(
					remoteHostName, remotePort + ""));
		} catch (NotBoundException e) {
			logger.errorMessage("获取远端服务器:" + remoteHostName + "时出现异常,该对象未曾注册", e);
			throw new RuntimeException("获取远端服务器:" + remoteHostName
					+ "时出现异常,该对象未曾注册", e);
		}
		return remoteRegistry;
	}

	public void stop() throws RemoteException {
		stopHeart();
		unexportObjects();
		stopLocalRegistry();
	}
	
	public void stopLocalRegistry()throws RemoteException{
		if(registry!=null){
			UnicastRemoteObject.unexportObject(this, true);
		}
	}

	private void stopHeart() {
		heartThread.stop();
	}

	public void addTrigger(ConnectTrigger trigger) throws RemoteException {
		String type = trigger.getType();
		if (triggers.containsKey(type)) {
			triggers.get(type).add(trigger);
		} else {
			List<ConnectTrigger> list = new ArrayList<ConnectTrigger>();
			list.add(trigger);
			triggers.put(type, list);
		}
	}

	class HeartThread extends Thread implements Serializable {
		private static final int MILLISECOND_PER_SECOND = 1000;
		private volatile boolean stop = false;
		private int breathInterval = 20;// 单位秒

		public void setStop(boolean stop) {
			this.stop = stop;
		}

		public void run() {
			while (!stop) {

				try {
					sleep(breathInterval * MILLISECOND_PER_SECOND);
				} catch (InterruptedException e) {
					continue;
				}
				logger.logMessage(LogLevel.DEBUG, "开始检测远端服务器的可用性");
				try {
					remoteRegistry.list();
					logger.logMessage(LogLevel.DEBUG, "远端服务器正常");
				} catch (Exception e) {
					logger.logMessage(LogLevel.DEBUG, "远端服务器不可用，开始尝试重新获取");
					try {
						getRemoteRegistry();
						logger.logMessage(LogLevel.DEBUG, "远端服务器尝试重新获取成功");
					} catch (Exception e2) {
						logger.logMessage(LogLevel.DEBUG, "远端服务器尝试重新获取失败");
						continue;
					}

				}
				if (!checkRemoteHasThis()) {
					logger.logMessage(LogLevel.DEBUG, "远端服务器上不存在本地服务器信息");
					reReg();
					List<ConnectTrigger> list = triggers
							.get(ConnectTrigger.REREG);
					for (ConnectTrigger trigger : list) {
						trigger.deal();
					}

				}
				logger.logMessage(LogLevel.DEBUG, "检测远端服务器的可用性完成");
			}
		}
	}

	private void reReg() {

		try {
			remoteServer = (RmiServer) remoteRegistry.lookup(getKeyName(
					remoteHostName, remotePort + ""));
		} catch (NotBoundException e) {
			logger.errorMessage("获取远端服务器:" + remoteHostName + "时出现异常,该对象未曾注册", e);
		} catch (RemoteException e1) {
			logger.errorMessage("连接远端服务器:" + remoteHostName + "失败", e1);
		}

		for (String name : registeredLocalObjectMap.keySet()) {
			try {
				remoteServer.registerRemoteObject(
						registeredLocalObjectMap.get(name), name);
			} catch (RemoteException e) {
				logger.errorMessage("向远端服务器重新注册对象name:{}时出现异常", e, name);
			}
		}
		logger.logMessage(LogLevel.DEBUG, "将本地对象重新注册至远端服务器完成");
		try {
			bindThis();
		} catch (RemoteException e) {
			logger.errorMessage("向远端服务器重新绑定当前服务器信息时出现异常", e);
		}
	}

	private boolean checkRemoteHasThis() {
		try {
			Object o = remoteServer.getObject(getKeyName(hostName, port + ""));
			if (o != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void registerLocalObject(Remote object, String name)
			throws RemoteException {
		try {
			logger.logMessage(LogLevel.DEBUG, "开始注册本地对象:{}", name);
			System.setProperty("java.rmi.server.hostname", hostName);
			registeredLocalObjectMap.put(name, object);
			if (object instanceof UnicastRemoteObject) {
				registry.rebind(name, object);
				if (remoteServer != null) {
					remoteServer.registerRemoteObject(object, name);
				}
			} else {
				Remote stub = UnicastRemoteObject.exportObject(object, 0);
				registry.rebind(name, stub);
				if (remoteServer != null) {
					remoteServer.registerRemoteObject(stub, name);
				}
			}
			

			logger.logMessage(LogLevel.DEBUG, "结束注册本地对象:{}", name);
		} catch (RemoteException e) {
			logger.errorMessage("注册本地对象:{}时发生异常:{}！", e, name, e.getMessage());
			registeredLocalObjectMap.remove(name);
			throw new RuntimeException(e);
		}
	}

	public void registerLocalObject(Remote object, Class type, String id)
			throws RemoteException {
		registerLocalObject(object, type.getName(), id);
	}

	public void registerLocalObject(Remote object, String type, String id)
			throws RemoteException {
		registerLocalObject(object, getKeyName(type, id));
	}

	public void registerLocalObject(Remote object, Class type)
			throws RemoteException {
		registerLocalObject(object, type.getName());
	}

	public void registerRemoteObject(Remote object, Class type, String id)
			throws RemoteException {
		registerRemoteObject(object, getKeyName(type.getName(), id));
	}

	public void registerRemoteObject(Remote object, String type, String id)
			throws RemoteException {
		registerRemoteObject(object, getKeyName(type, id));
	}

	public void registerRemoteObject(Remote object, String name)
			throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始注册远程对象:{}", name);
		registeredRemoteObjectMap.put(name, object);
		logger.logMessage(LogLevel.DEBUG, "注册远程对象:{}结束", name);
	}

	public void registerRemoteObject(Remote object, Class type)
			throws RemoteException {
		registerRemoteObject(object, type.getName());
	}

	public void unregisterObject(Remote object) throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始注销对象object:{}", object);
		boolean flag = false;
		for (String name : registeredLocalObjectMap.keySet()) {
			Remote r = registeredLocalObjectMap.get(name);
			if (r.equals(object)) {
				unregisterLocalObject(name);
				flag = true;
				break;
			}
		}
		if (!flag) {
			for (String name : registeredRemoteObjectMap.keySet()) {
				Remote r = registeredRemoteObjectMap.get(name);
				if (r.equals(object)) {
					unregisterRemoteObject(name);
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			logger.logMessage(LogLevel.ERROR, "需要注销的对象object:{}不存在", object);
		}
		logger.logMessage(LogLevel.DEBUG, "注销对象object:{}完成", object);
	}

	private void unregisterLocalObject(String name) throws RemoteException {
		try {
			logger.logMessage(LogLevel.DEBUG, "开始注销本地对象:{}", name);
			registry.unbind(name);
			if (registeredLocalObjectMap.get(name) != null) {
				UnicastRemoteObject.unexportObject(
						registeredLocalObjectMap.get(name), true);
			}
			registeredLocalObjectMap.remove(name);
			if (remoteServer != null) {
				remoteServer.unregisterObject(name);
			}
			logger.logMessage(LogLevel.DEBUG, "注销本地对象:{}完成", name);
		} catch (Exception e) {
			logger.errorMessage("注销对象:{}时发生异常:{}！", e, name, e.getMessage());
		}
	}

	private void unregisterRemoteObject(String name) throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始注销远程对象:{}", name);
		registeredRemoteObjectMap.remove(name);
		logger.logMessage(LogLevel.DEBUG, "注销远程对象:{}完成", name);
	}

	public void unregisterObject(String name) throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始注销对象:{}", name);
		if (registeredLocalObjectMap.containsKey(name)) {
			unregisterLocalObject(name);
		} else if (registeredRemoteObjectMap.containsKey(name)) {
			unregisterRemoteObject(name);
		} else {
			logger.logMessage(LogLevel.ERROR, "需要注销的对象name:{}不存在", name);
		}
		logger.logMessage(LogLevel.DEBUG, "结束注销对象:{}", name);
	}

	public void unregisterObjectByType(Class type) throws RemoteException {
		logger.logMessage(LogLevel.DEBUG, "开始注销对象type:{}", type.getName());
		unregisterLocalObjectByType(type);
		unregisterRemoteObjecttByType(type);
		logger.logMessage(LogLevel.DEBUG, "注销对象type:{}完成", type.getName());
	}

	private void unregisterLocalObjectByType(Class type) throws RemoteException {
		String typeName = type.getName();
		for (String name : registeredLocalObjectMap.keySet()) {
			if (name.startsWith(typeName + "|")) { // 如果名称是以typeName打头
				unregisterLocalObject(name);
			} else {
				Object obj = registeredLocalObjectMap.get(name); // 如果名称不匹配再进行类型判断
				if (type.isAssignableFrom(obj.getClass())) {
					unregisterLocalObject(name);
				}
			}
		}
	}

	private void unregisterRemoteObjecttByType(Class type)
			throws RemoteException {
		String typeName = type.getName();
		for (String name : registeredRemoteObjectMap.keySet()) {
			if (name.startsWith(typeName + "|")) { // 如果名称是以typeName打头
				unregisterRemoteObject(name);
			} else {
				Object obj = registeredRemoteObjectMap.get(name); // 如果名称不匹配再进行类型判断
				if (type.isInstance(obj)) {// type.isAssignableFrom(obj.getClass())
					unregisterRemoteObject(name);
				}
			}

		}
	}

	public void unregisterObjectByType(String type) throws RemoteException {
		try {
			unregisterObjectByType(Class.forName(type));
		} catch (ClassNotFoundException e) {
			logger.errorMessage("注销类型为:{}的对象时发生异常:{}!", e, type, e.getMessage());
		}

	}

	public void unregisterObject(String type, String id) throws RemoteException {
		unregisterObject(getKeyName(type, id));
	}

	public void unregisterObject(Class type, String id) throws RemoteException {
		unregisterObject(getKeyName(type.getName(), id));
	}

	public <T> T getObject(String name) throws RemoteException {

		if (registeredLocalObjectMap.containsKey(name)) {
			return (T) registeredLocalObjectMap.get(name);
		}
		if (registeredRemoteObjectMap.containsKey(name)) {
			return (T) registeredRemoteObjectMap.get(name);
		}
		if (remoteServer != null) {
			return (T) remoteServer.getObject(name);
		}
		return null;
	}

	public <T> T getObject(Class<T> type) throws RemoteException {
		for (String sName : registeredRemoteObjectMap.keySet()) {
			try {
				Remote object = getObject(sName);
				if (type.isInstance(object)) {
					return (T) object;
				}
			} catch (RemoteException e) {
				logger.errorMessage("获取对象Name:{}时出现异常", e, sName);
			}
		}
		for (String sName : registeredLocalObjectMap.keySet()) {
			try {
				Remote object = getObject(sName);
				if (type.isInstance(object)) {
					return (T) object;
				}
			} catch (RemoteException e) {
				logger.errorMessage("获取对象Name:{}时出现异常", e, sName);
			}
		}
		if (remoteServer != null) {
			return remoteServer.getObject(type);
		}
		return null;
	}

	private <T> void getObjectListInstanceOf(Class<T> type, List<T> result,
			Map<String, Remote> map) throws RemoteException {
		for (String sName : map.keySet()) {
			try {
				Remote object = getObject(sName);
				if (type.isInstance(object) && !result.contains(object)) {
					result.add((T) object);
				}
			} catch (RemoteException e) {
				logger.errorMessage("获取对象Name:{}时出现异常", e, sName);
			}
		}
	}

	public <T> List<T> getObjectList(Class<T> type) throws RemoteException {
		return getObjectList(type.getName());
	}

	private <T> void getObjectList(String typeName, List<T> result,
			Map<String, Remote> map) throws RemoteException {
		for (String sName : map.keySet()) {
			Object o = map.get(sName);
			if (result.contains(o)) {
				continue;
			}
			if (sName.startsWith(typeName + "|")) {
				result.add((T) o);
			} else if (o.getClass().toString().equals(typeName)) {
				result.add((T) o);
			}
		}
	}

	public <T> List<T> getObjectList(String typeName) throws RemoteException {
		List<T> result = new ArrayList<T>();
		getObjectList(typeName, result, registeredLocalObjectMap);
		getObjectList(typeName, result, registeredRemoteObjectMap);
		if (remoteServer != null) {
			List<T> list = remoteServer.getObjectList(typeName);
			for (Object t : list) {
				if (!result.contains(t)) {
					result.add((T) t);
				}
			}
		}
		return result;
	}

	public <T> List<T> getRemoteObjectListInstanceOf(Class<T> type)
			throws RemoteException {
		List<T> result = new ArrayList<T>();
		getObjectListInstanceOf(type, result, registeredLocalObjectMap);
		getObjectListInstanceOf(type, result, registeredRemoteObjectMap);
		if (remoteServer != null) {
			List<T> list = remoteServer.getRemoteObjectListInstanceOf(type);
			for (T t : list) {
				if (!result.contains(t)) {
					result.add(t);
				}
			}
		}
		return result;
	}

	public void unexportObjects() throws RemoteException {
		List<String> names = new ArrayList<String>();
		for (String name : registeredLocalObjectMap.keySet()) {
			names.add(name);
		}
		for (String name : names) {
			try {
				unregisterLocalObject(name);
			} catch (Exception e) {
				logger.errorMessage("注销对象name:{}时失败", e, name);
			}
		}
		names.clear();

		for (String name : registeredRemoteObjectMap.keySet()) {
			names.add(name);
		}
		for (String name : names) {
			try {
				unregisterRemoteObject(name);
			} catch (Exception e) {
				logger.errorMessage("注销对象name:{}时失败", e, name);
			}
		}
	}

	private String getKeyName(String name, String id) throws RemoteException {
		return RmiUtil.getName(name, id);
	}

}
