package com.framework.store.hbdao.common;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Abortable;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.MetaTableAccessor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.hadoop.hbase.zookeeper.ZooKeeperWatcher;

public class HbaseAdminNew extends HBaseAdmin {
	private static final String ZK_IDENTIFIER_PREFIX =  "hbase-admin-on-";

	public HbaseAdminNew(Connection connection) throws MasterNotRunningException, ZooKeeperConnectionException {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	

	public HbaseAdminNew(Configuration c) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		super(c);
		// TODO Auto-generated constructor stub
	}


	HbaseAdminNew(ClusterConnection connection) throws MasterNotRunningException, ZooKeeperConnectionException {
		super(connection);
		// TODO Auto-generated constructor stub
	}
	  
	  public void splitRegion(final TableName tableName, String idInregionName)
	  throws IOException {
//		  System.out.println("==========tableName:"+tableName);
		    ZooKeeperWatcher zookeeper = null;
		    try {
		      checkTableExists(tableName);
		      zookeeper = new ZooKeeperWatcher(super.getConfiguration(), ZK_IDENTIFIER_PREFIX + super.getConnection().toString(),
		        new ThrowableAbortable());
		      List<Pair<HRegionInfo, ServerName>> pairs =
		        MetaTableAccessor.getTableRegionsAndLocations(zookeeper, super.getConnection(), tableName);
		      for (Pair<HRegionInfo, ServerName> pair: pairs) {
		        // May not be a server for a particular row
		        if (pair.getSecond() == null) continue;
		        HRegionInfo r = pair.getFirst();
		        // check for parents
		        if (r.isSplitParent()) continue;
		        // if a split point given, only split that particular region
		        if (!org.apache.commons.lang.StringUtils.isEmpty(idInregionName)) {
//		        	System.out.println("==========regionName:"+r.getRegionNameAsString());
		        	if(r.getRegionNameAsString().contains(idInregionName)){
		        		// call out to region server to do split now
		        		split(pair.getSecond(), pair.getFirst(), null);
		        	}
		        }
		      }
		    } finally {
		      if (zookeeper != null) {
		        zookeeper.close();
		      }
		    }
	  }
	  
	  private static class ThrowableAbortable implements Abortable {
		  @Override
		  public void abort(String why, Throwable e) {
			  throw new RuntimeException(why, e);
		  }
		  
		  @Override
		  public boolean isAborted() {
			  return true;
		  }
	  }

	  private TableName checkTableExists(final TableName tableName)
		      throws IOException {
		  if (!MetaTableAccessor.tableExists(super.getConnection(), tableName)) {
			  throw new TableNotFoundException(tableName);
		  }
		  return tableName;
	  }
		  
}
