<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">

    <title><p:msg key="jsp-index.header"/></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>

  <body>
    <br>
  	<div align="right" style="font-size: xx-small;">
  	Version: 3.5 by <a href="http://www.jessma.org">JessMA Open Source</a>&nbsp;<br><br>
  	<a href="test/reload-cfg.action?type=mvc">更新 MVC 配置</a>&nbsp;
  	<a href="test/reload-cfg.action?type=rest">更新 REST 配置</a>&nbsp;
  	<a href="test/reload-cfg.action?type=user">更新用户配置</a>&nbsp;
  	<a href="test/reload-cfg.action?type=res">更新资源文件</a>&nbsp;
  	<a href="test/reload-cfg.action?type=all">更新所有配置</a>&nbsp;
  	</div>
    <div align="center">
    <table border="0"><tr><td>
  	<br>
    <p:msg key="jsp-index.sayhello" p0="${times}"/>
    <br>
    <br>
    <ol>
    	<li><a href="test/testBean_1.action"><p:msg key="jsp-index.TestBean" p0="1"/></a></li>
    	<li><a href="test/testBean_2.action"><p:msg key="jsp-index.TestBean" p0="2"/></a></li>
    	<li><a href="test/testBean_3.action"><p:msg key="jsp-index.TestBean" p0="3"/></a></li>
    	<li><a href="test/testBeanValidation.action"><p:msg key="jsp-index.TestBeanValidation"/></a></li>
    	<li><a href="test/testValidate.action"><p:msg key="jsp-index.TestValidate"/></a></li>
    	<li><a href="test/testI18N.action"><p:msg key="jsp-index.TestI18N"  p0="Session"/></a></li>
    	<li><a href="test/testI18N_Cookie.action"><p:msg key="jsp-index.TestI18N" p0="Cookie"/></a></li>
    	<li><a href="test/testUpload.action">测试文件上传</a></li>
    	<li><a href="test/testDownload.action">测试文件下载</a></li>
    	<li><a href="test/dao/testJdbc.action">测试 DAO (JDBC)</a></li>
    	<li><a href="test/dao/testMyBatis.action">测试 DAO (MyBatis)</a></li>
    	<li><a href="test/dao/testHibernate.action">测试 DAO (Hibernate)</a></li>
    	<!-- <li><a href="test/page/queryInterest.action">测试页面静态化</a></li> -->
    	<!-- <li><a href="page/interest-0-0.html">测试页面静态化</a></li> -->
    	<!-- <li><a href="
	    	<c:url value="/test/page/queryInterest.action">
	    		<c:param name="gender" value="0" />
	    		<c:param name="experience" value="0" />
	    	</c:url>
    	">测试页面静态化</a></li> -->
    	<li><a href="<c:url value="/test/page/queryInterest.action?gender=0&experience=0" />">测试页面静态化</a></li>
    	<li><a href="test/ftl/queryInterest.action">测试 Freemarker</a></li>
    	<li><a href="test/entry/user.action">测试多入口 Action</a></li>
    	<li><a href="test/spring/user.action">测试 Spring 整合</a></li>
    	<li><a href="test/guice/user.action">测试 Guice 整合</a></li>
    	<li><a href="test/conv/user.action">测试 Action Convention</a></li>
    	<li><a href="user">测试 REST Convention - 1</a></li>
    	<li><a href="user-2">测试 REST Convention - 2</a></li>
    	<li><a href="test/async/test-async.action">测试异步 Action（Servlet 3.0）</a></li>
    	<li><a href="test-rest/-1/0/true">测试异步 REST Action（Servlet 3.0）</a></li>    	
    </ol>
    </td></tr></table>
    </div>
  </body>
</html>
