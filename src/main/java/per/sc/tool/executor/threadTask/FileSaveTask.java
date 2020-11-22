package per.sc.tool.executor.threadTask;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *   文件保存线程
 * @author shuchao
 * @date   2019年3月3日
 */
public class FileSaveTask implements Runnable{
	private String absoluteFileName;
	private byte[] fileByte;

	private static Logger logger = LoggerFactory.getLogger(FileSaveTask.class);
	
	public FileSaveTask(String absoluteFileName, byte[] fileByte) {
		super();
		this.fileByte = fileByte;
		this.absoluteFileName = absoluteFileName;
	}

	@Override
	public void run() {
		try {
			FileUtils.writeByteArrayToFile(per.sc.tool.util.io.FileUtils.newFile(absoluteFileName), fileByte);
		} catch (IOException e) {
			logger.error("文件保存异常", e);
		}
	}
	
}
