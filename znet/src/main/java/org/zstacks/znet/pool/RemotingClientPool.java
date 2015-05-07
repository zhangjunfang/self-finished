package org.zstacks.znet.pool;

import java.io.IOException;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.zstacks.znet.ClientDispatcherManager;
import org.zstacks.znet.RemotingClient;

public class RemotingClientPool extends GenericObjectPool<RemotingClient>{ 
	private ClientDispatcherManager clientDispatcherManager = null;
	private boolean ownClientDispatcherManager = false;
	
	public RemotingClientPool(RemotingClientPoolConfig config) throws IOException{
		super(new RemotingClientFactory(config), config);
		
		RemotingClientFactory factory = (RemotingClientFactory)getFactory();
		this.clientDispatcherManager = config.getClientDispatcherManager();
		if(this.clientDispatcherManager == null){
			this.clientDispatcherManager = new ClientDispatcherManager();
			this.ownClientDispatcherManager = true;
		}
		this.clientDispatcherManager.start();
		
		factory.clientMgr = this.clientDispatcherManager;
	}  

	public ClientDispatcherManager getClientDispatcherManager(){
		return this.clientDispatcherManager;
	}
	
	@Override
	public void close() {  
		super.close();  
		try {
			if(ownClientDispatcherManager && this.clientDispatcherManager != null){
				this.clientDispatcherManager.close();
			}
		} catch (IOException e) {
			//ignore
		}
	}

}

class RemotingClientFactory extends BasePooledObjectFactory<RemotingClient> {
	ClientDispatcherManager clientMgr;
	private final String broker; 
	
	public RemotingClientFactory(RemotingClientPoolConfig config) throws IOException{
		this.broker = config.getBrokerAddress();
	}

	@Override
	public RemotingClient create() throws Exception { 
		return new RemotingClient(broker, clientMgr); 
	}

	@Override
	public PooledObject<RemotingClient> wrap(RemotingClient obj) { 
		return new DefaultPooledObject<RemotingClient>(obj);
	} 
	
	@Override
	public void destroyObject(PooledObject<RemotingClient> p) throws Exception {
		RemotingClient client = p.getObject();
		client.close();
	}
	
	@Override
	public boolean validateObject(PooledObject<RemotingClient> p) {
		return p.getObject().hasConnected();
	}
}