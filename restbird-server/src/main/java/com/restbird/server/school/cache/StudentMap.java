package com.restbird.server.school.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.restbird.server.school.db.bean.Student;

public class StudentMap {
	private static Map<String, Student> studentMap = new ConcurrentHashMap<String, Student>();

	public static Map<String, Student> getStudentMap() {
		return studentMap;
	}

	public static void setStudentMap(Map<String, Student> studentMap) {
		StudentMap.studentMap = studentMap;
	}

}
