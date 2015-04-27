
JessMA 核心依赖包：
-----------------------------------------------
1、JessMA 框架有 3 个基础依赖包：cglib-nodep-x.x.x.jar、dom4j-x.x.x.jar、slf4j-api-x.x.x.jar
2、文件上传功能则需要 2 个依赖包：commons-fileupload-x.x.x.jar、commons-io-x.x.x.jar
3、其他依赖包均为可选依赖包，可根据需要加入到项目中

HelloJessMA 模板工程部署：
-----------------------------------------------

*** 部署环境 ***

JRE/JDK	：JRE/JDK 1.6 以上
JavaEE	：JavaEE 6 以上
Tomcat	：Tomcat 6 以上

*** 部署步骤 ***

1、把 HelloJessMA 项目加入 Eclipse 的 Workspace
2、把 HelloJessMA 发布到 tomcat（默认发布目录为 hello）
3、启动 tomcat，检查启动日志，确保没有异常
4、访问：http://localhost:8080/hello

MyJessMA 测试工程部署：
-----------------------------------------------

*** 部署环境 ***

JRE/JDK	：JRE/JDK 1.6 以上
JavaEE	：JavaEE 6 以上
Tomcat	：Tomcat 6 以上（如果要运行“异步 Action”示例则需要 Tomcat 7 以上）

*** 注意事项 ***

如果要在 Tomcat-6.0 中开启 Bean Validation 机制，需要把 “tomcat_7_to_6_lib” 文件夹下的 3 个 jar 文件替换  Tomcat-6.0 的 lib 目录下的同名文件

*** 部署步骤 ***

1、创建 mysql 数据库：myjessma
2、执行脚本：myjessma.sql
3、把 MyJessMA 项目加入 Eclipse 的 Workspace
4、加入 MyJessMA 项目依赖的 jar 包：
    <A> 方式一：在 Eclipse 中创建三个名称分别为“jessma-lib”、“spring-lib”和“guice-lib”的 User Library，并把 jessma-lib/required、 jessma-lib/optional、spring-lib 和 guice-lib 文件夹下的 jar 包加入其中
    <B> 方式二：把  jessma-lib/required、 jessma-lib/optional、spring-lib 和 guice-lib 文件夹下的 jar 包加入 MyJessMA 项目的 lib 目录中，并取消 MyJessMA 对“jessma-lib”、“spring-lib”和“guice-lib”的 User Library 的依赖
5、根据需要修改 Hibernate、MyBatis 和 Jdbc 的数据库配置（mybatis.cfg.properties、druid.cfg.xml / druid.cfg.properties、jndi.cfg.xml / jndi.cfg.properties、jdbc.cfg.xml / jdbc.cfg.properties、mybatis.cfg.properties、proxool.xml、proxool-2.xml、META-INF/context.xml）
6、把 MyJessMA 发布到 tomcat（默认发布目录为 jessma）
7、启动 tomcat，检查启动日志，确保没有异常
8、访问：http://localhost:8080/jessma
