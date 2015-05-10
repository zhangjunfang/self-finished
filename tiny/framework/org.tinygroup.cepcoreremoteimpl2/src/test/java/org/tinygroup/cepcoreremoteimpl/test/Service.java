package org.tinygroup.cepcoreremoteimpl.test;

import java.util.List;

public class Service {
	public String hello(String name){
		return "hello"+name;
	}
	
	public String helloException(){
		throw new RuntimeException("RuntimeException");
	}
	
	public String helloArray(String[] name){
		System.out.println(name);
		System.out.println("hello "+name);
		return "hello"+name;
	}
	
	public String helloUserArray(User[] users){
		System.out.println(users);
		for(User u:users){
			System.out.println("hello "+u.getName()+u.getAge());
		}
		
		return "hello"+users;
	}
	
	public String helloUserList(List<User> users){
		System.out.println(users);
		for(User u:users){
			System.out.println("hello "+u.getName()+u.getAge());
		}
		
		return "hello"+users;
	}
}
