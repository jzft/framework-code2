package com.framework.shard.table.parse;

public class CreateTableSqlParser extends AbsSqlParser {
//	private String updateRegex = "(update)(\\s+low_priority)?\\s+?([^\\s\\(]+)";
	
	public CreateTableSqlParser(){
		super.sqlRegex = "(create)(\\s+temporary)?\\s+table\\s+(if\\s+not\\s+exists\\s+)?([^\\s\\(]+)\\s*";
	}
	
	@Override
	public String genSql(String logicSql,String [] tableExpressions) {
		// TODO Auto-generated method stub
		String sql2 = super.replaceAll(logicSql, "$1$2 table $3 {{$4}}");
		sql2 = super.genSql2(sql2,tableExpressions);
		return sql2;
	}
}
