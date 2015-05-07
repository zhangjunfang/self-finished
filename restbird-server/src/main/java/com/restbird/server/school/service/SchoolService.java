package com.restbird.server.school.service;

import org.springframework.stereotype.Service;

import com.restbird.server.school.cache.StudentMap;
import com.restbird.server.school.db.bean.Student;

@Service
public class SchoolService {
	public Student getStudentBySid(String sid) {
		Student student = StudentMap.getStudentMap().get(sid);
		return student;
	}

}
