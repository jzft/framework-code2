# 脚手架

## 能力

该项目为组件基础模板工程(后端)，为快速构件项目组件库，子项目通过maven中央仓快速应用相应组件，实现插拔式功能导入；

所有maven工程，依赖脚手架pom文件父类;好处：

* 1、省略一些重复开发，如权限管理，分库分表，hbase持久层框架，redis持久层等。

* 2、只是依赖不会引进任何jar，maven工程依赖jar保持最小单元，去除无关jar。如：springcloud应用项目，只需要引入framework-cloud就行了，如果要在springcloud应用基础上加入数据库分库分表，只需要引入framework-shard和相关数据库驱动；

* 3、可以让配置统一管理,如 ：1、对资源文件解释处理css字体文件乱码问题；2、统一编码utf-8；通过maven -P [dev,test,prod] 动态指定开发测试生产环境。

* 4、所有maven工程引用jar版本统一管理，都在父级pom指定版本，方便jar版本更新，和所有项目统一版本。


当前功能有： 
* 通用权限（RBAC）<br/>
* excel通用导入<br/>
* 分库分表模块<br/>
* springcloud 通过maven引用最小单元jar
* redis持久层工具包。 <br/>
* 数据库持久层mybatis。 <br/>
* hbase持久层phoenix框架<br/>
.... <br/>

持续完善....

## 使用
打包到nexus私服,或者导入代码，依赖父级工程，导入相关依赖包

