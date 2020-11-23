package com.kglab.tool.util.io;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class DownloadProgress {
	/** 下载线程ID */
	@NotNull
	private String id;
	private float finishedPercent=0.0f;
	private File file=null;
	/** 文件总大小（由文件下载前确定） */
	private long size ;
	
	/** 下载进度缓存(最高承受300人同时下载记录) */
	private static final LinkedHashMap<String,  DownloadProgress> downloadProgressCache = new LinkedHashMap<String,DownloadProgress>(300,
			0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<String, DownloadProgress> eldest) {
			return size() > 299;
		}
	};
	
	/** 创建一个下载进度集(默认加入下载监控列表)*/
	public DownloadProgress(String id) { this(id, 0, null); } 
	/** 创建一个下载进度集(默认加入下载监控列表)*/
	public DownloadProgress(String id, long size) { this(id, size, null); } 
	/** 创建一个下载进度集(默认加入下载监控列表)*/
	public DownloadProgress(String id, File file) { this(id, 0, file); } 

	/** 创建一个下载进度集(默认加入下载监控列表)*/
	public DownloadProgress(String id, long size, File file) {
		super();
		this.id = id;
		this.size = size;
		this.file = file;
		downloadProgressCache.put(id, this);
	}
	 
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getSize() {
		if(size<=0) {
			throw new IllegalArgumentException("DownloadProgress未设置size大小");
		}
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public float getFinishedPercent() {
		return finishedPercent;
	}
	/**
	 * 注：此方法不能将进度设为满值，若要设满请使用{@link #setFinished}
	 * @author shuchao
	 * @data   2019年3月6日
	 * @param l
	 */
	public void setFinishedPercent(float l) {
		if(l>=100) {
			this.finishedPercent  = 99.9f;
		}else {
			this.finishedPercent = l;
		}
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	/**
	  *   是否成功下载完成
	 * @author shuchao
	 * @data   2019年3月6日
	 * @return
	 */
	public boolean isFinished() {
		if(finishedPercent>=100)
			return true;
		else return false;
	}
	/**
	 * 是否下载失败
	 */
	public boolean isFail() {
		if(finishedPercent<0)
			return true;
		else return false;
	}
	
	/**
	 * 获取下载状态
	 * @author shuchao
	 * @data   2019年3月6日
	 * @return -1：下载失败<br>  0：下载中<br>   1：下载完成
	 * 
	 */
	public short getStatus() {
		if(finishedPercent<0)
			return -1;
		else if(finishedPercent==100)
			return 1;
		else 
			return 0;
	}
	public void setFinished() {
		finishedPercent = 100;
	}
	/**
	 * 此方法同时会将文件删除
	 * @author shuchao
	 * @data   2019年3月6日
	 */
	public void setFail() {
		finishedPercent = -1;
		try {
			System.out.println("删除文件："+this.file.delete());
			System.out.println("删除文件夹："+this.file.getParentFile().delete());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	/**
	 * 获取下载进度线程列表(包括已结束和失败的)
	 * @author shuchao
	 * @data   2019年3月8日
	 * @return
	 */
	public static LinkedHashMap<String,  DownloadProgress> getDownloadProgressCache() {
		return downloadProgressCache;
	}
	
	/**
	 * 根据ID获取下载进度集
	 * @author shuchao
	 * @data   2019年3月8日
	 * @return
	 */
	public static DownloadProgress get(String id) {
		return downloadProgressCache.get(id);
	}
	public static DownloadProgress remove(String id) {
		return downloadProgressCache.remove(id);
	}
}
