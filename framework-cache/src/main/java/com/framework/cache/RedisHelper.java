package com.framework.cache;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import com.framework.cache.execption.RedisExecption;
import com.framework.cache.lock.LockCallback;
import com.framework.utils.NumUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import redis.clients.jedis.JedisPoolConfig;

public class RedisHelper {
    public static RedisTemplate redisTemplate;

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    private static final String IGNORE_EXEC_KEY = "_ignoreExec";



    public static <T> T lockExec(LockCallback<T> callBack, String lockKey,
                                 Integer timeout, Integer expireSeconds) {
        RedisLock lock = new RedisLock(lockKey);
        try {
            lock.lock(timeout, expireSeconds);
            return callBack.exec();
        } catch (Exception e) {
            throw new RedisExecption(e);
        } finally {
            lock.unlock();
        }
    }

    public final static String LOCKSTR = "POOL_EXEC:";

    public static <T> RedisPoolExecVo<T> poolExec(LockCallback<T> callBack, String lockKey, Integer maxExecSize,
                                                  Integer timeout) {
        String key = LOCKSTR + lockKey;
        final Integer expireSeconds = timeout / 1000 + 10;
        Long currExecCount = incr(key);
        if (currExecCount == null) {
            return new RedisPoolExecVo<T>(callBack.exec(), 1);
        }
        if (currExecCount.intValue() > maxExecSize) {
            return new RedisPoolExecVo<>();
        }

        try {
            return new RedisPoolExecVo<T>(callBack.exec(), 1);
        } catch (Exception e) {
            throw new RedisExecption(e);
        } finally {
            try {
                decr(key, expireSeconds);
            } catch (Exception e) {
                try {
                    decr(key, expireSeconds);
                } catch (Exception e2) {
                    decr(key, expireSeconds);
                }
            }
        }
    }


