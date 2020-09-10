# phoenix持久层组件

## 能力

1、自动建表。<br/>
2、注解对象映射表查询、新增。<br/>

## 使用

#### 第一步：安装 hadoop+hbase，hbase整合phoenix
·phoenix对版本匹配要求比较高
·本组件用到的版本：
·hadoop2.8.5
·hbase1.4.13
·apche-phoenix-4.15.0-HBase-1.4
（本人对其几个版本集群都试过，hadoop(2.7.1+hbase2、hbase1.1+phoenix5.0、phoenix5.0、4.14等，效果不是那么好，集成phoenix经常报类找不到，个人感觉以上在hbase1.4当中，以上版本是最好的集群了）</br>
还有一个版本集成成功的版本：hadoop2.7.1、hbase1.1.4、phoenix4.8，太老了不建议用。</br>
hbase基于lsm树，新增数据快，如果使用hbase2，新增会更快，我没用过。</br>
hive建phoenix外表的时候是没有apche-phoenix-4.15.0依赖包的，使用apche-phoenix-4.14.0的jar就行了。

###第一步：springboot文件注入配置'phoenix.entitys[0]=com.test.XXEntity'

###第二步：
·配置winutils.exe，环境变量HADOOP_HOME
·配置环境变量如JAVA_HOME。
·配置hosts文件，指定hbase的zookeeper、master、regionserver的地址映射。

### 使用BasePhoenixBaseDao 类，新增查询phoenix数据。