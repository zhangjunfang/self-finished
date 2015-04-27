<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title><p:msg key="jsp-set_locale.header" p0="Cookie"/></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>
  
  <body>
  <br>
  <div align="right">
  	<a href="index.action"><p:msg key="jsp-set_locale.back-to-index"/></a>
  </div>
    <br><br><br><br>
    <div align="center">
    	<br><br><br>
    	<span style="font-size: x-small; color:blue;"><p:msg key="jsp-set_locale.notice" p0="Locale Session Attr"/></span>
    	<br><br><br>
    	<c:set var="en" value="english" />
    	<c:set var="cn" value="chinese" />
    	<c:set var="del" value="none" />

    	<c:url var="to_en" value="test/testI18N_Cookie.action">
    		<c:param name="lan" value="${en}" />
    	</c:url>
    	
    	<c:url var="to_cn" value="test/testI18N_Cookie.action">
    		<c:param name="lan" value="${cn}" />
    	</c:url>
    	
    	<c:url var="delete" value="test/testI18N_Cookie.action">
    		<c:param name="lan" value="${del}" />
    	</c:url>

    	<a href="${to_en}"><p:msg key="jsp-set_locale.english"/></a>
    	&nbsp;&nbsp;&nbsp;&nbsp;
    	<a href="${to_cn}"><p:msg key="jsp-set_locale.chinese"/></a>
    	&nbsp;&nbsp;&nbsp;&nbsp;
    	<a href="${delete}"><p:msg key="jsp-set_locale.delete" p0="Locale Cookie"/></a>
    	<br><br>
    	<a href="test/testI18N_Cookie.action?__locale=en_US" style="font-size: x-small;"><p:msg key="jsp-set_locale.url_en"/></a>
    	&nbsp;&nbsp;&nbsp;&nbsp;
    	<a href="test/testI18N_Cookie.action?__locale=zh_CN" style="font-size: x-small;"><p:msg key="jsp-set_locale.url_cn"/></a>
   		<br><br>
    	<p:msg key="jsp-set_locale.test" params="${__action.serial}"/>
    </div>
  </body>
</html>
