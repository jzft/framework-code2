package com.framework.shard.table;

import org.apache.commons.lang3.StringUtils;

import com.framework.shard.table.parse.AbsSqlParser;
import com.framework.shard.table.parse.CreateTableSqlParser;
import com.framework.shard.table.parse.DeleteSqlParser;
import com.framework.shard.table.parse.InsertSqlParser;
import com.framework.shard.table.parse.SelectSqlParser;
import com.framework.shard.table.parse.SqlParserException;
import com.framework.shard.table.parse.UpdateSqlParser;

public class SqlParserFactory {

	public static AbsSqlParser getParser(String logicSql){
		logicSql = StringUtils.trim(logicSql);
		if(StringUtils.startsWithIgnoreCase(logicSql,"select")){
			return new SelectSqlParser();
		}else if(StringUtils.startsWithIgnoreCase(logicSql,"insert")){
			return new InsertSqlParser();
		}else if(StringUtils.startsWithIgnoreCase(logicSql,"update")){
			return new UpdateSqlParser();
		}else if(StringUtils.startsWithIgnoreCase(logicSql,"delete")){
			return new DeleteSqlParser();
		}else if(StringUtils.startsWithIgnoreCase(logicSql,"create")&&
				StringUtils.containsIgnoreCase(logicSql, " table ")){
			return new CreateTableSqlParser();
		}else{
			throw new SqlParserException("找不到 ‘"+StringUtils.substringBefore(logicSql, " ")+"’ 相关转化器。");
		}
	}
}
