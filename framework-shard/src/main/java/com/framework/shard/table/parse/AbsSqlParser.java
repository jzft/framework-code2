package com.framework.shard.table.parse;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;



public abstract class AbsSqlParser {

	public String sqlRegex ;
	
	public String replaceAll(String str, String replacement) {
		return replaceAll(str,sqlRegex,replacement);
	}
	
	protected String replaceAll(String str,String regex, String replacement) {
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE)
		.matcher(str);
		String reg_s = matcher.replaceAll(replacement);
		
	return reg_s;
	}
	
	/**
	 * 拆解表达式
	 * @param tableExpression
	 * @return
	 */
	private String[] disassemblyExpression(String tableExpression){
 		String regex = "(.*)(\\{\\{(.*)\\}\\})(.*)";
 		
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE)
				.matcher(tableExpression);
		String [] tt = null;
		if (matcher.find()) {
			tt = new String[4];
			tt[0] = matcher.group(1);//start
			String tableRegex = "\\{\\{(\\s*|[^\\}]*[\\.,\\s])"+matcher.group(3)+"(\\s*\\,[^\\}]*|\\s*)\\}\\}";
//			tt[1] = matcher.group(2);//Expression
			tt[1] = tableRegex;//Expression
			tt[2] = matcher.group(3);//table
			tt[3] = matcher.group(4);
		}else{
			throw new SqlParserException(tableExpression+"格式错误,请设置格式hit_{{tablename}}或{{tablename}}_hit");
		}
		return tt;
	}
	
	private Set<String> getSqlTables(String expressionSql){
		String regex = "\\{\\{([^\\{\\}]*)\\}\\}";
		Set<String> list = new TreeSet<String>();
		Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE + Pattern.DOTALL + Pattern.MULTILINE)
				.matcher(expressionSql);
		if (matcher.find()) {
			list.add(matcher.group(0));
		}
		return list;
	}
	
	protected String genSql2(String sql,String[] tableExpressions) {
		if(ArrayUtils.isNotEmpty(tableExpressions)){
			Set <String> sqlTables = getSqlTables(sql);
			  java.util.Iterator<String> it1 = sqlTables.iterator();
				while(it1.hasNext()){
					String table = it1.next();
					for(String expression:tableExpressions){
						String[] cells = this.disassemblyExpression(expression);
						String tableExpression = new StringBuffer("([\\s\\.,\\{])").append(cells[2]).append("([\\s,\\}])").toString();
						
						String table2 = replaceAll(table, tableExpression, new StringBuffer("$1").append(cells[0]).append(cells[2]).append(cells[3]).append("$2").toString());
						sql = StringUtils.replace(sql, table, table2);
						table = table2;
	//					sql = this.replaceAll(sql, cells[1], new StringBuffer("$1").append(cells[0]).append(cells[2]).append(cells[3]).append("$2").toString());
					}
			}
			
		}
		sql = replaceAll(sql,"\\{\\{([^\\}]+)\\}\\}", "$1");
		return sql;
	}
	protected String selectRegex = "\\s+?(from|join)\\s+?([\\S]+[^,]*(\\s*,\\s*[\\S]+\\s?[^,\\s]*)+\\s*?|[\\S]+\\s*?)";
//	static String tableRegex = "(.+)\\s+?(from|join)\\s+?(.+?(\\s*?,\\s*?.+?)+?\\s*?|.+?\\s*?)";
//	static String selectRegex = "\\s+?(from|join)\\s+?([\\S]+(\\s*?,\\s*?[\\S]+)+\\s*?|[\\S]+\\s*?)";
//	static String insertRegex = "(insert)(\\s+delayed)?(\\s+into)?\\s+?([^\\s\\(]+)";
//	static String deleteRegex = "(\\s+?from\\s+?)([\\S]+\\s*?)";
//	static String updateRegex = "(update)(\\s+low_priority)?\\s+?([^\\s\\(]+)";
	/**
	 * 
	 * @param logicSql
	 * @param tableExpression 如：hitValue_{{tablename}} {{tablename}}_hitValue
	 * @return
	 */
	public abstract String genSql(String logicSql,String[] tableExpression);
	
	
	
}
