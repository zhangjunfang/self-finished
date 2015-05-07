package org.zstacks.znet.nio; 

public interface Codec{
	public IoBuffer encode(Object msg);
	public Object decode(IoBuffer buff);
}
