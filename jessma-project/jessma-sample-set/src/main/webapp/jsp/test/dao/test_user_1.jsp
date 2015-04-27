<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    <title>测试 DAO (JDBC)</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>
  
  <body>
  <br>
  <div align="right">
  	<a href="index.action">首&nbsp;页</a>
  </div>
    <br>
    <div align="center">
    	<!-- 创建新用户表单 -->
    	<form action="test/dao/createUser1.action" method="post">
    		<h2>创建新用户</h2>
    		<table>
    		<tr><td align="right">姓名:</td><td><input type="text" name="name" value=""></td></tr>
    		<tr><td align="right">年龄:</td><td><input type="text" name="age" value=""></td></tr>
    		<tr><td align="right">性别:</td><td>
    			<c:forEach items="${__cache.genders}" var="it" varStatus="st">
    				<input type="radio" name="gender" value="${it.id}"
    					<c:if test="${it.id == 1}"> checked="checked"</c:if>>
    			${it.name}&nbsp;&nbsp;</c:forEach>
    		</td></tr>
    		<!-- 
    		男 <input type="radio" name="gender" value="1" checked="checked">
    			&nbsp;女 <input type="radio" name="gender" value="2">
    		 -->
    		<tr><td align="right">工作年限:</td><td><select name="experience">
    			<c:forEach items="${__cache.experences}" var="it" varStatus="st">
    				<option value="${it.id}"
    					<c:if test="${it.id == 3}"> selected="selected"</c:if>
    				>${it.name}</option>
    			</c:forEach>
    			<!--  
    			<option value="1">3 年以下</option>
    			<option value="2">3-5 年</option>
    			<option value="3" selected="selected">5-10 年</option>
    			<option value="4">10 年以上</option>
    			-->
    		</select></td></tr>
    		<tr><td align="right">兴趣爱好:</td><td>
    			<c:forEach items="${__cache.interests}" var="it" varStatus="st">
    				<input type="checkbox" name="interests" value="${it.id}"
    					<c:if test="${it.id % 2 != 0}"> checked="checked"</c:if>>
    			${it.name}&nbsp;&nbsp;</c:forEach>  		
    		</td></tr>
    		<!-- 
    		游泳 <input type="checkbox" name="interest" value="1" checked="checked">
	    		&nbsp;打球 <input type="checkbox" name="interest" value="2" checked="checked">
	    		&nbsp;下棋 <input type="checkbox" name="interest" value="3">
	    		&nbsp;打麻将 <input type="checkbox" name="interest" value="4">
	    		&nbsp;看书 <input type="checkbox" name="interest" value="5" checked="checked">
    		 -->
    		<tr><td>&nbsp;</td><td><input type="submit" value="创 建">
    		&nbsp;&nbsp;<input type="reset" value="重 置"></td></tr>
    		</table>
    	</form>
    	<hr>
    	<!-- 查询用户表单 -->
    	<form action="test/dao/queryUser1.action" method="post">
    		<h2>查询用户</h2>
    		<table>
    		<tr><td>姓名:<td><input type="text" name="name" 
    			<c:if test="${param.fromQueryAction && not empty __action.name}"> value="${__action.name}"</c:if>
    		>&nbsp;</td>
    			<td>工作年限:</td><td><select name="experience">
    				<option value="0"
    					<c:if test="{empty __action.experience || __action.experience == 0}">
    					 selected="selected"</c:if>
    				>全部</option>
	    			<c:forEach items="${__cache.experences}" var="it" varStatus="st">
	    				<option value="${it.id}"
	    					<c:if test="${param.fromQueryAction && not empty __action.experience && it.id == __action.experience}">
	    						 selected="selected"
	    					</c:if>
	    				>${it.name}</option>
	    			</c:forEach>
    		</select>&nbsp;</td>
    		<td><input type="submit" value="查 询">
    		</td></tr>
    		</table>
    	</form>
    	<br>
    	<c:if test="${param.fromQueryAction}">
    	<!-- 查询结果列表 -->
    	<table border="1" cellpadding="10" cellspacing="0">
    	<tr><th>ID</th><th>姓 名</th><th>年 龄</th><th>性 别</th><th>工作年限</th><th>兴趣爱好</th><th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th></tr>
    	<c:if test="${not empty __action.users}">
    	<c:forEach items="${__action.users}" var="it" varStatus="st">
    		<tr align="center">
    			<td>${it.id}&nbsp;</td>
    			<td>${it.name}&nbsp;</td>
    			<td>${it.age}&nbsp;</td>
    			<td>${it.genderObj.name}&nbsp;</td>
    			<td>${it.experienceObj.name}&nbsp;</td>
    			<td>
    				<c:forEach items="${it.interestObjs}" var="obj">
    					${obj.name}&nbsp;
    				</c:forEach>
    			&nbsp;</td>
    			<td><a href="test/dao/deleteUser1.action?id=${it.id}">删 除</a>
    			</td>
    		</tr>
    	</c:forEach>
    	</c:if>
    	</table>
    	</c:if>
    </div>
  </body>
</html>
