# 权限管理基本功能模块

## 能力

提供基本的权限用户相关的增删查改功能

## 使用

### spring ioc

* 扫描的包路径要添加：com.framework.auth
* 数据库导入framework-auth下RBAC.sql文件,建立相应的表。

### mybatis

* mapper的xml文件地址:classpath*:mapper/*.xml
* typeAliasesPackage的包路径:com.framework.auth.pojo.entity


### shiro配置

模块有内置的配置并开放了部分给使用者配置。
使用者可以通过在resources下对应的环境${env}目录下建立my-shiro-config.properties文件进行自定义配置。
自定义配置支持的配置属性：
```
#zookeeper地址
shiro.zookeeper.url=localhost
#web domain
shiro.web.domain=127.0.0.1
#zookeeper是否启用
shiro.zookeeper.zkEnabled = false
#权限是否持久保存在内存，即登出后不清空内存中权限；为了加快访问，权限存在jvm，不在redis；如果需要管理权限，且系统做了负载均衡策略不是ip_hash,请开启zookeeper，通常情况isPersistCache不需要设置成true.
shiro.isPersistCache = false
#图形验证码在session中的key值，如果不需要验证码，删除该配置，或者设置成false
shiro.captchaSessionKey = captchaCode
```