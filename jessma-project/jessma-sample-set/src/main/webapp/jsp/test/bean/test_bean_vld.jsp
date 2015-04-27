<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../jessma-base.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${__base}">
    
    <title>测试 Bean 自动验证</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="icon" href="favicon.ico" type="image/x-icon">
  </head>
  
  <script language=javascript>
  function autoValidate()
  {
	  document.person.action="test/check-bean-validation.action?fromCheckAction=true";
	  document.person.submit();
  }

  function manualValidate()
  {
	  document.person.action="test/check-bean-validation!run.action?fromCheckAction=true";
	  document.person.submit();
  }
  </script>
  
  <body>
  <br>
  <div align="right">
  	<a href="index.action">首&nbsp;页</a>
  </div>
    <br><br><br><br>
    <div align="center">
    	<form name="person" method="post">
    	<table>
    	  <c:choose>
    	  <c:when test="${param.fromCheckAction}">
    	  <tr><td align="right">
    		First Name: <input type="text" name="firstName" value="${__action.person.firstName}">
    		</td><td><p:err key="firstName" cssStyle="font-size: x-small;color: red;"/></td></tr>
    		 <tr><td align="right">
    		Last Name: <input type="text" name="lastName" value="${__action.person.lastName}">
    		</td><td><p:err key="lastName" cssStyle="font-size: x-small;color: red;"/></td></tr>
    		<tr><td align="right">
    		Birthday: <input type="text" name="birthday" 
    		<c:if test="${not empty __action.person.birthday}"> value="<fmt:formatDate value='${__action.person.birthday}' pattern='yyyy-MM-dd'/>"</c:if>
    		></td><td><p:err key="birthday" cssStyle="font-size: x-small;color: red;"/></td></tr>
    		<tr><td align="right">
    		Gender: 男 <input type="radio" name="gender" value="false" <c:if test="${not __action.person.gender}">checked="checked"</c:if>>
    			&nbsp;女 <input type="radio" name="gender" value="true" <c:if test="${__action.person.gender}">checked="checked"</c:if>>
    		</td><td><p:err key="gender" cssStyle="font-size: x-small;color: red;"/></td></tr>
    		<tr><td align="right">
    		Working age: <select name="workingAge">
    			<option value="-1" <c:if test="${__action.person.workingAge == -1}">selected="selected"</c:if>>-请选择-</option>
    			<option value="3" <c:if test="${__action.person.workingAge == 3}">selected="selected"</c:if>>三年</option>
    			<option value="5" <c:if test="${__action.person.workingAge == 5}">selected="selected"</c:if>>五年</option>
    			<option value="10" <c:if test="${__action.person.workingAge == 10}">selected="selected"</c:if>>十年</option>
    			<option value="20" <c:if test="${__action.person.workingAge == 20}">selected="selected"</c:if>>二十年</option>
    		</select>
    		</td><td><p:err key="workingAge" cssStyle="font-size: x-small;color: red;"/></td></tr>
    		<tr><td align="right">
    		Interest: <br/>
    			游泳 <input type="checkbox" name="interest" value="1" 
    			 <c:if test="${not empty __action.person.interest}">
    			    <c:forEach items="${__action.person.interest}" var="it" varStatus="st">
						<c:if test="${st.current == 1}">checked="checked"</c:if>
     				</c:forEach>  		
    			 </c:if>><br/>
	    		打球 <input type="checkbox" name="interest" value="2" 
    			 <c:if test="${not empty __action.person.interest}">
    			    <c:forEach items="${__action.person.interest}" var="it" varStatus="st">
						<c:if test="${st.current == 2}">checked="checked"</c:if>
     				</c:forEach>  		
    			 </c:if>><br/>
	    		下棋 <input type="checkbox" name="interest" value="3" 
    			 <c:if test="${not empty __action.person.interest}">
    			    <c:forEach items="${__action.person.interest}" var="it" varStatus="st">
						<c:if test="${st.current == 3}">checked="checked"</c:if>
     				</c:forEach>  		
    			 </c:if>><br/>
	    		打麻将 <input type="checkbox" name="interest" value="4" 
    			 <c:if test="${not empty __action.person.interest}">
    			    <c:forEach items="${__action.person.interest}" var="it" varStatus="st">
						<c:if test="${st.current == 4}">checked="checked"</c:if>
     				</c:forEach>  		
    			 </c:if>><br/>
	    		看书 <input type="checkbox" name="interest" value="5" 
    			 <c:if test="${not empty __action.person.interest}">
    			    <c:forEach items="${__action.person.interest}" var="it" varStatus="st">
						<c:if test="${st.current == 5}">checked="checked"</c:if>
     				</c:forEach>  		
    			 </c:if>>
    		</td><td><p:err key="interest" cssStyle="font-size: x-small;color: red;"/></td></tr>
    	  </c:when>
    	  <c:otherwise>
    	  <tr><td align="right">
    		First Name: <input type="text" name="firstName" value=""></td><td>&nbsp;</td></tr>
    		<tr><td align="right">
    		Last Name: <input type="text" name="lastName" value=""></td><td>&nbsp;</td></tr>
    		<tr><td align="right">
    		Birthday: <input type="text" name="birthday" value=""></td><td>&nbsp;</td></tr>
    		<tr><td align="right">
    		Gender: 男 <input type="radio" name="gender" value="false">
    			&nbsp;女 <input type="radio" name="gender" value="true" checked="checked"></td><td>&nbsp;</td></tr>
    		<tr><td align="right">
    		Working age: <select name="workingAge">
    			<option value="-1" selected="selected">-请选择-</option>
    			<option value="3">三年</option>
    			<option value="5">五年</option>
    			<option value="10">十年</option>
    			<option value="20">二十年</option>
    		</select></td><td>&nbsp;</td></tr>
    		<tr><td align="right">
    		Interest: <br/>
    			游泳 <input type="checkbox" name="interest" value="1"><br/>
	    		打球 <input type="checkbox" name="interest" value="2"><br/>
	    		下棋 <input type="checkbox" name="interest" value="3"><br/>
	    		打麻将 <input type="checkbox" name="interest" value="4"><br/>
	    		看书 <input type="checkbox" name="interest" value="5"></td><td>&nbsp;</td></tr>
     	  </c:otherwise>
    	  </c:choose>
    		<tr><td colspan="2">&nbsp;</td></tr>
    		<tr><td colspan="2" align="center">
			<input type="button" value="自动验证" onclick="autoValidate()">&nbsp;
			<input type="button" value="手工验证" onclick="manualValidate()">&nbsp;
			<input type="reset" value="重 置">
			</td></tr>
			</table>
    	</form>    	
    </div>
  </body>
</html>
