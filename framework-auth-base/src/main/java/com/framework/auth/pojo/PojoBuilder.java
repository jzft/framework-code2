package com.framework.auth.pojo;

import java.util.Collection;
import java.util.List;

/**
 * @author casper
 * @date 2020/6/29 上午11:21
 **/
public interface PojoBuilder<T, R> {


    /**
     * 构建器
     *
     * @param target 源数据
     * @return
     */
    R build(T target);

    /**
     * 批量构建器
     *
     * @param targets 源数据
     * @return
     */
    List<R> build(Collection<T> targets);


}
