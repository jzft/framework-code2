package com.framework.shard.table.parse;

public class DeleteSqlParser extends AbsSqlParser {
	public DeleteSqlParser(){
		super.sqlRegex = "(\\s+?from\\s+?)([\\S]+\\s*?)";
	}
	@Override
	public String genSql(String logicSql,String [] tableExpressions) {
		// TODO Auto-generated method stub
		String sql2 = super.replaceAll(logicSql, " $1{{$2}}");
		sql2 = super.genSql2(sql2,tableExpressions);
		return sql2;
	}
}
