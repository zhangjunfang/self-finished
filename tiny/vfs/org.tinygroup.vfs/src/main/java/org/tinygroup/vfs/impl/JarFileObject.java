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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.VFSRuntimeException;

public class JarFileObject extends AbstractFileObject {
	private static final long serialVersionUID = -8744742869664989315L;
	private List<FileObject> children;
	private JarFile jarFile = null;
	private File file = null;
	private JarEntry jarEntry = null;
	private File cacheFile = null;

	public JarFileObject(SchemaProvider schemaProvider, String resource) {
		super(schemaProvider);
		try {
			this.file = new File(resource);
			if (file.exists()) {
				this.jarFile = new JarFile(resource);
			}
		} catch (IOException e) {
			throw new VFSRuntimeException(resource + "打开失败，错误："
					+ e.getMessage(), e);
		}
	}

	@Override
	public void clean() {
		try {
			jarFile.close();
		} catch (IOException e) {
			throw new VFSRuntimeException(e);
		}
	}

	public JarFileObject(JarFileObject parent, JarEntry entry) {
		super(parent.getSchemaProvider());
		this.jarFile = parent.jarFile;
		this.file = parent.file;
		this.jarEntry = entry;
	}

	public String getFileName() {
		if (jarEntry != null) {
			String[] names = jarEntry.getName().split("/");
			return names[names.length - 1];
		} else {
			return file.getName();
		}
	}

	public String getPath() {
		if (jarEntry != null) {
			return "/" + jarEntry.getName();
		}
		return "/";
	}

	public String getAbsolutePath() {
		String path = file.getAbsolutePath();
		if (File.separatorChar == '\\') {
			path = path.replaceAll("\\\\", "/");
		}
		if (jarEntry != null) {
			path = path + "!/" + jarEntry.getName();
		}
		return path;
	}

	public String getExtName() {
		String name;
		if (jarEntry != null) {
			if (!jarEntry.isDirectory()) {
				name = jarEntry.getName();
			} else {
				return null;
			}
		} else {
			name = file.getName();
		}
		int lastIndexOfDot = name.lastIndexOf('.');
		if (lastIndexOfDot == -1) {
			// 如果不存在
			return null;
		}
		return name.substring(lastIndexOfDot + 1);
	}

	public long getSize() {
		if (jarEntry != null) {
			return jarEntry.getSize();
		}
		if (file.exists() && file.isFile()) {
			return file.length();
		}
		return 0;
	}

	public InputStream getInputStream() {
		try {
			if (jarEntry != null) {
				if (cacheFile == null || !cacheFile.exists()
						|| cacheFile.length() != this.getSize()) {
					createCacheFile();
					FileOutputStream out = new FileOutputStream(cacheFile);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(
							jarFile.getInputStream(jarEntry));
					StreamUtil.io(bufferedInputStream, out, true, true);
				}
				return new BufferedInputStream(new FileInputStream(cacheFile));
			}
			if (file.exists() && file.isFile()) {
				return new JarInputStream(new FileInputStream(file));
			} else {
				throw new VFSRuntimeException(file.getAbsolutePath()
						+ "不存在，或不是文件。");
			}
		} catch (Exception e) {
			throw new VFSRuntimeException(file.getAbsolutePath()
					+ "获取FileInputStream出错，原因" + e);
		}
	}

	@Override
	public String toString() {
		return getURL().toString();
	}

	public boolean isFolder() {
		if (jarEntry != null) {
			return jarEntry.isDirectory();
		}
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public List<FileObject> getChildren() {
		if (!file.exists()) {
			return null;
		}
		if (children == null) {
			children = new ArrayList<FileObject>();
			Enumeration<JarEntry> e = jarFile.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if (getParent() == null) {
					String[] names = entry.getName().split("/");
					// 如果当前是jar文件，如果
					if (names.length == 1) {
						addSubItem(entry);
					}
				} else {
					// 如果不是根目录
					String parentName = jarEntry.getName();
					if (!entry.getName().equals(jarEntry.getName())
							&& entry.getName().startsWith(parentName)) {
						String fn = entry.getName().substring(
								parentName.length());
						String[] names = fn.split("/");
						if (names.length == 1) {
							addSubItem(entry);
						}
					}
				}
			}
		}
		return children;
	}

	private void addSubItem(JarEntry entry) {
		JarFileObject jarFileObject = new JarFileObject(this, entry);
		jarFileObject.setParent(this);
		children.add(jarFileObject);
	}

	public long getLastModifiedTime() {
		if (jarEntry != null) {
			return jarEntry.getTime();
		}
		return file.lastModified();
	}

	public boolean isExist() {
		if (jarEntry != null) {
			return true;
		}
		return file.exists();
	}

	public boolean isInPackage() {
		return jarFile != null;
	}

	public FileObject getChild(String fileName) {
		if (getChildren() != null) {
			for (FileObject fileObject : getChildren()) {
				if (fileObject.getFileName().equals(fileName)) {
					return fileObject;
				}
			}
		}
		return null;
	}

	public URL getURL() {
		try {
			if (jarEntry != null) {
				return new URL(JarSchemaProvider.JAR_PROTOCOL
						+ FileSchemaProvider.FILE_PROTOCOL + getAbsolutePath());
			} else {
				return new URL(FileSchemaProvider.FILE_PROTOCOL
						+ getAbsolutePath());
			}
		} catch (MalformedURLException e) {
			throw new VFSRuntimeException(e);
		}
	}

	public OutputStream getOutputStream() {
		try {
			if (jarEntry != null) {
				if (cacheFile == null || !cacheFile.exists()
						|| cacheFile.length() != this.getSize()) {
					createCacheFile();
					BufferedInputStream bufferedInputStream = new BufferedInputStream(
							jarFile.getInputStream(jarEntry));
					FileOutputStream out = new FileOutputStream(cacheFile);
					StreamUtil.io(bufferedInputStream, out, true, true);
				}
				return new BufferedOutputStream(new FileOutputStream(cacheFile));
			}
			if (file.exists() && file.isFile()) {
				return new JarOutputStream(new FileOutputStream(file));
			} else {
				throw new VFSRuntimeException(file.getAbsolutePath()
						+ "不存在，或不是文件。");
			}
		} catch (Exception e) {
			throw new VFSRuntimeException(file.getAbsolutePath()
					+ "获取outputStream出错，原因" + e);
		}
	}

	private void createCacheFile() {
		String tempPath = System.getProperty("java.io.tmpdir");
		if (!tempPath.endsWith(File.separator)) {
			tempPath = tempPath + File.separator;
		}
		tempPath = tempPath + getExtName() + File.separator;
		File tempPathFile = new File(tempPath);
		if (!tempPathFile.exists()) {
			boolean created = tempPathFile.mkdirs();
			if (!created) {
				throw new VFSRuntimeException("创建临时目录" + tempPath + "失败！");
			}
		}
		cacheFile = new File(tempPath + getFileName() + "_"
				+ getLastModifiedTime());
	}

}
