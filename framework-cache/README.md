# 脚手架

## 能力
通过静态类RedisHelper统一去管理redis调用，自动回收redis连接，更好的通过代码去管理redis，更好的排除问题，
如：
1、防止个人单独写redis的实现，忘记关闭连接，导致占用连接及线程无法释放；<br/>
2、对数据过期时间的设置统一管理，防止大量内存数据永不释放。


## 使用
将redisTemplate注入RedisHelper。
