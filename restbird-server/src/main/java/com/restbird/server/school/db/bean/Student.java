package com.restbird.server.school.db.bean;

public class Student {
	private Long id;
	/** 学号 **/
	private String sid;
	/** 姓名 **/
	private String sname;
	/** 年龄 **/
	private int sage;
	/** 性别 **/
	private String ssex;
	/** 班级 **/
	private String sclass;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public int getSage() {
		return sage;
	}

	public void setSage(int sage) {
		this.sage = sage;
	}

	public String getSsex() {
		return ssex;
	}

	public void setSsex(String ssex) {
		this.ssex = ssex;
	}

	public String getSclass() {
		return sclass;
	}

	public void setSclass(String sclass) {
		this.sclass = sclass;
	}

}
