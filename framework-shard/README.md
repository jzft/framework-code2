# 通用分库组件

## 能力

1、对需要分库分表的数据库表代码实现。<br/>
2、支持一个方法的事务中使用另外切片数据库的事务。<br/>
3、支持按照分库字段的取值范围进行分库<br/>
4、支持分库字段数字取余数进行分库<br/>

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
parent.datasource.poolPreparedStatements=true<br/>
parent.datasource.maxPoolPreparedStatementPerConnectionSize=50<br/>
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
*  3、scope为数字如‘0’，分库字段'余数%分表数据源总数'余数为0取该库;规则：0<=scope<分库数据源总数


###第五步：方法注解指定分片数据源。(业务开发阶段使用)
* 1、通过方法注解@Shard指定target切片数据源；
* 2、或通过方法注解@SplitShard指定分库字段，根据分库字段的数字，映射配置文件数据源的shard.datasource.[target].scope值<br/>
