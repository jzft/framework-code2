# 通用分库组件
  该方案已支持某平台年过百亿流水订单系统，日订单量30w+；
 通过的spring动态数据源+druid+mybatis实现，通过jdk动态代理对数据库connection及statement对象方法传入的sql进行了正则表达式解释，如mybatis传入的表名tablename，则解释分片表名为01_tablename；让未分库分表的系统重构更加简便，无需修改任何代码；具体项目实例参照test-shard项目。
 目前只支持mysql的ddl：create table；dml：insert，update，delete；及select语句；
 
## 技术点
 1、jdk动态代理<br/>
 2、正则表达式<br/>
 3、druid数据源<br/>
 4、spring动态数据源<br/>
 5、stack、ThreadLocal<br/>
 6、线程池<br/>
 7、服务器原理<br/>
 8、mybatis<br/>
 9、spring aop<br/>
 10、spring事务机制
 
## 能力
1、对需要分库分表的数据库表代码实现。<br/>
2、支持一个方法的事务中使用另外切片数据库的事务。<br/>
3、支持按照分库字段的取值范围进行分库<br/>
4、支持分库字段数字取余数进行分库<br/>
5、支持分表

## 使用
###第一步：配置分片数据源（项目初始化）
* 配置多个target

shard.datasource.[target].driverClassName=com.mysql.cj.jdbc.Driver
shard.datasource.[target].url=jdbc:mysql://localhost:3306/datasource0?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai<br/>
shard.datasource.[target].username=user<br/>
shard.datasource.[target].password=****<br/>
shard.datasource.[target].scope = 0<br/>

###第二步：配置数据源通用配置（项目初始化）
parent.datasource.maxWait=30000<br/>
parent.datasource.timeBetweenEvictionRunsMillis=60000<br/>
parent.datasource.minEvictableIdleTimeMillise=300000<br/>
parent.datasource.validationQuery=SELECT 'x'<br/>
parent.datasource.testWhileIdle=true<br/>
parent.datasource.testOnBorrow=true<br/>
parent.datasource.testOnReturn=true<br/>
parent.datasource.filters=config,stat,log4j<br/>
parent.datasource.maxActive=50<br/>
parent.datasource.minIdle=0<br/>
parent.datasource.initialSize=0<br/><br/>

batis.typeAliasesPackage=com.company.example.pojo<br/>
batis.mapperLocations=classpath*:mybatis/mapper/*.xml<br/>
batis.basePackages=com.company.example.mapper

###第三步：将配置注入AbsShardDbConfig子类，是切片配置生效（项目初始化）
* 如：
@Configuration<br/>
@PropertySource("shard-db.properties")<br/>
public class ShardDbConfig extends AbsShardDbConfig {}<br/>

###第四步：约定分库字段规则。如：userId。（项目初始化）
* 分库 规则定义shard.datasource.[target].scope如下：
*  1、scope是‘a-m’或者‘0-4’,分库字段开头0-4或者a-m取该库，分库字段值必须映射到所有的库。如开头0-9，a-z都必须设置。
*  2、scope为字母如‘am’，分库字段以am target=am
*  3、scope为%数字如‘%0’，分库字段'余数%分表数据源总数'余数为0取该库;规则：0<=scope<分库数据源总数


###第五步：方法注解指定分片数据源。(业务开发阶段使用)
* 1、通过方法注解@Shard指定target切片数据源；
* 2、或通过方法注解@SplitShard指定分库字段，根据分库字段的数字，映射配置文件数据源的shard.datasource.[target].scope值<br/>
