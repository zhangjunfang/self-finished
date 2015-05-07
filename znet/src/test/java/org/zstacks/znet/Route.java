/**
 * 
 */
package org.zstacks.znet;

import java.io.Serializable;

/**
 * @author ocean
 *
 */
public class Route implements Serializable {

	private static final long serialVersionUID = 5511128043480860427L;

	private Version version;

	private Group group;

	private NameSpace nameSpace;
	
	public Route() {
		super();
	}

	public Route(Version version, Group group, NameSpace nameSpace) {
		super();
		this.version = version;
		this.group = group;
		this.nameSpace = nameSpace;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public NameSpace getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(NameSpace nameSpace) {
		this.nameSpace = nameSpace;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(nameSpace).append("/").append(group).append("/").append(version)
				;
		return builder.toString();
	}

	
}
