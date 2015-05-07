## restbird-server
基于netty4的轻量级http服务, 支持restful风格, 集合了spring, mybatis, bonecp等流行框架
自带一个查询学生信息的http接口,是学习netty的好例子,同时又便于二次开发

## 本地开发环境
1. 导入该git项目，成功下载jar包
2. 执行sql/install.sql,数据库为mysql，修改server.properties数据库连接帐号和密码
3. 启动服务com.restbird.server.httpserver.Server,执行main方法
4. TestController执行main方法或者浏览器输入http://localhost:9010/school/student?sid=15075501显示返回json string

如果需要新增一个Controller，直接修改resources下的controller/controller-mapping.xml文件，然后实现相应的Controller
	
	<bean id="studentController" class="com.restbird.server.school.controller.SchoolController" />

	<bean id="controllerMap" class="com.restbird.server.httpserver.netty.ControllerMap">
		<property name="controllerMap">
			<map>
				<entry key="/school/student" value-ref="studentController" />
			</map>
		</property>
	</bean>

## 打包部署
直接项目右键run as maven build,Goals里输入package,然后run
最终生成一个部署zip压缩包，直接解压执行脚本即可
