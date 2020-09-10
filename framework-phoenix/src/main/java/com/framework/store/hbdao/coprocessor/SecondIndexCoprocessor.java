package com.framework.store.hbdao.coprocessor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;

/**
 * 协处理器,暂时不建议使用，需要用二级索引通过普通的程序建
 * @author lyq
 * @date 2020年8月31日 下午6:35:07
 */
public class SecondIndexCoprocessor extends BaseRegionObserver  {
	@Override
	  public void prePut(final ObserverContext<RegionCoprocessorEnvironment> e, 
	      final Put put, final WALEdit edit, final Durability durability) throws IOException {
	Configuration conf = new Configuration();
//	conf.set(name, value)
	//需要设置conf....
	HTable table = new HTable(conf, "indexTableName");
	  List<Cell> kv = put.get("familyName".getBytes(), "columnName".getBytes());
	 Iterator<Cell> kvItor = kv.iterator();
	 while (kvItor.hasNext()) {
		 Cell tmp = kvItor.next();
		 Put indexPut = new Put(tmp.getValue());
		 indexPut.add("familyName".getBytes(), "columnName".getBytes(), tmp.getRow());
		 table.put(indexPut);
	 }
	 table.close();
	 }

	
}
