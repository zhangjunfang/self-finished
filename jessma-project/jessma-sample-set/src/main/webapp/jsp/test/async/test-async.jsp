<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    <title>测试异步 Action</title>
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
    	<form action="test/async/test-async.action" method="post">
    		<h2>Test Async</h2>
    		<table>
    		<tr><td align="right">Time Out:</td><td><input type="text" name="timeout" value="${empty __action.timeout ? -1 : __action.timeout}"></td></tr>
    		<tr><td align="right">Task Time:</td><td><input type="text" name="tasktime" value="${empty __action.tasktime ? 0 : __action.tasktime}"></td></tr>
    		<tr><td align="right">Is Async:</td><td>
    				<input type="radio" name="async" value="false"
    					<c:if test="${not empty __action.async and __action.async eq false}"> checked="checked"</c:if>>
    					否&nbsp;&nbsp;
    				<input type="radio" name="async" value="true"
    					<c:if test="${not empty __action.async and __action.async eq true}"> checked="checked"</c:if>>
    					是</td></tr>
    		<tr><td>&nbsp;</td><td><input type="submit" value="确 定">
    		&nbsp;&nbsp;<input type="reset" value="重 置"></td></tr>
    		<tr><td colspan="2" style="color: red;" align="left" rowspan="2">
    			<br>Async Flag:&nbsp;
    			<c:choose>
   					<c:when test="${not empty __action.async and __action.async eq false}">
    					<c:out value=""></c:out>
    				</c:when>
    				<c:when test="${empty __action.async and empty __action.flag}">
    					<c:out value="ready !"></c:out>
    				</c:when>
    				<c:when test="${not empty __action.async and empty __action.flag}">
    					<c:out value="complete"></c:out>
    				</c:when>
    				<c:otherwise>
    					<c:out value="${__action.flag}"></c:out>
    				</c:otherwise>
    			</c:choose>
    		</td></tr>
    		</table>
    	</form>
    </div>
  </body>
</html>
