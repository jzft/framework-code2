package com.framework.shard.table.parse;

public class UpdateSqlParser extends AbsSqlParser {
//	private String updateRegex = "(update)(\\s+low_priority)?\\s+?([^\\s\\(]+)";
	
	public UpdateSqlParser(){
		super.sqlRegex = "(update)(\\s+low_priority)?\\s+?([^\\s\\(]+)";
	}
	@Override
	public String genSql(String logicSql,String [] tableExpressions) {
		// TODO Auto-generated method stub
		String sql2 = super.replaceAll(logicSql, "$1$2 {{$3}}");
		sql2 = super.genSql2(sql2,tableExpressions);
		return sql2;
	}
	
}
