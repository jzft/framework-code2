package com.framework.shard.table.parse;

public class InsertSqlParser extends AbsSqlParser {
	
	public InsertSqlParser(){
		super.sqlRegex = "(insert)(\\s+delayed)?(\\s+into)?\\s+?([^\\s\\(]+)";
	}
	@Override
	public String genSql(String logicSql,String [] tableExpressions) {
		// TODO Auto-generated method stub
		String sql2 = super.replaceAll(logicSql, "$1$2$3 {{$4}}");
		sql2 = super.replaceAll(sql2,super.selectRegex, " $1 {{$2}}");
		sql2 = super.genSql2(sql2,tableExpressions);
		return sql2;
	}
}
