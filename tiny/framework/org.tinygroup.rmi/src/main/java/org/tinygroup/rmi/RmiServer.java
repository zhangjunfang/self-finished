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
package org.tinygroup.rmi;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * RMI服务器接口
 * Created by luoguo on 14-1-10.
 */
public interface RmiServer extends Remote,Serializable{
    int DEFAULT_RMI_PORT = 8828;
    void addTrigger(ConnectTrigger trigger)throws RemoteException;
    /**
     * 返回本地对象注册表
     *
     * @return
     */
    Registry getRegistry()   throws RemoteException;

    /**
     * 注册本地对象，类型及ID
     *
     * @param type   注册的对象类型，可以重复
     * @param id     注册的对象的ID，相同类型不可以重复，不同类型可以重复
     * @param object
     */
    void registerLocalObject(Remote object, Class type, String id)  throws RemoteException;

    /**
     * 注册本地对象，类型及ID
     *
     * @param type   注册的对象类型，可以重复
     * @param id     注册的对象的ID，相同类型不可以重复，不同类型可以重复
     * @param object
     */
    void registerLocalObject(Remote object, String type, String id)  throws RemoteException;

    /**
     * 注册本地对象，类型及ID
     *
     * @param name   名字如果重复，已经存在的对象将被替换，全局唯一
     * @param object
     */
    void registerLocalObject(Remote object, String name)  throws RemoteException;

    /**
     * 按类型名称注册本地对象,如果此类型已经存在对象，则已经存在的对象将被替换
     *
     * @param type
     * @param object
     */
    void registerLocalObject(Remote object, Class type)  throws RemoteException;

    /**
     * 注册远程对象，类型及ID，在Server中仅仅将该对象存放在内存map中
     *
     * @param type   注册的远程对象类型，可以重复
     * @param id     注册的远程对象的ID，相同类型不可以重复，不同类型可以重复
     * @param object
     */
    void registerRemoteObject(Remote object, Class type, String id)  throws RemoteException;

    /**
     * 注册远程对象，类型及ID，在Server中仅仅将该对象存放在内存map中
     *
     * @param type   注册的对象类型，可以重复
     * @param id     注册的对象的ID，相同类型不可以重复，不同类型可以重复
     * @param object
     */
    void registerRemoteObject(Remote object, String type, String id)  throws RemoteException;

    /**
     * 注册远程对象，类型及ID，在Server中仅仅将该对象存放在内存map中
     *
     * @param name   名字如果重复，已经存在的对象将被替换，全局唯一
     * @param object
     */
    void registerRemoteObject(Remote object, String name)  throws RemoteException;

    /**
     * 按类型名称注册远程对象，在Server中仅仅将该对象存放在内存map中,如果此类型已经存在对象，则已经存在的对象将被替换
     *
     * @param type
     * @param object
     */
    void registerRemoteObject(Remote object, Class type)  throws RemoteException;
    
    <T> List<T> getRemoteObjectListInstanceOf(Class<T> type)
	throws RemoteException;
    
    
    
    /**
     * 根据名称注销对象
     *
     * @param name 要注销的对象名
     */
    void unregisterObject(String name)   throws RemoteException;

    /**
     * 根据类型注销对象
     *
     * @param type 要注销的对象类型，所有匹配的对象都会被注销
     */
    void unregisterObjectByType(Class type)   throws RemoteException;

    /**
     * 根据类型注销对象
     *
     * @param type
     */
    void unregisterObjectByType(String type)   throws RemoteException;

    /**
     * 根据类型注销对象
     *
     * @param type 要注销的类型
     * @param id   要注销的ID
     */
    void unregisterObject(String type, String id)   throws RemoteException;

    /**
     * 根据类型注销对象
     *
     * @param type 要注销的类型
     * @param id   要注销的ID
     */
    void unregisterObject(Class type, String id)   throws RemoteException;
    /**
     * 停止具体的注册在此远程服务中心的本地对象
     *
     * @param object
     * @throws RemoteException
     */
    void unregisterObject(Remote object) throws RemoteException;

    
    
    
    /**
     * 返回对象
     *
     * @param name
     * @param <T>
     * @return
     * @throws RemoteException
     */
    <T> T getObject(String name) throws RemoteException;

    /**
     * 根据类型获取对象
     *
     * @param type
     * @param <T>
     * @return
     * @throws RemoteException
     */
    <T> T getObject(Class<T> type) throws RemoteException;

    /**
     * 根据类型返回对象列表
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> getObjectList(Class<T> type)   throws RemoteException;

    

    /**
     * 根据类型返回对象列表
     *
     * @param typeName
     * @param <T>
     * @return
     */
    <T> List<T> getObjectList(String typeName)   throws RemoteException;

    /**
     * 停止所有提供远程访问的对象
     *
     * @throws RemoteException
     */
    void unexportObjects() throws RemoteException;

    

    /**
     * 停止RMIServer
     */
    void stop()   throws RemoteException;
}
