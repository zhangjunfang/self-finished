<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,org.jessma.util.*,org.jessma.util.http.HttpHelper,org.jessma.mvc.Action" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%@ taglib prefix="p" uri="http://www.jessma.org/jsp/tags" %><%

/*
 * Copyright Bruce Liang (ldcsaa@gmail.com)
 *
 * Version	: JessMA 3.5.1
 * Author	: Bruce Liang
 * Website	: http://www.jessma.org
 * Project	: http://www.oschina.net/p/portal-basic
 * Blog		: http://www.cnblogs.com/ldcsaa
 * WeiBo	: http://weibo.com/u/1402935851
 * QQ Group	: 75375912
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

Action.BaseType baseType = (Action.BaseType)application.getAttribute(Action.Constant.APP_ATTR_BASE_TYPE);

if(baseType == Action.BaseType.AUTO)
{
	String __base = (String)request.getAttribute(Action.Constant.REQ_ATTR_BASE_PATH);
	
	if(__base == null)
	{
		__base = HttpHelper.getRequestBasePath(request);
		request.setAttribute(Action.Constant.REQ_ATTR_BASE_PATH, __base);
	}
}
%>