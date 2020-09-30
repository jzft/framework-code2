# 脚手架

## 能力
通过此脚手架开发springcloud应用，引入最小量的jar，打包大概30兆左右。


使用： 
	<!-- 开发时，依赖脚手架父级pom -->
	<parent>
	 	<groupId>com.framework.concise</groupId>
	    <artifactId>concise</artifactId>
	    <version>0.0.1-SNAPSHOT</version>
    </parent>
   <properties>
   <!-- mainClass为引用启动类
  	<mainClass>startup.Test1Startup</mainClass>
  </properties>
  <artifactId>test1-project</artifactId>
  <name>test1-project</name>
  <dependencies>
  	<dependency>
        <groupId>com.framework.concise</groupId>
        <artifactId>framework-cloud-web</artifactId>
        <type>pom</type>
    </dependency>
  </dependencies>
  <build>
      <plugins>
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
           </plugin>
      </plugins>
  </build>
