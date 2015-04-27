jessma-sample-hello 模板工程部署：
-----------------------------------------------

*** 部署环境 ***

JRE/JDK	：JRE/JDK 1.6 以上
JavaEE	：JavaEE 6 以上
Tomcat	：Tomcat 6 以上

*** 部署步骤 ***

1、把 jessma-sample-hello 项目导入 Eclipse 的 Workspace
2、启动 tomcat 6/7
2、把 jessma-sample-hello 发布到 tomcat（执行命令：'mvn clean tomcat6:deploy' 或 'mvn clean tomcat7:deploy'）
4、访问：http://localhost:8080/hello