    public static <T> T lockExec(LockCallback<T> callBack, String lockKey,
                                 Integer timeout) {
        final Integer expireSeconds = timeout / 1000 + 10;
        RedisLock lock = new RedisLock(lockKey);
        try {
            lock.lock(timeout, expireSeconds);
            return callBack.exec();
        } catch (Exception e) {
            throw new RedisExecption(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试加锁并执行操作
     * 如果加锁失败直接返回
     * 如果加锁成功，则指定锁时间
     *
     * @param action        待执行的动作
     * @param key           加锁的key
     * @param expireSeconds 锁超时时间，单位秒
     * @param <T>           泛型参数
     * @return
     */
    public static <T> T ignoreExec(LockCallback<T> action, String key,
                                   int expireSeconds) {

        if (setnx(key + IGNORE_EXEC_KEY, "TRUE", expireSeconds)) {
            try {
                return action.exec();
            } catch (Exception e) {
                throw new RedisExecption("ignoreExec error", e);
            } finally {
                del(key + IGNORE_EXEC_KEY);
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public static Boolean exists(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return connection.exists(serialize(key));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, true);
    }

    /**
     * @throws
     * @Title: setObj
     * @Description: 设置键值
     * @param: @param key
     * @param: @param value
     * @param: @param expireSeconds   过期时间，单位：秒
     * @return: void
     * @author lyq
     * @Date 2017年1月6日 下午3:51:58
     */
    @SuppressWarnings("unchecked")
    public static void setObj(final Object key, final Object value,
                              final Integer expireSeconds) {
        if (key == null || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.multi();
                    setObj(key, value, expireSeconds, connection);
                    connection.exec();
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, true);
    }

    public static void setObj(final Object key, final Object value, final Integer expireSeconds,
                              final RedisConnection connection) {
        byte[] byteKey = redisTemplate.getKeySerializer().serialize(key);
        connection.set(byteKey, redisTemplate.getValueSerializer().serialize(value));
        if (NumUtil.intValue(expireSeconds) != -1) {
            connection.expire(byteKey, expireSeconds);
        }
    }

    @SuppressWarnings({"unchecked"})
    public static boolean setnx(final String key, final Object value,
                                final Integer expireSeconds) throws RedisExecption {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.multi();
                    Boolean result = setnx(key, value, expireSeconds, connection);
                    List<Object> list = connection.exec();
                    if (CollectionUtils.isNotEmpty(list)) {
                        result = (Boolean) list.get(0);
                    }
                    return result;
                } catch (Exception e) {
//					connection.discard();
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static Boolean setnx(final String key, final Object value, final Integer expireSeconds,
                                final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        Boolean result = connection.setNX(byteKey,
                serialize(value));
        if (NumUtil.intValue(expireSeconds) != -1) {
            connection.expire(byteKey, expireSeconds);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void set(final String key, final Object value,
                           final Integer expireSeconds) {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.multi();
                    set(key, value, expireSeconds, connection);
//					connection.expire(byteKey, expireSeconds);
                    connection.exec();
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, true);
    }

    public static void set(final String key, final Object value, final Integer expireSeconds,
                           final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        if (NumUtil.intValue(expireSeconds) != -1) {
            connection.setEx(byteKey, expireSeconds, serialize(value));
        } else {
            connection.set(byteKey, serialize(value));
        }
    }


    @SuppressWarnings("unchecked")
    public static void zadd(final String key, final Object value,
                            final Integer expireSeconds) {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    zadd(key, value, expireSeconds, connection);
//					connection.expire(byteKey, expireSeconds);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, true);
    }

    public static void zadd(final String key, final Object value, final Integer expireSeconds,
                            final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        if (NumUtil.intValue(expireSeconds) != -1) {
            connection.zAdd(byteKey, -1d, serialize(value));
            connection.expire(byteKey, expireSeconds);
        } else {
            connection.set(byteKey, serialize(value));
        }
    }

    @SuppressWarnings("unchecked")
    public static void zrem(final String key, final Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    zrem(key, value, connection);
//					connection.expire(byteKey, expireSeconds);
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static void zrem(final String key, final Object value, final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        connection.zRem(byteKey, serialize(value));
    }

    @SuppressWarnings("unchecked")
    public static Integer zrank(final String key, final Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        return (Integer) redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return zrank(key, value, connection);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static Integer zrank(final String key, final Object value, final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        Long result = connection.zRank(byteKey, serialize(value));
        if (result == null) {
            return -1;
        }
        return NumUtil.intValue(result);
    }

    @SuppressWarnings("unchecked")
    public static Integer zcard(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key或value不能为空");
        }
        return (Integer) redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return zcard(key, connection);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static Integer zcard(final String key, final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        Long result = connection.zCard(byteKey);
        if (result == null) {
            return -1;
        }
        return NumUtil.intValue(result);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> zrange(final String key, Integer start, Integer end, Class<T> clz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    List<T> result = zrange(key, start, end, clz, connection);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        }, true);
    }

    public static <T> List<T> zrange(final String key, Integer start, Integer end, Class<T> clz,
                                     final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        Set<byte[]> values = connection.zRange(byteKey, start, end);
        if (values == null) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (byte[] value : values) {
            T obj = unserialize(value, clz);
            result.add(obj);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> zrangeByLex(final String key, Class<T> clz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(key);
                    Set<byte[]> values = connection.zRangeByLex(byteKey);
                    if (values == null) {
                        return null;
                    }
                    List<T> result = new ArrayList<T>();
                    for (byte[] value : values) {
                        T obj = unserialize(value, clz);
                        result.add(obj);
                    }
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Long sadd(final String key, final Object value,
                            final Integer expireSeconds) {
        if (StringUtils.isEmpty(key) || value == null) {
            throw new RedisExecption("key或value不能为空");
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    connection.multi();
                    return sadd(key, value, expireSeconds, connection);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static Long sadd(final String key, final Object value, final Integer expireSeconds,
                            final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        Long result = connection.sAdd(byteKey, serialize(value));
        if (NumUtil.intValue(expireSeconds) != -1) {
            connection.expire(byteKey, expireSeconds);
        }
        List<Object> list = connection.exec();
        if (CollectionUtils.isNotEmpty(list)) {
            return (Long) list.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static Long scard(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(key);
                    Long result = connection.sCard(byteKey);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> smembers(final String key, Class<T> valClz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        return (Set<T>) redisTemplate.execute(new RedisCallback<Set<T>>() {
            @Override
            public Set<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(key);
                    Set<byte[]> values = connection.sMembers(byteKey);
                    if (values == null) {
                        return null;
                    }
                    Set<T> result = new HashSet<T>();
                    for (byte[] value : values) {
                        T obj = unserialize(value, valClz);
                        result.add(obj);
                    }
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static Boolean hset(final String region, final String field,
                               final Object value, final Integer expireSeconds) {
        if (StringUtils.isEmpty(region) || StringUtils.isEmpty(field)) {
            throw new RedisExecption("region或者key不能为空");
        }
        if (value == null) {
            return false;
        }
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey;
                try {
                    byteKey = serialize(region);
                    connection.multi();
                    Boolean result = connection.hSet(byteKey,
                            serialize(field),
                            serialize(value));
                    if (NumUtil.intValue(expireSeconds) != -1) {
                        connection.expire(byteKey, expireSeconds);
                    }
                    List<Object> list = connection.exec();
                    if (CollectionUtils.isNotEmpty(list)) {
                        return (Boolean) list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    connection.discard();
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static Boolean hsetnx(final String region, final String field,
                                 final Object value, final Integer expireSeconds) {
        if (StringUtils.isEmpty(region) || StringUtils.isEmpty(field)) {
            throw new RedisExecption("region或者key不能为空");
        }
        if (value == null) {
            return false;
        }
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey;
                try {
                    byteKey = serialize(region);
                    connection.multi();
                    connection.hSetNX(byteKey,
                            serialize(field),
                            serialize(value));
                    if (NumUtil.intValue(expireSeconds) != -1) {
                        connection.expire(byteKey, expireSeconds);
                    }
                    List<Object> list = connection.exec();
                    if (CollectionUtils.isNotEmpty(list)) {
                        return (Boolean) list.get(0);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    connection.discard();
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Integer hincrBy(final String region, final String field,
                                  final Object value) {
        if (StringUtils.isEmpty(region) || StringUtils.isEmpty(field)) {
            throw new RedisExecption("region或者key不能为空");
        }
        if (value == null) {
            return null;
        }
        return (Integer) redisTemplate.execute(new RedisCallback<Integer>() {
            @Override
            public Integer doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return hincrBy(region, field, connection);
            }


        }, true);
    }

    public static Integer hincrBy(final String region, final String field, final RedisConnection connection) {
        byte[] byteKey;
        try {
            byteKey = serialize(region);
            Long val = connection.hIncrBy(byteKey, serialize(field), 1L);
            if (val == null) {
                return null;
            } else {
                return val.intValue();
            }
        } catch (Exception e) {
            connection.discard();
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> lrange(final String key, Integer start, Integer end, Class<T> clz) {
        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey = serialize(key);
                List<byte[]> values = connection.lRange(byteKey, start, end);
                List<T> result = new ArrayList<T>();
                for (byte[] value : values) {
                    T obj = unserialize(value, clz);
                    result.add(obj);
                }
                return result;
            }

        }, true);
    }

    @SuppressWarnings("unchecked")
    public static Long lpush(final String key,
                             final Object value, final Integer expireSeconds) {
        return lpush(key, value, null, expireSeconds);
    }

    @SuppressWarnings("unchecked")
    public static Long lpush(final String key,
                             final Object value, final Integer maxCount, final Integer expireSeconds) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        if (value == null) {
            return 0L;
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return lpush(key, value, maxCount, expireSeconds, connection);
            }

        }, true);
    }

    public static Long lpush(final String key, final Object value,
                             final Integer expireSeconds, final RedisConnection connection) {
        return lpush(key, value, null, expireSeconds, connection);
    }

    public static Long lpush(final String key, final Object value, final Integer maxCount,
                             final Integer expireSeconds, final RedisConnection connection) {
        byte[] byteKey;
        try {
            boolean isExec = true;
            byteKey = serialize(key);
            if (maxCount != null) {
                int max = maxCount == null ? 0 : maxCount;

                if (max > 0) {
                    if (connection.lLen(byteKey) > max) {
                        isExec = false;
                    } else {
                        isExec = true;
                        ;
                    }
                }
            }
            if (isExec) {
                Long result = connection.lPush(byteKey, serialize(value));
                if (expireSeconds != -1) {
                    connection.expire(byteKey, expireSeconds);
                }
                return result;
            }
            return 0L;
            //connection.expire(byteKey, expireSeconds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public static Long rpush(final String key,
                             final Object value, final Integer expireSeconds) {
        return rpush(key, value, null, expireSeconds);
    }

    @SuppressWarnings("unchecked")
    public static Long rpush(final String key,
                             final Object value, final Integer maxCount, final Integer expireSeconds) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }
        if (value == null) {
            return 0L;
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return rpush(key, value, maxCount, expireSeconds, connection);
            }

        }, true);
    }

    public static Long rpush(final String key, final Object value,
                             final Integer expireSeconds, final RedisConnection connection) {
        return rpush(key, value, null, expireSeconds, connection);
    }

    public static Long rpush(final String key, final Object value, final Integer maxCount,
                             final Integer expireSeconds, final RedisConnection connection) {
        byte[] byteKey;
        try {
            boolean isExec = true;
            byteKey = serialize(key);
            if (maxCount != null) {
                int max = maxCount == null ? 0 : maxCount;

                if (max > 0) {
                    if (connection.lLen(byteKey) > max) {
                        isExec = false;
                    } else {
                        isExec = true;
                        ;
                    }
                }
            }
            if (isExec) {
                Long result = connection.rPush(byteKey, serialize(value));
                if (expireSeconds != -1) {
                    connection.expire(byteKey, expireSeconds);
                }
                return result;
            }
            return 0L;
            //connection.expire(byteKey, expireSeconds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T rpop(final String key, final Class<T> valClz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("region或者key不能为空");
        }
        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return rpop(key, valClz, connection);
            }

        }, true);
    }

    public static <T> T rpop(final String key, final Class<T> valClz, final RedisConnection connection) {
        byte[] byteKey;
        try {
            byteKey = serialize(key);
            byte[] result = connection.rPop(byteKey);
            return unserialize(result, valClz);
            //connection.expire(byteKey, expireSeconds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T lget(final String key, Integer i, final Class<T> valClz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("region或者key不能为空");
        }
        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey;
                try {
                    byteKey = serialize(key);
                    List<byte[]> result = connection.lRange(byteKey, i, i + 1);
                    T t = null;
                    if (result != null && result.size() != 0) {
                        t = unserialize(result.get(0), valClz);
                    }
                    return t;
                    //connection.expire(byteKey, expireSeconds);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Long llen(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("region或者key不能为空");
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey;
                try {
                    byteKey = serialize(key);
                    Long result = connection.lLen(byteKey);
                    return result;
                    //connection.expire(byteKey, expireSeconds);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Long hlen(final String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("region或者key不能为空");
        }
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKey;
                try {
                    byteKey = serialize(key);
                    Long result = connection.hLen(byteKey);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }


    @SuppressWarnings("unchecked")
    public static <T> List<T> mget(final Set<String> keys, final Class<T> valClz) {
        if (CollectionUtils.isEmpty(keys)) {
            throw new RedisExecption("keys不能为空");
        }

        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    List<T> result = mget(keys, valClz, connection);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static <T> List<T> mget(final Set<String> keys, final Class<T> valClz, final RedisConnection connection) {
        if (keys == null || keys.size() == 0) {
            return null;
        }
        byte[][] bkeys = new byte[keys.size()][];
        int i = 0;
        for (String key : keys) {
            byte[] byteKey = serialize(key);
            bkeys[i++] = byteKey;
        }
        List<byte[]> byteValues = connection.mGet(bkeys);
        if (byteValues == null || byteValues.size() == 0) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (byte[] byteValue : byteValues) {
            T value = unserialize(byteValue, valClz);
            list.add(value);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(final String key, final Type valClz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }

        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    T result = get(key, valClz, connection);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static <T> T get(final String key, final Type valClz, final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        byte[] byteValue = connection.get(byteKey);
        T result = unserialize(byteValue, valClz);
        return result;
    }


    @SuppressWarnings("unchecked")
    public static <T> T get(final String key, final Class<T> valClz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisExecption("key不能为空");
        }

        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    T result = get(key, valClz, connection);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }

    public static <T> T get(final String key, final Class<T> valClz, final RedisConnection connection) {
        byte[] byteKey = serialize(key);
        byte[] byteValue = connection.get(byteKey);
        T result = unserialize(byteValue, valClz);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T hget(final String region, final String field, final Class<T> valClz) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }
        if (field == null) {
            throw new RedisExecption("field不能为空");
        }

        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(region);
                    byte[] byteValue = connection.hGet(byteKey,
                            serialize(field));

                    T result = unserialize(byteValue, valClz);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static boolean hExists(final String region, final String field) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }
        if (field == null) {
            throw new RedisExecption("field不能为空");
        }

        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(region);
                    boolean bool = connection.hExists(byteKey,
                            serialize(field));

                    return bool;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> hmget(final String region, final Set<String> fields, final Type valClz) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }
        if (CollectionUtils.isEmpty(fields)) {
            throw new RedisExecption("field不能为空");
        }

        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(region);

                    byte[][] bfields = new byte[fields.size()][];
                    int i = 0;
                    for (String key : fields) {
                        byte[] bytefield = serialize(key);
                        bfields[i++] = bytefield;
                    }

                    List<byte[]> byteValues = connection.hMGet(byteKey,
                            bfields);
                    if (byteValues == null || byteValues.size() == 0) {
                        return null;
                    }
                    List<T> list = new ArrayList<T>();
                    for (byte[] byteValue : byteValues) {
                        T value = unserialize(byteValue, valClz);
                        list.add(value);
                    }

                    return list;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> hmget(final String region, final Set<String> fields, final Class<T> valClz) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }
        if (CollectionUtils.isEmpty(fields)) {
            throw new RedisExecption("field不能为空");
        }

        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            @Override
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = serialize(region);

                    byte[][] bfields = new byte[fields.size()][];
                    int i = 0;
                    for (String key : fields) {
                        byte[] bytefield = serialize(key);
                        bfields[i++] = bytefield;
                    }

                    List<byte[]> byteValues = connection.hMGet(byteKey,
                            bfields);
                    if (byteValues == null || byteValues.size() == 0) {
                        return null;
                    }
                    List<T> list = new ArrayList<T>();
                    for (byte[] byteValue : byteValues) {
                        T value = unserialize(byteValue, valClz);
                        list.add(value);
                    }

                    return list;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Set<String> hkeys(final String region) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }

        return (Set<String>) redisTemplate.execute(
                new RedisCallback<Set<String>>() {
                    @Override
                    public Set<String> doInRedis(
                            final RedisConnection connection)
                            throws DataAccessException {
                        try {
                            byte[] byteKey = serialize(region);
                            Long start = System.currentTimeMillis();
                            Map<byte[], byte[]> byteMap = connection
                                    .hGetAll(byteKey);
                            if (byteMap == null || byteMap.size() == 0) {
                                return null;
                            }
                            Set<byte[]> set = byteMap.keySet();
                            Set<String> keys = new HashSet();
                            for (byte[] key : set) {
                                keys.add((String) unserialize(key, null));
                            }
                            return keys;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> hgetAll(final String region, final Class<T> valueClz) {
        if (StringUtils.isEmpty(region)) {
            throw new RedisExecption("key不能为空");
        }

        return (Map<String, T>) redisTemplate.execute(
                new RedisCallback<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> doInRedis(
                            final RedisConnection connection)
                            throws DataAccessException {
                        try {
                            byte[] byteKey = serialize(region);
                            Long start = System.currentTimeMillis();
                            Map<byte[], byte[]> byteMap = connection
                                    .hGetAll(byteKey);
                            if (byteMap == null || byteMap.size() == 0) {
                                return null;
                            }
                            Set<byte[]> set = byteMap.keySet();
                            Map<String, Object> result = new HashMap<String, Object>();
                            for (byte[] key : set) {
                                result.put(
                                        (String) unserialize(key, null),
                                        unserialize(byteMap.get(key), valueClz));
                            }
                            return result;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, true);
    }

    @SuppressWarnings("unchecked")
    public static Long del(final String key) throws RedisExecption {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return del(key, connection);
            }
        }, true);
    }

    public static Long del(final String key, final RedisConnection connection) {
        try {
            return connection.del(serialize(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Long delObj(final String key) throws RedisExecption {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                return delObj(key, connection);
            }
        }, true);
    }

    public static Long delObj(final String key, final RedisConnection connection) {
        try {
            return connection.del(redisTemplate.getKeySerializer().serialize(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Long hdel(final String region, final Object field)
            throws RedisExecption {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return connection.hDel(serialize(region),
                            serialize(field));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Long delStr(final String key) throws RedisExecption {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return connection.del(serialize(key));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    @SuppressWarnings("unchecked")
    public static Long decr(final String key, Integer seconds) {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                connection.multi();
                decr(key, seconds, connection);
                List<Object> list = connection.exec();
                if (CollectionUtils.isNotEmpty(list)) {
                    return (Long) list.get(0);
                } else {
                    return null;
                }
            }


        }, true);
    }

    public static Long decr(final String key) {
        return incr(key, -1);
    }

    public static Long decr(final String key, Integer seconds, final RedisConnection connection) {
        try {

            byte[] byteKey = serialize(key);
            Long result = connection.decr(byteKey);
            if (-1 != seconds) {
                connection.expire(serialize(key), seconds);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public static Long incr(final String key, Integer seconds) {
        return (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                connection.multi();
                incr(key, seconds, connection);
                List<Object> list = connection.exec();
                if (CollectionUtils.isNotEmpty(list)) {
                    return (Long) list.get(0);
                } else {
                    return null;
                }
            }


        }, true);
    }

    public static Long incr(final String key, Integer seconds, final RedisConnection connection) {
        try {

            byte[] byteKey = serialize(key);
            Long result = connection.incr(byteKey);
            if (-1 != seconds) {
                connection.expire(serialize(key), seconds);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Long incr(final String key) {
        return incr(key, -1);
    }

    @SuppressWarnings("unchecked")
    public static Boolean setnxStr(final String key, final String value) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return connection.setNX(serialize(key), serialize(value));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static void setStr(final String key, final String value, final Integer expireSeconds) {
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    if (NumUtil.intValue(expireSeconds) == -1) {
                        connection.set(serialize(key), serialize(value));
                    } else {
                        connection.setEx(serialize(key), expireSeconds, serialize(value));
                    }

                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    public static void setStr(final String key, final String value) {
        setStr(key, value, -1);
    }

    @SuppressWarnings("unchecked")
    public static Boolean isExists(final String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    Boolean flag = connection.exists(serialize(key));
                    return flag;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    /**
     * 获取hash键集合
     *
     * @param region 一级键
     * @return
     * @throws RedisException
     */
    @SuppressWarnings("unchecked")
    public static Set<String> keys(final String pattern) {
        return (Set<String>) redisTemplate.execute(new RedisCallback<Set<String>>() {
            public Set<String> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    Set<byte[]> keybytes = connection.keys(serialize(pattern));
                    if (keybytes == null || keybytes.isEmpty()) {
                        return Collections.emptySet();
                    }
                    Set<String> set = new HashSet<String>();
                    for (byte[] keybyte : keybytes) {
                        set.add(unserialize(keybyte, String.class));
                    }
                    return set;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    /**
     * 获取hash键集合
     *
     * @param region 一级键
     * @return
     * @throws RedisException
     */
    @SuppressWarnings("unchecked")
    public static Set<String> scan(final String pattern, int count) {
        return (Set<String>) redisTemplate.execute(new RedisCallback<Set<String>>() {
            public Set<String> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                Cursor<byte[]> cursor = null;
                try {
                    Set<String> set = new HashSet<String>();
                    ScanOptions options = new ScanOptions.ScanOptionsBuilder().match(pattern).count(count).build();
                    cursor = connection.scan(options);
                    if (cursor == null) {
                        return Collections.emptySet();
                    }
                    int i = 0;
                    while (cursor.hasNext() && i < count) {
                        i++;
                        set.add(unserialize(cursor.next(), String.class));

                    }
                    return set;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    /**
     * 获取hash值集合
     *
     * @param region 一级键
     * @return
     * @throws RedisException
     */
    @SuppressWarnings("")
    public static List<Object> values(final String pattern) {
        return values(pattern, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> values(final String pattern, Class<T> clz) {
        return (List<T>) redisTemplate.execute(new RedisCallback<List<T>>() {
            public List<T> doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    Set<byte[]> keys = connection.keys(serialize(pattern));
                    if (keys != null && !keys.isEmpty()) {
                        List<T> values = new ArrayList<T>(keys.size());
                        for (byte[] key : keys) {
                            T value = unserialize(connection.get(key), clz);
                            if (value != null) {
                                values.add(value);
                            }
                        }
                        return Collections.unmodifiableList(values);
                    } else {
                        return Collections.emptyList();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }


    public static byte[] serialize(Object value) {
        String valStr = getJsonStr(value);
        return redisTemplate.getStringSerializer().serialize(valStr);
    }

    public static <T> T unserialize(byte[] bytes, Class<T> clz) {
//		String valStr = getJsonStr);

        Object json = redisTemplate.getStringSerializer().deserialize(bytes);
        if (json == null) {
            return null;
        }
        if (clz == null || clz.getName().equals("java.lang.String")) {
            return (T) json;
        } else {
            return gson.fromJson(json.toString(), clz);
        }
    }

    /**
     * private static java.lang.reflect.Type imgType = new TypeToken<ArrayList<T>>() {}.getType();
     *
     * @param bytes
     * @param type
     * @return
     */
    public static <T> T unserialize(byte[] bytes, Type type) {
//		String valStr = getJsonStr);

        Object json = redisTemplate.getStringSerializer().deserialize(bytes);
        if (json == null) {
            return null;
        }
        if (type == null) {
            return (T) json;
        } else {
            return gson.fromJson(json.toString(), type);
        }
    }


    public static void main(String[] args) {
        RedisTemplate t = new RedisTemplate();


//		InputStream is = RedisHelper.class.getClassLoader().getResourceAsStream("redis.properties");
        Properties prop = new Properties();
        String hostname = "";
        String password = "";
        String port = "";
//		try {
//			prop.load(is);
        password = "WOprjS3K4hURTlbe";

//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


        /***热备start***/
//			String sentinelHost = "localhost";
//			int sentinelport = 26379;
//			RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration();
//			sentinelConfiguration.setMaster("mymaster");
//			sentinelConfiguration.sentinel(sentinelHost, sentinelport);
//			JedisConnectionFactory connectionFactory = new JedisConnectionFactory(sentinelConfiguration);
        /***热备end***/


        /***单机start***/
//			hostname = "120.25.226.230";
        hostname = "localhost";
        port = "6379";
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setPassword(password);
        connectionFactory.setHostName(hostname);
        connectionFactory.setPort(Integer.parseInt(port));
        connectionFactory.setDatabase(1);
        /***单机end***/
        JedisPoolConfig config = new redis.clients.jedis.JedisPoolConfig();
        config.setMaxTotal(5000);
        config.setMaxIdle(200);
        config.setMaxWaitMillis(5000);
        config.setTestOnBorrow(true);

        connectionFactory.afterPropertiesSet();
        t.setConnectionFactory(connectionFactory);
        connectionFactory.setPoolConfig(config);
        t.afterPropertiesSet();
        RedisHelper.redisTemplate = t;
        String key = "hehehe";
//		for(int i = 1;i<11;i++){
//			RedisHelper.lpush(key, i, 60);
//		}
        System.out.println(RedisHelper.lrange(key, -1, -1, Integer.class));

        Long start = System.currentTimeMillis();
//		Set<String> keys = RedisHelper.keys("meta_pid_channelCode_type_ishot:452*");
//		List metas = new ArrayList<>();
//		Set <String> ids = new HashSet<String>();
//		for(String key:keys){
//			String id = StringUtils.substringAfterLast(key, "_");
//			ids.add(id);
//		}
//		metas = RedisHelper.mget(ids, null);
//		System.out.println(System.currentTimeMillis() -start);
        System.out.println();
//		RedisHelper.getRedisTemplate().execute(new RedisCallback<Boolean>() {
//			@Override
//			public Boolean doInRedis(final RedisConnection connection)
//					throws DataAccessException {
//				Long start = System.currentTimeMillis();
//				connection.multi();
//				for(int i=0;i<1000;i++){
//				RedisHelper.zadd("testSortSet21:", "好的"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "坏人"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "好人"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "坏蛋"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "好好好"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "1212"+i, 20000,connection);
//				RedisHelper.zadd("testSortSet21:", "呵呵"+i, 20000,connection);
//				}
//				connection.exec();
//				System.out.println("1======="+(System.currentTimeMillis()-start));
//				List <String> set2 =RedisHelper.zrange("testSortSet21:", 0, 7, String.class,connection);
//				System.out.println("2======="+(System.currentTimeMillis()-start));
//				Integer i = RedisHelper.zrank("testSortSet21:","呵呵900");
//				System.out.println("3======="+(System.currentTimeMillis()-start));
//				RedisHelper.zrem("testSortSet21:", "呵呵901");
//				System.out.println("4======="+(System.currentTimeMillis()-start));
//				Integer i2 = RedisHelper.zrank("testSortSet21:","呵呵901");
//				System.out.println("5======="+(System.currentTimeMillis()-start));
//				return false;
//			}
//		});
//		List <String> set1 = RedisHelper.zrangeByLex("testSortSet:", String.class);
//		List <String> set2 =RedisHelper.zrange("testSortSet:", 0, 7, String.class);
//		
//		Integer i = RedisHelper.zrank("testSortSet:","呵呵");
//		Integer i2 = RedisHelper.zrank("testSortSet:","呵");
        System.out.println();


        //	Set<String> keys = RedisHelper.scan("AirportCode*", 2);
		
	/*	final CountDownLatch latch  = new CountDownLatch(1);
		for(int i = 0;i<500;i++){
			final int i_ = i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						
						latch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long start = System.currentTimeMillis();
					System.out.println(RedisHelper.getStr("Crawl_HttpClient:AirChina_Imme_ConnectTimeOut")+";"+i_+";"+(System.currentTimeMillis()-start)+"ms");
					
				}
			}).start();
		}
		latch.countDown();*/

//		RedisHelper.set("2", "3");
//		RedisCache<String, Session> cache = new RedisCache<String,Session>("shiro-activeSessionCache",1000L);
//		Session jlbtrip = cache.get("7ea27377-1776-4c4b-91e1-9c788bc29cc2");
//		System.out.println(jlbtrip);
    }


    @SuppressWarnings("unchecked")
    public static String getStr(final String key) {
        return (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    return (String) redisTemplate.getStringSerializer().deserialize(
                            connection.get(redisTemplate.getStringSerializer().serialize(key)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, true);
    }

    /**
     * @throws
     * @Title: getObj
     * @Description: 跟set, setnx配对使用, 得到key里面的键值, 并转化为valClz类型
     * @param: @param key 键
     * @param: valClz 值类型
     * @return: T
     * @author lyq
     * @Date 2017年1月6日 下午3:56:18
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObj(final Object key) {
        if (key == null) {
            throw new RedisExecption("key不能为空");
        }
        return (T) redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(final RedisConnection connection)
                    throws DataAccessException {
                try {
                    byte[] byteKey = redisTemplate.getKeySerializer().serialize(key);
                    byte[] byteValue = connection.get(byteKey);
                    T result = (T) redisTemplate.getValueSerializer().deserialize(byteValue);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }, true);
    }


    private static String getJsonStr(Object value) {
        String valStr = null;
        if (value == null) {
            valStr = null;
        }

        if (value instanceof String) {
            valStr = value.toString();
        } else if (value instanceof Number) {
            valStr = value + "";
        } else {
            valStr = gson.toJson(value);
        }
        return valStr;
    }


    @SuppressWarnings("rawtypes")
    public static RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }


    @SuppressWarnings({"rawtypes", "static-access"})
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}