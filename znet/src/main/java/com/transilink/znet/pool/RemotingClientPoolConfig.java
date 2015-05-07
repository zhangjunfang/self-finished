package com.transilink.znet.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import com.transilink.znet.ClientDispatcherManager;

public class RemotingClientPoolConfig extends GenericObjectPoolConfig { 
	private String brokerAddress = "127.0.0.1:15555";
	/**
	 * 可选项
	 * 如果配置不给出，ClientDispatcherManager内部生成，并自己管理关闭
	 * 如果配置给出，内部仅仅共享使用，不关闭
	 */
	private ClientDispatcherManager clientDispatcherManager;
	
	public String getBrokerAddress() {
		return brokerAddress;
	}

	public void setBrokerAddress(String brokerAddress) {
		this.brokerAddress = brokerAddress;
	}  
	
	
	
	
	public ClientDispatcherManager getClientDispatcherManager() {
		return clientDispatcherManager;
	}

	public void setClientDispatcherManager(
			ClientDispatcherManager clientDispatcherManager) {
		this.clientDispatcherManager = clientDispatcherManager;
	}

	@Override
	public RemotingClientPoolConfig clone() { 
		RemotingClientPoolConfig res = (RemotingClientPoolConfig)super.clone();
		res.brokerAddress = this.brokerAddress;
		return res;
	}

	@Override
	public String toString() {
		return "RemotingClientPoolConfig [brokerAddress=" + brokerAddress
				+ ", getMaxTotal()=" + getMaxTotal() + ", getMaxIdle()="
				+ getMaxIdle() + ", getMinIdle()=" + getMinIdle()
				+ ", getLifo()=" + getLifo() + ", getMaxWaitMillis()="
				+ getMaxWaitMillis() + ", getMinEvictableIdleTimeMillis()="
				+ getMinEvictableIdleTimeMillis()
				+ ", getSoftMinEvictableIdleTimeMillis()="
				+ getSoftMinEvictableIdleTimeMillis()
				+ ", getNumTestsPerEvictionRun()="
				+ getNumTestsPerEvictionRun() + ", getTestOnCreate()="
				+ getTestOnCreate() + ", getTestOnBorrow()="
				+ getTestOnBorrow() + ", getTestOnReturn()="
				+ getTestOnReturn() + ", getTestWhileIdle()="
				+ getTestWhileIdle() + ", getTimeBetweenEvictionRunsMillis()="
				+ getTimeBetweenEvictionRunsMillis()
				+ ", getEvictionPolicyClassName()="
				+ getEvictionPolicyClassName() + ", getBlockWhenExhausted()="
				+ getBlockWhenExhausted() + ", getJmxEnabled()="
				+ getJmxEnabled() + ", getJmxNameBase()=" + getJmxNameBase()
				+ ", getJmxNamePrefix()=" + getJmxNamePrefix()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
