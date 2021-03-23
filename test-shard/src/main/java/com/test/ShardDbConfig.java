package com.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.framework.shard.AbsShardDbConfig;

//import com.framework.shard.AbsShardDbConfig;


@Configuration
//@PropertySource("shard-db.properties")
public class ShardDbConfig extends AbsShardDbConfig {}
//public class ShardDbConfig {}
