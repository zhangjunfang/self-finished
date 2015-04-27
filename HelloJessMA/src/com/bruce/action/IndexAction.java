package com.bruce.action;

import java.util.Date;

import org.jessma.mvc.ActionSupport;

public class IndexAction extends ActionSupport
{
	Date now;
	
	public Date getNow()
	{
		return now;
	}

	@Override
	public String execute() throws Exception
	{
		now = new Date();
		return SUCCESS;
	}
}
