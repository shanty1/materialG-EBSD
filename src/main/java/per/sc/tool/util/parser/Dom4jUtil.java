package per.sc.tool.util.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * xml文档解析工具类
 * <br><br>
 * 进一步封装调用dom4j的api
 * @Date 2018年1月9日
 * @Author 舒超
 */
public class Dom4jUtil {

	private static Logger log = LoggerFactory.getLogger(Dom4jUtil.class);
	
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document load(InputStream in) {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(in);
		} catch (Exception e) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", e);
		} 
		return document;
	} 
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document load(InputSource ins) {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(ins);
		} catch (Exception e) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", e);
		} 
		return document;
	} 
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document load(Reader rd) {
		Document document = null;
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(rd);
		} catch (Exception e) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", e);
		} 
		return document;
	} 
	
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @param filePath cml文件绝对路径
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document load(String filePath) {
		Document document = null;
		File file = null;
		try {
			file = new File(filePath);
		} catch (Exception e) {
			log.error("【错误！】读取文件失败，异常信息如下\n", e);
		}
		if(file==null || !file.exists()){
			log.error("文件["+file.getAbsolutePath()+"]不存在！");
		}
		SAXReader saxReader = new SAXReader();
		try {
			document = saxReader.read(file);
		} catch (Exception e) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", e);
		} 
		return document;
	} 
	
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @param file 文档文件
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document load(File file) {
		Document document = null;
		if(file==null || !file.exists()){
			log.error("文件["+file.getAbsolutePath()+"]不存在！");
		}
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(file);  
		} catch (Exception ex) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", ex);
		}
		return document;
	} 
	
	/**
	 * 加载一个xml，生成document对象
	 * <br><br>
	 * 
	 * @param xmlString xml文本
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	public static Document parse(String xmlString) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(xmlString);  
		} catch (Exception ex) {
			log.error("【错误！】解析xml文档失败，异常信息如下\n", ex);
		}
		return document;
	} 

	/**
	 * 选取指定节点的text值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param xpathQuery 选择器语句
	 * @return 解析节点的text值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectText(Document doc, String xpathQuery, String defaultVal){
		return select(doc, xpathQuery,"text",defaultVal);
	}
	
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Element el, String xpathQuery, String defaultVal){
		return select(el, xpathQuery,"text",defaultVal);
	}
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Node node, String xpathQuery, String defaultVal){
		return select(node, xpathQuery,"text",defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param xpathQuery 选择器语句
	 * @param attrName 节点属性名
	 * @return 解析节点的属性值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectAttr(Document doc, String xpathQuery, String attrName, String defaultVal){
		return select(doc, xpathQuery,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Element el, String xpathQuery, String attrName, String defaultVal){
		return select(el, xpathQuery,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Node node, String xpathQuery, String attrName, String defaultVal){
		return select(node, xpathQuery,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的value值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param xpathQuery 选择器语句
	 * @param defult 解析失败或者结果节点value值不存在时返回默认值
	 * @return 解析节点的属性值 (如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectValue(Document doc, String xpathQuery, String defaultVal){
		return select(doc, xpathQuery, "value", defaultVal);
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Element el, String xpathQuery, String defaultVal){
		return select(el, xpathQuery,"value",defaultVal);
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Node node, String xpathQuery, String defaultVal){
		return select(node, xpathQuery,"value",defaultVal);
	}
	
	
	
	/**
	 * 选取指定节点的text值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param xpathQuery 选择器语句
	 * @return 解析节点的text值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectText(Document doc, String xpathQuery){
		return select(doc, xpathQuery,"text","");
	}
	
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Element el, String xpathQuery){
		return select(el, xpathQuery,"text","");
	}
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Node node, String xpathQuery){
		return select(node, xpathQuery,"text","");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param xpathQuery 选择器语句
	 * @param attrName 节点属性名
	 * @return 解析节点的属性值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectAttr(Document doc, String xpathQuery, String attrName){
		return select(doc, xpathQuery,attrName,"");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Element el, String xpathQuery, String attrName){
		return select(el, xpathQuery,attrName,"");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Node node, String xpathQuery, String attrName){
		return select(node, xpathQuery,attrName,"");
	}
	
	/**
	 * 选取指定节点的value值
	 * 
	 * @description:  
	 * @param doc dom4j-Document 文档对象
	 * @param xpathQuery xpath语法路径
	 * @param defult 解析失败或者结果节点value值不存在时返回默认值
	 * @return 解析节点的属性值 (如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectValue(Document doc, String xpathQuery){
		return select(doc, xpathQuery, "value", "");
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Element el, String xpathQuery){
		return select(el, xpathQuery,"value","");
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Node node, String xpathQuery){
		return select(node, xpathQuery, "value", "");
	}
	
	/** 选取文档元素 */
	public static Element selectElement(Document document, String xpathQuery){
		return selectEle(document, xpathQuery);
	}
	/** 选取文档元素 */
	public static Element selectElement(Node node, String xpathQuery){
		return selectEle(node, xpathQuery);
	}
	/** 选取多个文档元素 */
	public static List<Element> selectElements(Document document, String xpathQuery){
		return selectEles(document, xpathQuery);
	}
	/** 选取多个文档元素 */
	public static List<Element> selectElements(Node node, String xpathQuery){
		return selectEles(node, xpathQuery);
	}
	
	/**
	 * 获取节点文本或者属性值
	 * <br><br>
	 * 
	 * @param node
	 * @param xpathQuery
	 * @param attrName
	 * @param defaultVal
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	private static String select(Object node, String xpathQuery, String attrName, String defaultVal){
		Element el = selectEle(node, xpathQuery);
		return select(el, attrName, defaultVal);
	}
	
	/**
	 * 获取节点
	 * <br><br>
	 * 
	 * @param node
	 * @param xpathQuery
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	private static Element selectEle(Object node,  String xpathQuery){
		Element el = null;
		try {
			if(Document.class.isInstance(node)){
				Document document = (Document) node;
				el = (Element) document.selectSingleNode(xpathQuery);
			}else if(Element.class.isInstance(node)){
				el = (Element) node;
				el = (Element) el.selectSingleNode(xpathQuery);
			}else if(Node.class.isInstance(node)){
				el = (Element) node;
				el = (Element) el.selectSingleNode(xpathQuery);
			}
		} catch (Exception e) {
			log.error("selectEle",e);
		}
		
		return el;
	}
	/**
	 * 获取多节点
	 * <br><br>
	 * 
	 * @param node
	 * @param xpathQuery
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	@SuppressWarnings("unchecked")
	private static List<Element> selectEles(Object node,  String xpathQuery){
		List<Element> els = null;
		try {
			if(Document.class.isInstance(node)){
				Document document = (Document) node;
				els = (List<Element>) document.selectNodes(xpathQuery);
			}else if(Element.class.isInstance(node)){
				Element el = (Element) node;
				els = (List<Element>) el.selectNodes(xpathQuery);
			}else if(Node.class.isInstance(node)){
				Element el = (Element) node;
				els = (List<Element>) el.selectNodes(xpathQuery);
			}
		} catch (Exception e) {}
		
		return els;
	}
	
	/**
	 * 获取节点文本
	 * <br><br>
	 * 
	 * @param el
	 * @param attrName
	 * @param defaultVal
	 * @return
	 * @date 2018年1月10日
	 * @author 舒超
	 */
	private static String select(Element el, String attrName, String defaultVal){
		if(el==null){
			return defaultVal;
		}
		if("text".equals(attrName)){
			return el.getText();
		}else{
			return el.attributeValue(attrName);
		}
	}
	
	/**
	 * 序列化，生成xml文件
	 * <br><br>
	 * 
	 * @param document
	 * @param file
	 * @return
	 * @throws Exception
	 * @date 2018年1月10日
	 * @author 舒超
	 */
   public boolean writer(Document document, String file) throws Exception {  
	   boolean b = false;
		// 紧凑的格式
		// OutputFormat format = OutputFormat.createCompactFormat();
		// 排版缩进的格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		File f = createFile(file);
		if(document!=null && f!=null){
			try {
				// 创建XMLWriter对象,指定了写出文件及编码格式
				OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(new File(file)), "UTF-8");
				XMLWriter writer = new XMLWriter(os, format);
				// 写入
				writer.write(document);
				// 立即写入
				writer.flush();
				// 关闭操作
				writer.close();
				b = true;
			} catch (Exception e) {
				log.error("writer", e);
			}
		}
		return b;
	}
	
   private static File createFile(String file){
	   File f = null;
		try {
			f = new File(file);
			if (!f.exists()) {
				if(!f.mkdirs()){
					f= null;
				}
			} 
		} catch (Exception e) {
			log.error("createFile", e);
		}
		return f;
	}

}
