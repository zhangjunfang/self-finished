<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title>测试文件下载</title>
    
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
    <table border="0"><tr><td>
    <ol>
    	<li><a href="test/checkDownload.action?type=1" target="__blank">测试下载（相对路径）</a></li>
    	<li><a href="test/checkDownload.action?type=2" target="__blank">测试下载（绝对路径）</a></li>
    	<li><a href="test/checkDownload.action?type=3" target="__blank">测试下载（字节数组）</a></li>
    	<li><a href="test/checkDownload.action?type=4" target="__blank">测试下载（字节流）</a></li>
    </ol>
    </td></tr></table>
    </div>
  </body>
</html>
