package com.test;

import org.apache.commons.lang3.StringUtils;

import com.framework.shard.table.policy.SplitTablePolicy;

public class TestSplitTablePolicy implements SplitTablePolicy {

	@Override
	public String getShardTableExpression(String hitValue, String tableExpression) {
		if(StringUtils.isEmpty(hitValue)){
			return null;
		}
		String hit = StringUtils.substring(hitValue, 0, 2);
		return new StringBuffer(hit).append("_{{").append(tableExpression).append("}}").toString();
	}
}
