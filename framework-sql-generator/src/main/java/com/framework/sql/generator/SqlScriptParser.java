package com.framework.sql.generator;

import java.util.Iterator;

/**
 * @author casper
 * @date 2020/6/17 下午1:46
 **/
public interface SqlScriptParser {


    /**
     * 解析ddl文件并返回一个迭代对象
     *
     * @param filename 文件完整路径名称
     * @return
     */
    Iterator<String> parse(String filename);


}
