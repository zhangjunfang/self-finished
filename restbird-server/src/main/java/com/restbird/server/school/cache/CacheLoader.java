package com.restbird.server.school.cache;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.restbird.server.school.db.bean.Student;
import com.restbird.server.school.db.service.StudentService;

/**
 * 数据库中读取数据载入本地缓存，也可自己配置redis缓存等
 * 
 * @author ocean
 *
 */
public class CacheLoader {
	@Autowired
	private StudentService studentService;

	public void init() {
		loadStudentCache();
	}

	private void loadStudentCache() {
		List<Student> studentList = studentService.getStudentList();
		if (studentList != null) {
			Map<String, Student> studentMap = StudentMap.getStudentMap();
			for (Student student : studentList) {
				studentMap.put(student.getSid(), student);
			}
		}
	}
}
