package com.restbird.server.school.db.dao;

import java.util.List;

import com.restbird.server.school.db.bean.Student;

public interface StudentDao {
	public List<Student> getStudentList();
}
