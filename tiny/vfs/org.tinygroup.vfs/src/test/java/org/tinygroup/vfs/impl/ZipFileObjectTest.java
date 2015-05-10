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
package org.tinygroup.vfs.impl;

import java.io.InputStream;

import junit.framework.TestCase;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * zip fileobject 测试用例
 * 
 * @author songsw
 * 
 */
public class ZipFileObjectTest extends TestCase {

	public void testZipFileObject() throws Exception {

		String path=getClass().getResource("/test/test.zip").getFile();
		FileObject fileObject = VFS.resolveFile(path);
		FileObject fo = findFileObject(fileObject, "file1.txt");
		if (fo != null) {
			InputStream inputStream = fo.getInputStream();
			byte[] buf = new byte[(int) fo.getSize()];
			inputStream.close();
			assertTrue(buf!=null);
		}
	}

	private FileObject findFileObject(FileObject fileObject, String name) {

		if (fileObject.getFileName().equals(name)) {
			return fileObject;
		} else {
			if (fileObject.isFolder() && fileObject.getChildren() != null) {
				for (FileObject fo : fileObject.getChildren()) {
					FileObject f = findFileObject(fo, name);
					if (f != null) {
						return f;
					}
				}
			}
		}
		return null;
	}
	
	public void testSubFile(){
		String path=getClass().getResource("/test/test.zip").getFile();
		FileObject fileObject = VFS.resolveFile(path);
		FileObject subFile= fileObject.getFileObject("/");
		assertEquals(fileObject, subFile);
		subFile= fileObject.getFileObject("/read-tests");
		assertTrue(subFile!=null);
		subFile= fileObject.getFileObject("/read-tests/dir1");
		assertTrue(subFile!=null);
		subFile= fileObject.getFileObject("/read-tests/dir1/file1.txt");
		assertTrue(subFile!=null);
		assertEquals("file1.txt", subFile.getFileName());
	}
	
	
}
