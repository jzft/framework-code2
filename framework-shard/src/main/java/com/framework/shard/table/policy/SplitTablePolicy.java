package com.framework.shard.table.policy;

public interface SplitTablePolicy {

	/**
	 * 获取物理名称
	 * @param hitValue 数据库scope 命中值
	 * @param logicTableName 逻辑表名称
	 * @return 物理表名表达式，如：01_{{table}}
	 */
	String getShardTableExpression (String hitValue,String logicTableName);
}
