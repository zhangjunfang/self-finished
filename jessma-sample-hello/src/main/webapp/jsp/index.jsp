<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">

    <title>Hello JessMA</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>

  <body>
    <br>
    <div align="center">
		Hello, The time now is <fmt:formatDate value="${__action.now}" pattern="yyyy-MM-dd HH:mm" />
    </div>
  </body>
</html>
