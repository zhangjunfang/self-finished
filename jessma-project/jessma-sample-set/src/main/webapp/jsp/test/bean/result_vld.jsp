<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title>测试 Bean 装配 - 2</title>
    
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
    <br><br><br><br>
    <div align="center">
    <table border="1">
    <caption>Person Attributs</caption>
    
    	<tr><td>Name</td><td><c:out value="${__action.person.firstName} ${__action.person.lastName}"/>&nbsp;</td></tr>
    	<tr><td>Brithday</td><td><c:out value="${__action.person.birthday}"/>&nbsp;</td></tr>
    	<tr><td>Gender</td><td><c:out value="${__action.person.gender}"/>&nbsp;</td></tr>
    	<tr><td>Working Age</td><td><c:out value="${__action.person.workingAge}"/>&nbsp;</td></tr>
    	<tr><td>Interest</td><td><c:forEach var="its" items="${__action.person.interest}">
							 		<c:out value="${its}" /> &nbsp;
							  </c:forEach>&nbsp;</td></tr>
    </table>
   </div>
  </body>
</html>
