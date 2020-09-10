package com.framework.store.hbdao;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.framework.store.hbdao.common.entity.IBaseEntity;

//@ConfigurationProperties(prefix = "phoenix")

@ConfigurationProperties(prefix = "phoenix")
@Configuration
public class HbaseCreatTableListener implements InitializingBean {
	
//	@Value("${phoenix.entitys}")
	List <String> entitys ;
	@Autowired com.framework.store.hbdao.common.BasePhoenixBaseDao basePhoenixBaseDao;
	@Override
	public void afterPropertiesSet() throws Exception {
//		List<Object> classLists = ConfigContainer.getPropertys("hbase.entity");
		if(entitys!=null && entitys.size()>0){
			for(int i=0;i<entitys.size();i++){
				String className = entitys.get(i).toString();
				Class <IBaseEntity>clz = (Class<IBaseEntity>) Class.forName(className);
				basePhoenixBaseDao.createTable(clz);
			}
		}
		
//		List<Object> classListsTtl = ConfigContainer.getPropertys("hbase.ttl.entity");
//		if(classListsTtl!=null && classListsTtl.size()>0){
//			for(int i=0;i<classListsTtl.size();i++){
//				String className = classListsTtl.get(i).toString();
//				Class <IBaseEntity>clz = (Class<IBaseEntity>) Class.forName(className);
//				basePhoenixBaseDao.createTable(clz,"34819200"); //hbase table ttl: 34819200秒 = 13*31=403天
//			}
//		}
//		
//		List<Object> classListsProxyIp = ConfigContainer.getPropertys("hbase.proxyip.entity");
//		if(classListsProxyIp!=null && classListsProxyIp.size()>0){
//			for(int i=0;i<classListsProxyIp.size();i++){
//				String className = classListsProxyIp.get(i).toString();
//				Class <IBaseEntity>clz = (Class<IBaseEntity>) Class.forName(className);
//				basePhoenixBaseDao.createTable(clz,"604800"); //hbase table ttl: 604800 秒 = 7天
//			}
//		}
//		basePhoenixBaseDao.createTable(SinglePlaneInfoEntity.class);
//		basePhoenixBaseDao.createTable(DoublePlaneInfoEntity.class);
//		basePhoenixBaseDao.createTable(LogBaseAreaEntity.class);
	}
	public List<String> getEntitys() {
		return entitys;
	}
	public void setEntitys(List<String> entitys) {
		this.entitys = entitys;
	}

}
