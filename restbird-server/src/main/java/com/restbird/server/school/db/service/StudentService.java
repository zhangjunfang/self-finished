package com.restbird.server.school.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restbird.server.school.db.bean.Student;
import com.restbird.server.school.db.dao.StudentDao;

@Service
public class StudentService {
	@Autowired
	private StudentDao studentDao;

	public List<Student> getStudentList() {
		return studentDao.getStudentList();
	}
}
