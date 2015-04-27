<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title>测试页面静态化</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>
  
  <!-- include jQuery -->
  <script type="text/javascript" src="js/jquery-1.10.1.min.js"></script>
	
  <script type="text/javascript">
	
	$(function($) {
		$("#query").click(function() {
			// var href		= $("base:first").attr("href") + "test/page/queryInterest.action?gender=$1&experience=$2";
			var href		= $("base:first").attr("href") + "page/interest-$1-$2.html";
			var gender		= $("select[name='gender']").val();
			var experience	= $("select[name='experience']").val();

			if(gender == null)
				gender = 0;
			
			if(experience == null)
				experience = 0;

			href = href.replace("$1", gender);
			href = href.replace("$2", experience);

			window.location.href = href;
		});
	});
  </script>
  
  <body>
  <br>
  <div align="right">
  	<a href="index.action">首&nbsp;页</a>
  </div>
    <br>
    <div align="center">
        <!-- 查询条件 -->
    	<h2>查询用户</h2>
    	<table>
    		<tr><td>性别:</td><td><select name="gender">
    				<option value="0"
    					<c:if test="{empty __action.gender || __action.gender == 0}">
    					 selected="selected"</c:if>
    				>全部</option>
	    			<c:forEach items="${__cache.genders}" var="it" varStatus="st">
	    				<option value="${it.id}"
	    					<c:if test="${not empty __action.gender && it.id == __action.gender}">
	    						 selected="selected"
	    					</c:if>
	    				>${it.name}</option>
	    			</c:forEach>
    				</select>&nbsp;&nbsp;</td>
	   			<td>工作年限:</td><td><select name="experience">
    				<option value="0"
    					<c:if test="{empty __action.experience || __action.experience == 0}">
    					 selected="selected"</c:if>
    				>全部</option>
	    			<c:forEach items="${__cache.experences}" var="it" varStatus="st">
	    				<option value="${it.id}"
	    					<c:if test="${not empty __action.experience && it.id == __action.experience}">
	    						 selected="selected"
	    					</c:if>
	    				>${it.name}</option>
	    			</c:forEach>
    			</select>&nbsp;</td>
    			<td><input type="button" id="query" value="查 询">
    		</td></tr>
    	</table>
    	<br>
    	<!-- 查询结果 -->
    	<table border="1" cellpadding="10" cellspacing="0">
    	<tr><th>ID</th><th>姓 名</th><th>年 龄</th><th>性 别</th><th>工作年限</th><th>兴趣爱好</th></tr>
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
    		</tr>
    	</c:forEach>
    	</c:if>
    	</table>
    </div>
  </body>
</html>
