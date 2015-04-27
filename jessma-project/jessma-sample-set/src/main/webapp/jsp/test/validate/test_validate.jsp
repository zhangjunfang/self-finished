<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title>测试输入校验</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>
  
  	<!-- include jQuery -->
	<script type="text/javascript" src="js/jquery-1.10.1.min.js"></script>

	<script type="text/javascript">
	
	var nickName, tips, err;
	
	$(function($) {
		nickName	= $("#nickName");
		tips		= $("#tips");
		err			= $("#err");

		$("#check").click(function() {
			$.getJSON("test/checkNickName.action", {"nickName" : nickName.val()}, function(json) {

				var color = (json.code == "1") ? "green" : "red";
				
				tips.css("color", color);
				tips.text(json.desc);
				err.text("");
			});
		});
	});
	
	</script>
  
  <body>
  <br>
  <div align="right">
  	<a href="index.action">首&nbsp;页</a>
  </div>
    <br><br><br><br>
    <div align="center">
    	<form action="test/checkValidate.action" method="post">
    		(已存在昵称：bruce, kingfisher, 怪兽)<br>
    		Nick Name: <input type="text" name="nickName" id="nickName" value="${nickname}">
    		&nbsp;<input type="button" id="check" value="检查">
    		&nbsp;<font size="-1"><span id="tips"></span></font>
    		<br>
    		<c:if test="${not empty errdesc}">
    			<font size="-1" color="red"><span id="err">${errdesc}</span></font>
    		</c:if>
			<br>
			<input type="submit" value="确 定">&nbsp;&nbsp;<input type="reset" value="重 置">
    	</form>    	
    </div>
  </body>
</html>
