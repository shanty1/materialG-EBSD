package com.kglab.tool.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadExecutor {
	
	private static final int corePoolSize = 10;    // 初始线程池大小(其实是指在缓存队列没满情况下线程池大小，初始为0)
	private static final int maximumPoolSize = 20; // 最大创建线程数(当缓存队列排满了才会创建新的线程)
	private static final long keepAliveTime = 10;  // 单位：分钟（见下面构造方法单位参数）
	private static final int queueLength = 100;    // 排队队列长度
	
	private final static ThreadPoolExecutor threadPoolExecutor = 
			new ThreadPoolExecutor(
					corePoolSize,
					maximumPoolSize, 
					keepAliveTime,
					TimeUnit.MINUTES,
					new ArrayBlockingQueue<Runnable>(queueLength));
	/**
	 *   放入线程池执行
	 * @author shuchao
	 * @data   2019年3月3日
	 * @param command
	 */
	public static void execute(Runnable command) {
		threadPoolExecutor.execute(command);
	}
	
	/**
	 * 打印当前线程池运行情况
	 * @author shuchao
	 * @data   2019年3月3日
	 */
	public static void printStatus() {
		int queueSize = threadPoolExecutor.getQueue().size();
        System.out.println("当前排队线程数：" + queueSize);

        int activeCount = threadPoolExecutor.getActiveCount();
        System.out.println("当前活动线程数：" + activeCount);

        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        System.out.println("执行完成线程数：" + completedTaskCount);

        long taskCount = threadPoolExecutor.getTaskCount();
        System.out.println("总线程数：" + taskCount);
	}
	
	public static void main(String[] args) {
		for(int i =0; i <111; i++) {
			execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		printStatus();
	}
}
