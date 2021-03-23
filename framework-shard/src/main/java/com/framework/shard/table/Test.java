package com.framework.shard.table;

import java.util.ArrayList;
import java.util.List;

import com.framework.shard.table.parse.CreateTableSqlParser;
import com.framework.shard.table.parse.DeleteSqlParser;
import com.framework.shard.table.parse.InsertSqlParser;
import com.framework.shard.table.parse.SelectSqlParser;
import com.framework.shard.table.parse.UpdateSqlParser;
//import com.framework.utils.RegUtil;

public class Test {

public static void main(String[] args) {
		
		InsertSqlParser insert = new InsertSqlParser();
		UpdateSqlParser update = new UpdateSqlParser();
		DeleteSqlParser delete = new DeleteSqlParser();
		SelectSqlParser select = new SelectSqlParser();
		CreateTableSqlParser createTable = new  CreateTableSqlParser();
		String selectSql = "select * from ddd.aaa a , bbb e,ccc a,bbb left JOIN ddd.bbb b on a.ddd = b.ccc" ;
//		String selectSql = "select * from ddd.aaa a left JOIN ddd.bbb b on a.ddd = b.ccc" ;
		String insertSql = "insert into ssss(aaaa) values() ";
		String deleteSql = "delete from aaa s where aa = aa";
		String updateSql = "update low_priority aaaa set aa=ddd,bb=ddd";
		String createTableSql = "create temporary table if not exists tbl_name";
//		System.out.println(a.replaceAll(sql, selectRegex, " $1 $2_2"));
		String [] expressions = {"01_{{aaa}}","01_{{bbb}}"};
		System.out.println(insert.genSql(insertSql,expressions));
		System.out.println(update.genSql(updateSql,expressions));
		System.out.println(select.genSql(selectSql,expressions));
		System.out.println(delete.genSql(deleteSql,expressions));
		System.out.println(createTable.genSql(createTableSql,expressions));
		
		
//		System.out.println(a.replaceAll(insertSql, insertRegex, " $1$2$3 $4_2"));
//		
//		System.out.println(a.replaceAll(deleteSql, deleteRegex, " $1$2_2"));
//		
//		System.out.println(a.replaceAll(updateSql, updateRegex, " $1 $2$3_2"));
//		System.out.println(selectSql);
//		ArrayList<List<String>> list = RegUtil.retrieveLinkss(selectSql, select.sqlRegex);
//		for(List<String>cells : list){
//			System.out.println("-----------------------------");
//			for(String cell:cells){
//				System.out.println(cell);
//			}
//		}
	}
}
