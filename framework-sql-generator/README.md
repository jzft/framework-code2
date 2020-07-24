# 数据库生成器

## 描述

设计此生成器的目的在于自动化产执行数据库初始化脚本，减少开发时间，降低项目的开发难度。

## 兼容性

* mysql

## 使用方式

引入此依赖后，需要手动注入一个名为mainDataSource的DataSource bean。
在配置文件配置sql.script-file-path值，指明sql脚本的在classpath中的路径，以"/"开头