package com.framework.security;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.spring.utils.SpringContextUtil;




/**
 * @ClassName:     ZkRoleUriUpdateWatcher.java
 * @Description:   权限修改发布 
 * @author         Administrator
 * @version        V1.0  
 * @Date           2017年1月5日 下午5:07:34 
 */
public class ZkRoleUriUpdateWatcher implements Watcher,ZkPromulgator<String>  {
	private static final Logger logger = LoggerFactory.getLogger(ZkRoleUriUpdateWatcher.class);
	private ZooKeeper zk = null;
	private String hosts = null;
	private Integer timeout = 5000;
	 CountDownLatch connectedLatch = null;
	 
	 private boolean zkEnabled = true;
	
	//应用识别uuid
	private final static String uuid = UUID.randomUUID().toString();
	private String path = "/shiro_role_uri";
	int version = 0 ;
	public ZkRoleUriUpdateWatcher(String hosts){
		this(hosts,true);
	}
	public ZkRoleUriUpdateWatcher(String hosts,boolean zkEnabled){
		
	/*	InputStream is = ZkRoleUriUpdateWatcher.class.getClassLoader().getResourceAsStream("hostinfo.properties");
		Properties prop = new Properties();
		String path = "";
		try {
			prop.load(is);
			path = prop.getProperty("hostinfo.path");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			this.zkEnabled = zkEnabled;
			if(zkEnabled){
				this.hosts = hosts;
				newZk();
				if(zk.exists(path, false)==null){
					zk.create(path, uuid.getBytes(),  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					zk.exists(path, true);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void publish(String msg ) throws KeeperException, InterruptedException, IOException{
	/*	InputStream is = SecurityFilterUtil.class.getClassLoader().getResourceAsStream("hostinfo.properties");
		Properties prop = new Properties();
		prop.load(is);*/
		byte[] bytes = (uuid+"_"+msg).getBytes();
		/*path = prop.getProperty("hostinfo.path");	
		String hostname = prop.getProperty("hostinfo.hostname");
		System.out.println("hostname:"+hostname);
		String password = prop.getProperty("hostinfo.password");
		System.out.println("password:"+password);*/
		try{
			if(zk.exists(path, true)==null){
				zk.create(path, bytes,  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			zk.setData(path, bytes, -1);
		}catch(SessionExpiredException e){
			zk.close();
			try {
				newZk();
				zk.exists(path, true);
				zk.setData(path, bytes, -1);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
	
	/** 
	 * @Title:        process 
	 * @Description:  zookeeper监听执行方法 
	 * @param:        @param event        
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:07:56 
	 */
	@Override
	public void process(WatchedEvent event) {
		try {
			 if (event.getState() == KeeperState.SyncConnected&&connectedLatch!=null ) { 
				 //zookeeper链接成功，释放 ：waitUntilConnected
	               connectedLatch.countDown();  
	           }  
			if(event.getPath()==null){
				return ;
			}
			byte[] data = zk.getData(event.getPath(), false, null);
			String dataStr = new String(data);
			String []msg = StringUtils.split(dataStr ,"_");
			if(msg==null||msg.length<2){
				throw new RuntimeException("::监听zookeeper接受消息体错误，msg正确格式：ermoteUuid_username，错误msg："+dataStr);
			}
			String remoteUuid = msg[0];
			String username = msg[1];
			
			if(!uuid.equals(remoteUuid)){
				if(msg.length==4){
					String uri = msg[3];
					String roleCode = msg[2];
					RoleManager.zkDelRoleUri(roleCode, uri);
				}
				LoadAuthorization load = SpringContextUtil.getBean(LoadAuthorization.class);
				logger.info("机器："+uuid+" 修改权限，修改shiro拦截器");
				load.load(username);
			}else{
				logger.info("通过zookeeper监听到本机："+uuid+"正在发布修改权限任务，拦截器曾被修改，修改跳过。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	 /** 
	 * @Title:        waitUntilConnected 
	 * @Description:  等待zooKeeper连接成功 
	 * @param:        zooKeeper    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:08:10 
	 */
	private  void waitUntilConnected() {  
	        if (States.CONNECTING == zk.getState()) {  
	            try {  
	                connectedLatch.await();  
	            } catch (InterruptedException e) {  
	                throw new IllegalStateException(e);  
	            }  
	        }  
	}  
	 /** 
	 * @Title:        newZk 
	 * @Description:  新建zookeeper链接对象 
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @throws 
	 * @author        join
	 * @Date          2017年1月5日 下午5:08:30 
	 */
	private void newZk() throws IOException{
		 connectedLatch = new CountDownLatch(1);
		 zk = new ZooKeeper(hosts, timeout, this);
		 waitUntilConnected();
	 }


	public boolean isZkEnabled() {
		return zkEnabled;
	}


	public void setZkEnabled(boolean zkEnabled) {
		this.zkEnabled = zkEnabled;
	}
	
	
}