package org.tinygroup.dbrouter.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class ConsistentHash<T> {

	private final int numberOfReplicas;// 每个机器节点关联的虚拟节点个数
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();// 环形虚拟节点
	
	
	
	public ConsistentHash(Collection<T> nodes) {
		this(100,nodes);
	}

	/**
	 * 
	 * @param hashFunction
	 *            hash 函数接口
	 * @param numberOfReplicas
	 *            每个机器节点关联的虚拟节点个数
	 * @param nodes
	 *            真实机器节点
	 */
	public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
		this.numberOfReplicas = numberOfReplicas;
		for (T node : nodes) {
			addShardInfo(node);
		}
	}

	/**
	 * 增加虚拟接点与真实机器节点映射
	 * 
	 * @param node
	 */
	public void addShardInfo(T node) {
		for (int i = 0; i < this.numberOfReplicas; i++) {
			circle.put(this.hash(node.toString() + i), node);
		}
	}

	public List<Long> getShard(T node){
		List<Long> datas=new ArrayList<Long>();
		for( Entry<Long, T> entry: circle.entrySet()){
			if(entry.getValue().equals(node)){
				datas.add(entry.getKey());
			}
		}
		return datas;
	}
	
	/**
	 * 删除真实机器节点
	 * 
	 * @param node
	 */
	public void removeShardInfo(T node) {
		for (int i = 0; i < this.numberOfReplicas; i++) {
			circle.remove(this.hash(node.toString() + i));
		}
	}

	/**
	 * 取得真实机器节点
	 * 
	 * @param key
	 * @return
	 */
	public T getShardInfo(String key) {
		if (circle.isEmpty()) {
			return null;
		}

		long hash = hash(key);
		if (!circle.containsKey(hash)) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);// 沿环的顺时针找到一个虚拟节点
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}

		return circle.get(hash); // 返回该虚拟节点对应的真实机器节点的信息
	}

	/**
	 * MurMurHash算法，是非加密HASH算法，性能很高，碰撞率低
	 */

	public Long hash(String key) {
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(
					ByteOrder.LITTLE_ENDIAN);
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return h;
	}
 
    public static String getRandomString(int length) {   
    	         StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");   
    	         StringBuffer sb = new StringBuffer();   
    	         Random random = new Random();   
    	         int range = buffer.length();   
    	        for (int i = 0; i < length; i ++) {   
    	            sb.append(buffer.charAt(random.nextInt(range)));   
    	       }   
    	        return sb.toString();   
    	     }  
}
