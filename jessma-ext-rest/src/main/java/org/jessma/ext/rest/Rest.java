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

package org.jessma.ext.rest;

/** REST 接口 <br>
 * 
 * 处理 REST 请求的 Action 需要实现本接口。
 */
public interface Rest
{
	/** Rails-style REST 标准方法：处理 GET 请求，不带 id 请求参数（如：/entity） */
	RestResult index() throws Exception;
	/** Rails-style REST 标准方法：处理 POST 请求，不带 id 请求参数（如：/entity） */
	RestResult create() throws Exception;
	/** Rails-style REST 标准方法：处理 DELETE 请求，不带 id 请求参数（如：/entity） */
	RestResult deleteAll() throws Exception;
	/** Rails-style REST 标准方法：处理 PUT 请求，带 id 请求参数（如：/entity/{param}） */
	RestResult update(int id) throws Exception;
	/** Rails-style REST 标准方法：处理 DELETE 请求，带 id 请求参数（如：/entity/{param}） */
	RestResult delete(int id) throws Exception;
	/** Rails-style REST 标准方法：处理 GET 请求，带 id 请求参数（如：/entity/{param}） */
	RestResult show(int id) throws Exception;
	/** Rails-style REST 标准方法：处理 GET 请求，带 id 请求参数，且指定操作 edit 资源（如：/entity/{param}/edit） */
	RestResult edit(int id) throws Exception;
	/** Rails-style REST 标准方法：处理 GET 请求，不带 id 请求参数，且指定操作 new 资源（如：/entity/new） */
	RestResult editNew() throws Exception;
	
	/* Action Result (And method name) for Rails-style REST */
	String INDEX		= "index";
	String CREATE		= "create";
	String DELETE_ALL	= "deleteAll";
	String UPDATE		= "update";
	String DELETE		= "delete";
	String SHOW			= "show";
	String EDIT			= "edit";
	String EDIT_NEW		= "editNew";
	
	/** 与 {@link Rest} 相关的常量 */
	public interface Constant
	{
		/** Request Param -> REST 请求类型 */
		public static final String REQ_PARAM_REST_METHOD = "__rest_method";
		/** Request Param -> REST 渲染类型 */
		public static final String REQ_PARAM_REST_RENDER = "__rest_render";
		/** Request Attribute -> {@link RestContext} 对象 */
		public static final String REQ_ATTR_REST_CONTEXT = "__rest_context";
		/** Request Attribute -> REST 模型对象 */
		public static final String REQ_ATTR_REST_MODEL	 = "__rest_model";
	}
}
