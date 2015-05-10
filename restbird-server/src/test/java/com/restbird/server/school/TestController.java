package com.restbird.server.school;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * httpclient模拟客户端测试程序<br>
 * 1.执行sql目录下的install.sql脚本<br>
 * 2.启动com.restbird.server.httpserver.Server右键run as java application<br>
 * 3.执行该测试程序或在浏览器中输入http://localhost:9010/school/student?sid=15075501<br>
 * 4.返回 {"student":{"id":1,"sage":18,"sclass":"121322","sid":"15075501","sname":
 * "tom","ssex":"1"}}<br>
 * 
 * @author ocean
 *
 */
public class TestController {
	public static void main(String[] args) {

		// HttpClient httpClient = new HttpClient();
		// String url = "http://localhost/nameSpace/group/1.1";
		// PostMethod postMethod = new PostMethod(url);
		// postMethod.setRequestHeader("Connection", "close");
		// postMethod.setParameter("name", "15075501");
		// httpClient.getParams().setContentCharset("utf-8");
		// try {
		// httpClient.executeMethod(postMethod);
		// System.out.println(postMethod.getStatusLine());
		// System.out.println(postMethod.getResponseBodyAsString());
		// } catch (HttpException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// postMethod.releaseConnection();
		// }

		HttpClient httpClient = new HttpClient();
		String url = "http://localhost:9010/school/student";
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("Connection", "close");
		postMethod.setParameter("sid", "15075501");
		httpClient.getParams().setContentCharset("utf-8");
		try {
			httpClient.executeMethod(postMethod);
			System.out.println(postMethod.getStatusLine());
			System.out.println(postMethod.getResponseBodyAsString());
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}

	}
}
