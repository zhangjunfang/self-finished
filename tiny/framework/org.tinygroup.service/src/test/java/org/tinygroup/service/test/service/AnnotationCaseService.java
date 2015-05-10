package org.tinygroup.service.test.service;

import java.util.List;

import org.tinygroup.service.annotation.ServiceComponent;
import org.tinygroup.service.annotation.ServiceMethod;
import org.tinygroup.service.annotation.ServiceResult;
import org.tinygroup.service.test.base.ServiceUser;

@ServiceComponent()
public class AnnotationCaseService {
	@ServiceMethod(serviceId="annotationUserObject")
	@ServiceResult(name="result")
	public ServiceUser userObject(ServiceUser user) {
		System.out.println(user.getName() + ":s" + user.getAge());
		return user;
	}
	@ServiceMethod(serviceId="annotationUserList")
	@ServiceResult(name="result")
	public List<ServiceUser> userList(ServiceUser user, List<ServiceUser> users) {
		users.add(user);
		return users;
	}
	@ServiceMethod(serviceId="annotationUserArray")
	@ServiceResult(name="result")
	public ServiceUser[] userArray(ServiceUser[] users) {
		System.out.println(users.length);
		return users;
	}
}
