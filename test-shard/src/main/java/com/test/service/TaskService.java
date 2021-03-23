/**
 * 
 */
package com.test.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.framework.shard.annotation.Shard;
import com.framework.shard.annotation.SplitShard;
import com.framework.utils.DateUtil;
import com.google.gson.Gson;
import com.test.cron.TaskCronJob;
import com.test.mapper.AbcEntityMapper;
import com.test.pojo.AbcEntity;
import com.test.pojo.AbcEntityExample;


/**
 * 服务器不够,不做集群，单机版
 * @author lyq
 * @date 2020年8月25日 下午12:33:18 
 */


@PropertySource("classpath:${env}/define.properties")
@Service
public class TaskService {
//	Interner<String> pool = Interners.newWeakInterner();//jdk1.7特殊处理,为了锁不消耗永久代内存。
	Gson gson = new Gson();
	Log logger = LogFactory.getLog(TaskService.class);
	@Value("${serial.dir}")
	private String serialUrl = null;
	private String serialHistDateList = "serialHistDate_list.dat";
	private String  upTOdateJobName = "upTOdateJobName.dat";
	
	@Autowired
	AbcEntityMapper mapper;
	@Autowired 
	TaskService taskService;
	@Autowired 
//	@Transactional
//	public void test(){
//		Job51Task  task = new Job51Task();
//		task.setPage(new java.util.Random().nextInt(20));
//		task.setCityCode("190200");
//		
//		new JobFetcher().fetch(task);
//		
//	}
	
	
	
	


	
//	private Set<String> getSerialObjSet(String dir){
//		File file = new File(dir);
////		System.out.println(file.getPath());
//		return getSerialObjSet(file);
//	}
	
	
	public static void main(String[] args) {
//		for(int i=0;i<10000;i++){
//			new Thread(new Runnable() {
//				public void run() {
					
//				Set<String> aa = new TaskService().getSerialObjSet("e:/serialHistDate_list.dat");

//				}
//			}).start();
			
//		}
	}
	
	public List<AbcEntity> abc(){
		AbcEntityExample example = new AbcEntityExample();
		List<AbcEntity> list = mapper.selectByExample(example );
		return list;
	}
	
	@SplitShard(paramPosition=0)
	@Transactional
	public void testParamPosition(String col1){
		List<AbcEntity> list = new ArrayList<AbcEntity>();
		for(int i=0;i<3;i++){
			AbcEntity entity = new AbcEntity();
			entity.setCol1(col1);
			entity.setCol2("b"+i);
			mapper.insert(entity);
			list.add(entity);
		}
		AbcEntity entity = new AbcEntity();
		entity.setCol1("03_dddd");
		entity.setCol2("test");
		List<AbcEntity> list1 = abc();
//		mapper.batchInsert(list);
	}
	
	@SplitShard(splitPPName="col1")
	@Transactional(TxType.REQUIRES_NEW)
//	@Transactional
	public void testSplitPPName(AbcEntity abc){
		System.out.println("======="+abc.getCol1()+" start============");
		
		List<AbcEntity> list1 = abc();
			mapper.insert(abc);
			for(AbcEntity ejtity:list1){
				System.out.println("col1:"+ejtity.getCol1()+";col2");
			}
		System.out.println("======="+abc.getCol1()+" end============");
//		mapper.batchInsert(list);
	}
}
