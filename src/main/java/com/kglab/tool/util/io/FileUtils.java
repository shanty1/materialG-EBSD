package com.kglab.tool.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kglab.tool.util.base.NumberUtil;
import com.kglab.tool.util.parser.TikaUtil;

public class FileUtils {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	protected static final String DEF_ENCODE = "UTF-8";
	/** 从流中读取字节的缓冲区大小（默认：50kb） */
	private static final int bufferSize = 1024*50;

	/**
	 * 创建本地目录
	 * 
	 * @parma dirs 目录名称
	 * @return 是否成功
	 * @throws Exception
	 */
	public boolean makeDir(String dirs) throws Exception {
		boolean result = false;
		try {
			File fi = new File(dirs);
			// 创建目录
			result = fi.mkdirs();
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 创建Web目录的方法
	 * 
	 * @param request:Http回应request类
	 * @param dirs:Web路径 /相对路径 相对于跟目录 例如/Document/Contract/
	 * @return true/false
	 * @throws Exception
	 */

	public boolean makeRemoteDir(HttpServletRequest request, String dirs) throws Exception {
		boolean result = false;
		if (dirs != null) {
			String pathString = "";
			// 得到绝对路径
			pathString = request.getSession().getServletContext().getRealPath("");
			pathString = pathString.replace('\\', '/');
			// 得到目录路径
			pathString = pathString + dirs;
			try {
				File fi = new File(pathString);
				// 创建目录
				result = fi.mkdirs();
			} catch (Exception e) {
				result = false;
				logger.error(e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * 删除本地目录
	 * 
	 * @parma dirName 目录名称
	 * @return true/false
	 */

	public boolean deleteDirectory(String fullDirName) {
		boolean result = false;
		int len = 0, i = 0;
		try {
			File Dire = new File(fullDirName);
			if (!Dire.exists())
				result = false;// 源目录不存在返回
			if (Dire.isFile()) {
				result = Dire.delete();// 是文件删除文件
			}
			File[] fi = Dire.listFiles();
			if(fi!=null){
			    len = fi.length;// 取得目录下文件和目录数之和
	            if (len == 0) {
	                result = Dire.delete();// 空目录
	            }
	            for (i = 0; i < len; i++) {
	                if (fi[i].isDirectory()) {
	                    result = deleteDirectory(fi[i].getPath());// 删除目录
	                } else {
	                    result = fi[i].delete();// 删除文件
	                }
	            }
			}
			if (Dire.exists()) {
				result = Dire.delete();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 删除Web目录的方法
	 * 
	 * @param request:Http回应request类
	 * @param filePath:Web路径 /相对路径 相对于跟目录 例如/Document/Contract/
	 * @return true/false
	 * @throws Exception
	 */

	public boolean deleteRemoteDir(HttpServletRequest request, String filePath) throws Exception {

		boolean result = false;
		if (filePath != null) {
			String pathString = "";
			// 取得目录路径
			pathString = request.getSession().getServletContext().getRealPath("");
			pathString = pathString.replace('\\', '/');
			pathString = pathString + filePath;
			int len = 0, i = 0;
			try {
				File Dire = new File(pathString);
				if (!Dire.exists()) {
					// 目录不存在
					result = false;
				}
				if (Dire.isFile()) {
					result = Dire.delete();
				}
				File[] fi = Dire.listFiles();
				// 得到目录下文件数以及文件夹数
				len = fi.length;
				if (len == 0) {
					// 删除空目录
					result = Dire.delete();
				}
				for (i = 0; i < len; i++) {
					if (fi[i].isDirectory()) {
						// 删除文件夹以及文件夹所有文件
						result = deleteDirectory(fi[i].getPath());
					} else {
						// 删除文件
						result = fi[i].delete();
					}
				}
				if (Dire.exists()) {
					result = Dire.delete();
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		return result;

	}

	/**
	 * 移动一个文件夹和文件夹下所有文件
	 * 
	 * @parma sourDir 源目录
	 * @parma desDir 目标目录
	 * @return 是否成功
	 */

	public boolean moveDirectory(String sourceDir, String desDir) {
		boolean result = false;
		int len = 0, i = 0;
		sourceDir = sourceDir.replace('\\', '/');
		desDir = desDir.replace('\\', '/');
		String sourcePath = "";
		String desPath = "";
		String fileName = "";
		try {
			File Dire = new File(sourceDir);
			if (!Dire.exists()) {
				result = false;
			}
			File[] fi = Dire.listFiles();
			len = fi.length;
			if (len == 0) {
				Dire.delete();// 空目录则退出
				result = true;
			}
			File d = new File(desDir);
			if (!d.exists()) {
				this.makeDir(desDir);
			}
			for (i = 0; i < len; i++) {
				if (fi[i].isDirectory()) // 判断是否是子目录
				{
					// 取得子目录名称subdirname
					int last = fi[i].getPath().lastIndexOf("\\");
					String subdirname = fi[i].getPath().substring(last + 1, fi[i].getPath().length());
					// 移动子目录下所有的文件以及目录
					result = moveDirectory(fi[i].getPath(), desDir + "/" + subdirname);
					if (result) {
						// 移除成功删除源目录
						deleteDirectory(fi[i].getPath());
					}
				} else {
					// 移除目录下的文件
					fileName = fi[i].getName();
					sourcePath = fi[i].getAbsolutePath();
					desPath = desDir.replace('/', '\\');
					desPath = desPath + "\\" + fileName;
					this.moveFile(sourcePath, desPath);
				}
			}
			if (Dire.exists()) {
				Dire.delete();
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 移动一个文件夹和文件夹下所有文件
	 * 
	 * @parma sourDir 源目录
	 * @parma desDir 目标目录
	 * @parma dirName 目标目录指定文件夹的名称
	 * @return 是否成功
	 */
	public boolean moveDirectory(String sourceDir, String desDir, String dirName) {
		boolean result = false;
		int len = 0, i = 0;
		sourceDir = sourceDir.replace('\\', '/');
		desDir = desDir.replace('\\', '/');
		String sourcePath = "";
		String desPath = "";
		String fileName = "";
		try {
			File Dire = new File(sourceDir);
			if (!Dire.exists()) {
				result = false;
			}
			File[] fi = Dire.listFiles();
			len = fi.length;
			if (len == 0) {
				result = Dire.delete();
			}
			File d = new File(desDir + "/" + dirName);
			if (!d.exists()) {
				result = this.makeDir(desDir + "/" + dirName);
			}
			for (i = 0; i < len; i++) {
				if (fi[i].isDirectory())// 判断是否是目录
				{
					// 取得子目录的名称subdirName
					int last = fi[i].getPath().lastIndexOf("\\");
					String subdirName = fi[i].getPath().substring(last + 1, fi[i].getPath().length());
					// 移动子目录以及子目录下文件
					result = moveDirectory(fi[i].getPath(), desDir + "/" + dirName + "/" + subdirName);
					if (result) {
						deleteDirectory(fi[i].getPath()); // 将源目录删除
					}
				} else {
					// 移动目录下的文件
					fileName = fi[i].getName();
					sourcePath = fi[i].getAbsolutePath();
					desPath = desDir.replace('/', '\\');
					desPath = desPath + "\\" + dirName + "\\" + fileName;
					result = this.moveFile(sourcePath, desPath);
				}
			}
			// 删除源目录
			if (Dire.exists()) {
				result = Dire.delete();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * @Title 写文件
	 * @Description 根据文件全路径名和内容，创建并写入文件内容
	 * @author lyz
	 * @date 2015年6月1日 下午4:46:32
	 * @param fullFileName 包含文件名的全路径名
	 * @param txt 文本内容
	 * @return boolean
	 */
	public static boolean createFile(String fullFileName, String txt) {
		if (StringUtils.isBlank(fullFileName)) {
			return false;
		}
		boolean restult = false;
		try {
			if (txt == null)
				txt = "";
			int last = fullFileName.lastIndexOf("/");
			if (last < 0) {
				last = fullFileName.lastIndexOf("\\");
			}
			String dirName = fullFileName.substring(0, last);
			File Dire = new File(dirName);
			if (!Dire.exists()) {
				Dire.mkdir();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(fullFileName)), DEF_ENCODE);
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(txt);
			writer.close();
			restult = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return restult;
	}

	/**
	 * @Title 读取文件内容
	 * @Description
	 * @author lyz
	 * @date 2015年6月1日 下午4:57:39
	 * @param fullFileName 包含文件名的全路径名
	 * @return String
	 */
	public static String readFile(String fullFileName) {
		if (StringUtils.isBlank(fullFileName)) {
			return null;
		}
		StringBuilder fileContent = new StringBuilder();
		try {
			File f = new File(fullFileName);
			if (f.isFile() && f.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(f), DEF_ENCODE);
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					fileContent.append(line);
				}
				read.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return fileContent.toString();
	}

	/**
	 * 创建web文件
	 * 
	 * @parma fullFileName 文件路径+文件名称
	 * @parma txt 文件内容
	 * @return 是否成功
	 */
	public boolean createRemoteFile(HttpServletRequest request, String fullFileName, String txt) {
		boolean restult = false;
		String pathString = "";
		pathString = request.getSession().getServletContext().getRealPath("");
		pathString = pathString.replace('\\', '/');
		fullFileName = pathString + fullFileName;
		try {
			if (txt == null)
				txt = "";
			int last = fullFileName.lastIndexOf("/");
			String dirName = fullFileName.substring(0, last);
			dirName = dirName.replace('/', '\\');
			File Dire = new File(dirName);
			if (!Dire.exists()) {
				makeDir(dirName);
			}

			PrintWriter pw = new PrintWriter(new FileOutputStream(fullFileName));
			pw.println(txt);
			restult = true;
			pw.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return restult;
	}

	/**
	 * 删除本地文件
	 * 
	 * @parma fullFileName 文件绝对路径+文件名称 如:D:\\test\\test.jsp
	 * @return true/false
	 */
	public boolean deleteFile(String fullFileName) {
		boolean result = false;
		File fl;
		if ((fullFileName == null) || (fullFileName.equals(""))) {
			result = false;
		}
		fullFileName = StringUtils.replace(fullFileName, "//", "\\");
		fl = new File(fullFileName);
		result = fl.delete();
		return result;
	}

	/**
	 * 删除web文件的方法
	 * 
	 * @param request:Http回应request类
	 * @param filePath:文件路径 /相对路径 相对于跟目录 例如/Document/Contract/Test.xml
	 * @return true/false
	 * @throws Exception
	 */
	public boolean deleteRemoteFile(HttpServletRequest request, String filePath) throws Exception {
		boolean result = false;
		if (filePath != null) {
			String pathString = "";
			// 取得文件路径以及文件名称
			pathString = request.getSession().getServletContext().getRealPath("");
			pathString = pathString.replace('\\', '/');
			pathString = pathString + filePath;
			try {
				File f = new File(pathString);
				if (f.exists())
					result = f.delete(); // 删除文件
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}

		return result;
	}

	/**
	 * 取得文件扩展名
	 * 
	 * @parma fullFileName 文件路径+文件名称 文件路径+文件名称或者文件名 如:D:\\test\\test.jsp 或者/test/test.jsp 或者test.jsp
	 * @return String
	 */
	public String getFileExtName(String fullFileName) {
		int i = 0, Len = 0;
		String charStr = "", rtn = "";

		if (fullFileName == null)
			return "";
		fullFileName = fullFileName.trim();
		Len = fullFileName.length();
		if (Len <= 1)
			return "";

		for (i = Len - 1; i > 0; i--) {
			charStr = fullFileName.substring(i, i + 1);
			rtn = charStr + rtn;
			if (charStr.compareTo(".") == 0)
				break;
		}
		if (rtn.length() > 5)
			return "";
		else return rtn;
	}

	/**
	 * 取得文件名称不含扩展名
	 * 
	 * @parma fullFileName 文件路径+文件名称或者文件名 如:D:\\test\\test.jsp 或者/test/test.jsp 或者test.jsp
	 * @return String
	 */
	public String getFileNoExtName(String fullFileName) {
		String rtn = "", ext = "";
		if (fullFileName.length() <= 5)
			return "";
		fullFileName = fullFileName.replace('\\', '/');
		ext = this.getFileExtName(fullFileName);
		int Start = fullFileName.lastIndexOf("/");
		rtn = fullFileName.substring(Start + 1, fullFileName.length() - ext.length());
		return rtn;
	}

	/**
	 * 取得完整的文件名包括文件扩展名
	 * 
	 * @param str
	 * @return String
	 */

	public static final String getFile(String path) {
		String result = "";
		if (path.length() < 5)
			return "";
		try {
			path = path.trim();
			String str = "";
			for (int i = path.length(); i > 0; i--) {
				str = path.substring(i - 1, i);
				if (str.equals("/") || str.equals("\\"))
					break;
				result = str + result;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return result;
	}

	/**
	 * 判断本地文件或者目录是否存在
	 * 
	 * @parma fullFileName 文件路径+文件名称 如:D:\\test\\test.jsp
	 * @return String
	 */
	public boolean isExist(String fullFileName) {
		File fl;
		if ((fullFileName == null) || (fullFileName.equals("")))
			return false;

		fullFileName = StringUtils.replace(fullFileName, "//", "\\");
		fl = new File(fullFileName);
		if (fl.exists())
			return true;
		else return false;
	}

	/**
	 * 判断web目录或者web文件是否存在
	 * 
	 * @parma fullFileName 文件路径+文件名称 如:/test/test.jsp
	 * @return true/false
	 */
	public boolean isRemoteExist(HttpServletRequest request, String fullFileName) {
		File fl;
		if ((fullFileName == null) || (fullFileName.equals("")))
			return false;
		String pathString = "";
		pathString = request.getSession().getServletContext().getRealPath("");
		pathString = pathString.replace('\\', '/');
		pathString = pathString + fullFileName;
		fl = new File(pathString);
		if (fl.exists())
			return true;
		else return false;
	}

	/**
	 * 本地文件更名
	 * 
	 * @parma oldFileName 文件路径+文件名称 如:D:\\test\\test.jsp newFileName 更改后文件名
	 * @return true/false
	 */
	public boolean reName(String oldFileName, String newFileName) {
		boolean result = false;
		try {
			File fl;
			File f2;
			if ((oldFileName == null) || (oldFileName.equals("")) || (newFileName == null) || (newFileName.equals(""))) {
				result = false;
			} else {

				if ((newFileName.indexOf("\\") > 0) || (newFileName.indexOf("/") > 0) || (newFileName.indexOf(":") > 0)
						|| (newFileName.indexOf("*") > 0) || (newFileName.indexOf("?") > 0) || (newFileName.indexOf("|") > 0)
						|| (newFileName.indexOf("<") > 0) || (newFileName.indexOf(">") > 0)) {
					result = false;
				} else {
					oldFileName = StringUtils.replace(oldFileName, "//", "\\");
					int last = oldFileName.lastIndexOf("\\");
					String filePath = oldFileName.substring(0, last);
					fl = new File(oldFileName);
					f2 = new File(filePath + "\\" + newFileName);
					result = fl.renameTo(f2);
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * web目录文件更名
	 * 
	 * @parma oldFileName 文件路径+文件名称 如:/test/test.jsp newFileName 更改后文件名
	 * @return true/false
	 */
	public boolean reNameRemoteFile(HttpServletRequest request, String oldFileName, String newFileName) {
		boolean result = false;
		try {
			File fl;
			File f2;
			if ((oldFileName == null) || (oldFileName.equals("")) || (newFileName == null) || (newFileName.equals(""))) {
				result = false;
			} else {
				String pathString = "";
				if ((newFileName.indexOf("\\") > -1) || (newFileName.indexOf("/") > -1) || (newFileName.indexOf(":") > -1)
						|| (newFileName.indexOf("*") > -1) || (newFileName.indexOf("?") > -1) || (newFileName.indexOf("|") > -1)
						|| (newFileName.indexOf("<") > 0) || (newFileName.indexOf(">") > -1)) {
					result = false;
				} else {

					// 取得文件路径以及文件名称
					pathString = request.getSession().getServletContext().getRealPath("");
					pathString = pathString.replace('\\', '/');
					pathString = pathString + oldFileName;
					int last = pathString.lastIndexOf("/");
					// 取得路径
					String filePath = pathString.substring(0, last);
					fl = new File(pathString);
					f2 = new File(filePath + "/" + newFileName);
					result = fl.renameTo(f2);
				}
			}
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 移除文件
	 * 
	 * @parma src 源文件地址
	 * @param des 目的文件地址
	 * @return true/false
	 */
	public boolean moveFile(String src, String des) throws Exception {
		boolean result = false;
		try {
			FileInputStream fi = new FileInputStream(src);
			BufferedInputStream ipt = new BufferedInputStream(fi);

			FileOutputStream fo = new FileOutputStream(des);
			BufferedOutputStream opt = new BufferedOutputStream(fo);

			boolean eof = false;
			while (!eof) {
				int input = ipt.read();
				if (input == -1)
					break;
				opt.write(input);
			}
			ipt.close();
			opt.close();
			File Source = new File(src);
			if (Source.delete())
				result = true;
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * 读取文件，如果不存在则创建
	 * <br><br>
	 * 
	 * @param filePath
	 * @return
	 * @date 2018年3月21日
	 * @author 舒超
	 * @throws IOException 
	 */
	public static File newFile(String filePath) throws IOException{
		return new File(filePath);
	}
	/**
	 * 创建文件
	 * <br><br>
	 * 
	 * @param filePath
	 * @return
	 * @date 2018年3月21日
	 * @author 舒超
	 * @throws IOException 
	 */
	public static File newFile(File file) throws IOException{
		if (file.exists())
			return file;
		File pre_file = file.getParentFile();
		if (!pre_file.exists() || !pre_file.isDirectory()) {
			pre_file.mkdirs();
		}
		file.createNewFile();
		return file;
	}
	
	
	/**
	 * 读取文件夹，如果不存在则创建
	 * <br><br>
	 * 
	 * @param directory
	 * @return
	 * @date 2018年3月21日
	 * @author 舒超
	 */
	public static File newDirectory(String directory){
		File file = new File(directory);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	

	/**
	 * 获得指定文件的byte数组 
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static byte[] file2Byte(String filePath){
		ByteArrayOutputStream bos=null;
		BufferedInputStream in=null;
		try{
			File file = new File(filePath);
			if(!file.exists()){  
	            return null;
	        }
			bos=new ByteArrayOutputStream((int)file.length());
			in=new BufferedInputStream(new FileInputStream(file));
			int buf_size=1024;
			byte[] buffer=new byte[buf_size];
			int len=0;
			while(-1 != (len=in.read(buffer,0,buf_size))){
				bos.write(buffer,0,len);
			}
			return bos.toByteArray();
		}
		catch(Exception e){
			logger.error("file2Byte",e);
            return null;
		}
		finally{
			try{
				if(in!=null){
					in.close();
				}
				if(bos!=null){
					bos.close();
				}
            }
			catch(Exception e){
				logger.error(e.getMessage(), e);  
            }
        }
    }
    /**
     * 根据byte数组，生成文件 
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
	public static void byte2File(byte[] bfile,File file){
		BufferedOutputStream bos=null;
		FileOutputStream fos=null;
		try{
			fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			bos.write(bfile);
		} 
		catch(Exception e){
			logger.error("byte2File",e); 
        }
		finally{
			try{
				if(bos != null){
					bos.close(); 
				}
				if(fos != null){
					fos.close();
				}
			}
			catch(Exception e){
				 logger.error(e.getMessage(), e); 
				 
			}
		}
    }

	/**
	 *  文件流保存文件(此方法会关闭流)
	 * @author shuchao
	 * @data   2019年3月5日
	 * @param inputStream
	 * @param file
	 * @throws Exception
	 */
	public static boolean SaveFile(InputStream inputStream, File file) throws Exception {
		OutputStream os = null;
		try {
			byte[] buffer = new byte[bufferSize];
			int len;
			os = new FileOutputStream(file);
			long s = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
				s += len;
			}
			System.out.println("大小："+s);
			return true;
		} catch (Exception e1) {
			logger.error("SaveFile error", e1);
			return false;
		}finally {
			// 完毕，关闭所有链接
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  文件流保存文件(此方法会关闭流)
	 * @author shuchao
	 * @data   2019年3月5日
	 * @param inputStream
	 * @param file
	 * @throws Exception
	 */
	public static boolean SaveFile(InputStream inputStream, @NotNull final DownloadProgress downloadProgress){
		// 输出的文件流保存到本地文件
		OutputStream os = null;
		File file = downloadProgress.getFile();
		try {
			byte[] buffer = new byte[bufferSize];
			int len;
			os = new FileOutputStream(file);
			// 开始读取
			long s = 0 ;
			while ((len = inputStream.read(buffer)) != -1) {
				os.write(buffer, 0, len);
				os.flush();
			downloadProgress.setFinishedPercent(
						NumberUtil.calculatedPercentage(s, downloadProgress.getSize()));
				s += len;
			}
//			System.out.println("实际大小："+s+"  ，识别大小："+downloadProgress.getSize());
			return true;
		} catch (Exception e1) {
			logger.error("SaveFile error", e1);
			// 重要：如果失败则要设置失败状态！
			downloadProgress.setFail();
			return false;
		}finally {
			// 完毕，关闭所有链接
			try {
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(downloadProgress.isFail())
				downloadProgress.setFail();
			else {// 判断文件后缀是否正确，不正确就会改正
				String suffix = "."+TikaUtil.getFileSuffix(file);
				String filename = file.getAbsolutePath();
				if(!filename.endsWith(suffix)) {
					if(file.renameTo(new File(filename+suffix)))
						// file更名后需要重新设置dp中file的引用
						downloadProgress.setFile(new File(filename+suffix));
				}
				// 重要：下载完成一定要置满文件下载进度！只有在更名结束之后才算真正完成了。否则其他调用此引用会导致名称不统一。
				downloadProgress.setFinished();
			}
		}
	}

	public static boolean isExistFile(File f) {
		if(f!=null && f.exists() && f.isFile() && f.length()>0) return true;
		else return false;
	}
	public static boolean isExistFolder(File f) {
		if(f!=null && f.exists() && f.isDirectory()) return true;
		else return false;
	}

	/**
	 * 创建文件夹，如果存在，则清空里面的文件
	 * @author shuchao
	 * @data   2019年3月5日
	 * @param folder
	 */
	public static void newClearFolder(File folder) {
		if(isExistFolder(folder)) {
			File[] files = folder.listFiles();
			for(File f : files)
				f.delete();
		}else
			folder.mkdirs();
	}
}
