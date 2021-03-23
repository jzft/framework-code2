package com.framework.shard.table.parse;

public class SelectSqlParser extends AbsSqlParser {
	
	public SelectSqlParser(){
//		super.sqlRegex = "\\s+?(from|join)\\s+?([\\S]+(\\s*?,\\s*?[\\S]+)+\\s*?|[\\S]+\\s*?)";
//		super.sqlRegex = "\\s+?(from|join)\\s+?([\\S]+[^,]*(\\s*,\\s*[\\S]+\\s?[^,\\s]*)+\\s*?|[\\S]+\\s*?)";
		super.sqlRegex = super.selectRegex;
	}
	@Override
	public String genSql(String logicSql,String [] tableExpressions) {
		// TODO Auto-generated method stub
		String sql2 = super.replaceAll(logicSql, " $1 {{$2}}");
		sql2 = super.genSql2(sql2,tableExpressions);
		return sql2;
	}
}
