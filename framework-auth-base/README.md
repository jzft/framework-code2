# 权限管理基本功能模块

## 能力

提供基本的权限用户相关的增删查改功能

## 使用

### spring ioc

扫描的包路径要添加：com.framework.auth

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
#是否启用测试模式
shiro.testEnabled = true
```