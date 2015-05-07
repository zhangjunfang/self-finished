package com.restbird.server.school.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.restbird.server.httpserver.netty.Request;
import com.restbird.server.httpserver.netty.bean.Modle;
import com.restbird.server.httpserver.netty.mvc.Controller;
import com.restbird.server.school.db.bean.Student;
import com.restbird.server.school.service.SchoolService;

/**
 * 业务Controller
 * 
 * @author littleBirdTao
 *
 */
public class SchoolController implements Controller {

	@Autowired
	private SchoolService schoolService;

	public Modle handleRequest(Request request) throws Exception {
		String sid = request.getParameter("sid");
		Student student = schoolService.getStudentBySid(sid);

		JSONObject resJSONObj = new JSONObject();
		resJSONObj.put("student", student);
		Modle modle = new Modle();
		modle.setContent(JSON.toJSONString(resJSONObj));
		modle.setContentType(Modle.CONTENT_TYPE_JSON);
		return modle;
		// return new
		// Modle().setContent(JSON.toJSONString(resJSONObj)).setContentType(Modle.CONTENT_TYPE_JSON);
	}

}
