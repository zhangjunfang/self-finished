package org.tinygroup.vfs.cache;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

import junit.framework.TestCase;

/**
 * @Description:  
 * @author  SongShuangwang
 * @date 2014-7-16 上午09:50:47 
 */
public class CacheTest extends TestCase {
	
	public void testCache(){
		String path=getClass().getResource("/vfs-0.0.1-SNAPSHOT.jar").getFile();
		FileObject fileObject1= VFS.resolveFile(path);
		FileObject fileObject2= VFS.resolveFile(path);
		assertTrue(fileObject1==fileObject2);
	}
	

}
