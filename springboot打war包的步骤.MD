##1. 打包方式
````
    1.1 jar
        服务化的概念，后续接触springcloud，所有的服务打包都是以jar的形式存在
    1.2 war
        应用程序的概念，也可以向外提供服务和接口     
````
##2. 打war包第一步
````
    在food-platform-api的pom.xml中添加
    	<!-- 打包war [1] -->
    	<packaging>war</packaging>
````
##3. 打war包第二步
````
    移除项目内置的tomcat（food-platform目录下pom.xml）
    <!-- 打包war [2] 移除自带内置tomcat -->
    			<exclusions>
    				<exclusion>
    					<artifactId>spring-boot-starter-tomcat</artifactId>
    					<groupId>org.springframework.boot</groupId>
    				</exclusion>
    			</exclusions>
````
##4. 打war包第三步
````
    添加servlet依赖food-platform目录下pom.xml）
    <!-- 打包war [3] 添加依赖 -->
    		<dependency>
    			<groupId>javax.servlet</groupId>
    			<artifactId>javax.servlet-api</artifactId>
    			<scope>provided</scope>
    		</dependency>
````
##5. 打war包第四步
````
    增加war的启动类（com.zx.WarStarterApplication）
````