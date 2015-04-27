jessma-sample-set 测试工程部署：
-----------------------------------------------

*** 部署环境 ***

JRE/JDK	：JRE/JDK 1.6 以上
JavaEE	：JavaEE 6 以上
Tomcat	：Tomcat 6 以上（如果要运行“异步 Action”示例则需要 Tomcat 7 以上）

*** 部署步骤 ***

1、创建 mysql 数据库：myjessma
2、执行脚本：myjessma.sql，创建 数据表
3、把 jessma-sample-set 项目导入 Eclipse 的 Workspace
4、根据需要修改 Hibernate、MyBatis 和 Jdbc 的数据库配置（mybatis.cfg.properties、druid.cfg.xml / druid.cfg.properties、jndi.cfg.xml / jndi.cfg.properties、jdbc.cfg.xml / jdbc.cfg.properties、mybatis.cfg.properties、proxool.xml、proxool-2.xml、META-INF/context.xml）
5、启动 tomcat 6/7
6、把 jessma-sample-set 发布到 tomcat（执行命令：'mvn clean tomcat6:deploy' 或 'mvn clean tomcat7:deploy'）
7、访问：http://localhost:8080/jessma
