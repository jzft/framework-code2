package com.framework.sql.generator;

import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * date 2020/6/17 下午1:47
 *
 * @author casper
 **/
public class DefaultSqlScriptParser implements SqlScriptParser {
    /**
     * 解析sql脚本文件并返回一个sql语句的迭代对象
     *
     * @param filename 文件完整路径名称
     * @return
     */
    @Override
    public Iterator<String> parse(String filename) {
        List<String> contains = new LinkedList<>();
        if (!StringUtils.isEmpty(filename)) {
            contains.add(filename);
        }
        return contains.iterator();
    }


}
