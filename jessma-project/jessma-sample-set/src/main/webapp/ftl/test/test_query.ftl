<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>测试 Freemarker</title>
    <base href=${__base}>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
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
    	<!-- 查询结果 -->
    	<h2>查询用户</h2>
    	<table border="1" cellpadding="10" cellspacing="0">
    	<tr><th>ID</th><th>姓 名</th><th>年 龄</th><th>性 别</th><th>工作年限</th><th>兴趣爱好</th></tr>
    	<#if __action?? && __action.users??>
    	<#list __action.users as it>
    		<tr align="center">
    			<td>${it.id!""}&nbsp;</td>
    			<td>${it.name!""}&nbsp;</td>
    			<td>${it.age!""}&nbsp;</td>
    			<td><#if it.genderObj??>${it.genderObj.name!""}</#if>&nbsp;</td>
    			<td><#if it.experienceObj??>${it.experienceObj.name!""}</#if>&nbsp;</td>
    			<td>&nbsp;
    				<#if it.interestObjs??>
    					<#list it.interestObjs as obj>
    						${obj.name!""}&nbsp;
    					</#list>
    				</#if>
    			</td>
    		</tr>
    	</#list>
    	</#if>
    	</table>
    </div>
  </body>
</html>
