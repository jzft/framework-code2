package com.test.cron;

import org.springframework.stereotype.Component;

/**
 * 任务发布
 * @author Administrator
 *
 */
@Component
public class TaskCronJob {
//	final public static Queue<Job51Task> job51TaskQueue = new ConcurrentLinkedQueue<Job51Task>();
//
//	/**
//	 * 已经执行过的任务页
//	 */
//	static Map <Long,Set<Integer>> taskIdExecPage = new HashMap<Long,Set<Integer>>();
//	
//	private  ThreadPoolExecutor executor = null;
//	
//	
//	private ThreadPoolExecutor getExector(){
//		/**
//		 * 无界队列
//		 */
//		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
//		ThreadFactory threadFactory = new ThreadFactory() {
//			
//			@Override
//			public Thread newThread(Runnable r) {
//				Thread thread = new Thread(r);
//				thread.setName("生产抓取任务线程");
//				return thread;
//			}
//		};
////		ThreadPoolTaskExecutor e = new ThreadPoolTaskExecutor();
//		/**
//		 * 始终保持10个线程处理任务
//		 */
//		int threadSize = 10;
//		executor = new ThreadPoolExecutor(threadSize, threadSize, 10000, TimeUnit.MILLISECONDS, workQueue, threadFactory);
//		return executor;
//	}
//	
//	@Scheduled(fixedRate = 1000*60*60*24)//两秒一次
//	public void pushTask(){
//		Runnable command = new Runnable() {
//			@Override
//			public void run() {
//				Map<Long ,Set<Integer>> newTaskIdExecPage = new HashMap<Long ,Set<Integer>>();
//				taskIdExecPage.forEach((key,value)->{
//					int distance = DateUtil.daysOfTwo(new Date(key), new Date());
//					if(distance<=1){
//						newTaskIdExecPage.put(key, value);
//					}
//				});
//				taskIdExecPage.clear();
//				taskIdExecPage = newTaskIdExecPage;
//				Job51Task task = new Job51Task();
//				task.setCityCode("030200");
//				task.setPage(1);
//				task.setTaskId(System.currentTimeMillis());
//				pushTaskQueue(task);
//			}
//		};
//		getExector().execute(command );
//	}
//	
//	
//	/**
//	 * 新增任务
//	 * @param task
//	 */
//	public static void pushTaskQueue(Job51Task task){
//		Set<Integer> execPage = taskIdExecPage.get(task.getTaskId());
//		if(CollectionUtils.isNotEmpty(execPage)){
//			if(!execPage.contains(task.getPage())){//任务执行过.
//				System.out.println("新增任务："+task.getTaskId()+"_"+task.getPage());
//				execPage.add(task.getPage());
//				job51TaskQueue.add(task);
//			}
//		}else{
//			execPage = Collections.synchronizedSet(Sets.newHashSet());
//			taskIdExecPage.put(task.getTaskId(), execPage);
//			System.out.println("新增任务："+task.getTaskId());
//			execPage.add(task.getPage());
//			job51TaskQueue.add(task);
//		}
//	}
//	public boolean hasExecPage(Integer taskId,Integer page){
//		
//		return false;
//	}
	
}