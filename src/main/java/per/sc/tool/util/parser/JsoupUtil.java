package per.sc.tool.util.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * jsoup解析工具类
 * 
 * @description
 * @author 舒超
 * @date 2017年12月11日
 */
public class JsoupUtil {
	/**
	 * 转换文本编码
	 * <BR/><BR/>
	 * 
	 * @param text 原文
	 * @param textCharset 解码格式
	 * @param changeToCharset 重新编码格式
	 * @return
	 * @date 2017年12月15日
	 * @author 舒超
	 */
	public static String transcoding(String text, String textCharset, String changeToCharset){
		if (text == null) {
			return "";
		}
		try {
			return new String(text.getBytes(textCharset), changeToCharset).trim();
		} catch (Exception e) {
			return text;
		}
	}
	public static Document parse(String html, String charset){
		if(html!=null && !"".equals(html.trim())){
			html = html.replace("&amp;", "&").replace("&quot;", "\"").replace("&lt;", "<").replace("&gt;", ">").replace("&nbsp;", " ");
			html = html.replace("\\r\\n", " ").replace("\\r", " ").replace("\\n", " ");
			if(charset!=null && !"".equals(charset.trim())){
				html = transcoding(html, "ISO-8859-1", charset);
			}
			return Jsoup.parse(html);
		}
		return null;
	}
	public static Document parse(String html){
		return parse(html, null);
	}
	/**
	 * 选取指定节点的text值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @return 解析节点的text值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectText(Document doc,String query, String defaultVal){
		return selectNode(doc, query,"text",defaultVal);
	}
	
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Element el,String query, String defaultVal){
		return selectNode(el, query,"text",defaultVal);
	}
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Elements els,String query, String defaultVal){
		return selectNode(els, query,"text",defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @param attrName 节点属性名
	 * @return 解析节点的属性值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectAttr(Document doc,String query, String attrName, String defaultVal){
		return selectNode(doc, query,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Element el,String query, String attrName, String defaultVal){
		return selectNode(el, query,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Elements els,String query, String attrName, String defaultVal){
		return selectNode(els, query,attrName,defaultVal);
	}
	
	/**
	 * 选取指定节点的value值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @param defult 解析失败或者结果节点value值不存在时返回默认值
	 * @return 解析节点的属性值 (如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectValue(Document doc,String query, String defaultVal){
		return selectNode(doc, query, "value", defaultVal);
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Element el,String query, String defaultVal){
		return selectNode(el, query,"value",defaultVal);
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Elements els,String query, String defaultVal){
		return selectNode(els, query,"value",defaultVal);
	}
	
	
	
	/**
	 * 选取指定节点的text值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @return 解析节点的text值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectText(Document doc,String query){
		return selectNode(doc, query,"text","");
	}
	
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Element el,String query){
		return selectNode(el, query,"text","");
	}
	/**
	 * 选取指定节点的text值
	 * 
	 */
	public static String selectText(Elements els,String query){
		return selectNode(els, query,"text","");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @param attrName 节点属性名
	 * @return 解析节点的属性值(如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectAttr(Document doc,String query, String attrName){
		return selectNode(doc, query,attrName,"");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Element el,String query, String attrName){
		return selectNode(el, query,attrName,"");
	}
	
	/**
	 * 选取指定节点的属性值
	 * 
	 */
	public static String selectAttr(Elements els,String query, String attrName){
		return selectNode(els, query,attrName,"");
	}
	
	/**
	 * 选取指定节点的value值
	 * 
	 * @description:  
	 * @param doc jsoup-Document 文档对象
	 * @param query 选择器语句
	 * @param defult 解析失败或者结果节点value值不存在时返回默认值
	 * @return 解析节点的属性值 (如果不存在则返回空串"")
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectValue(Document doc,String query){
		return selectNode(doc, query, "value", "");
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Element el,String query){
		return selectNode(el, query,"value","");
	}
	/**
	 *  选取指定节点的value值
	 * 
	 */
	public static String selectValue(Elements els,String query){
		return selectNode(els, query,"value","");
	}
	
	/**
	 * jsoup选择器
	 * 
	 * @description: 
	 * @param node Document/Element/Elemnts对象
	 * @param query 查询语句
	 * @param attrName 要抓取的属性，如果要获得text值，填text
	 * @param defaultVal 如果属性值不存在，返回此值
	 * @return 抓去指定节点的指定属性值或者text内容
	 * @date 2017年12月11日
	 * @author 舒超
	 */
	public static String selectNode(Object node, String query, String attrName, String defaultVal){
		String str = "";
		try {
			if(Document.class.isInstance(node)){
				if("text".equals(attrName)){
					str = ((Document)node).select(query).text();
				}else{
					str = ((Document)node).select(query).attr(attrName);
				}
			}else if(Element.class.isInstance(node)){
				if("text".equals(attrName)){
					str = ((Element)node).select(query).text();
				}else{
					str = ((Element)node).select(query).attr(attrName);
				}
			}else if(Elements.class.isInstance(node)){
				if("text".equals(attrName)){
					str = ((Elements)node).select(query).text();
				}else{
					str = ((Elements)node).select(query).attr(attrName);
				}
			} 
		} catch (Exception e) {
			str = defaultVal;
		}
		
		if(str==null || str.trim().equals("")){
			str = defaultVal;
		}
		return str.trim();
	}
	
	/**
	 * 获取表单参数集合
	 * <br><br>
	 * 
	 * @param document
	 * @return
	 * @date 2018年1月11日
	 * @author 舒超
	 */
	public static Map getFormParam(Document document, String queryPath) {
		Map<String, String> map = new HashMap<String, String>();
		if(document==null) return map;
		Elements els = document.select(queryPath);
		els = els.select("input[name]");
		if(els==null || els.size()<1) return map;
		for(Element e: els){
			String name = e.attr("name");
			String value = e.val();
			if(value!=null) value = value.trim();
			if(name!=null){
				name = name.trim();
				map.put(name, value);
			}
		}
		return map;
	}
	/**
	 * 获取表单参数集合
	 * <br><br>
	 * 
	 * @param document
	 * @return
	 * @date 2018年1月11日
	 * @author 舒超
	 */
	public static Map<String, String> getFormParam(Document document) {
		Map<String, String> map = new HashMap<String, String>();
		if(document==null) return map;
		Elements els = document.select("input[name]");
		if(els==null || els.size()<1) return map;
		for(Element e: els){
			String name = e.attr("name");
			String value = e.val();
			if(value!=null) value = value.trim();
			if(name!=null){
				name = name.trim();
				map.put(name, value);
			}
		}
		return map;
	}
	
	/**
	 * 获取表单参数集合
	 * <br><br>
	 * 
	 * @param html
	 * @return
	 * @date 2018年1月11日
	 * @author 舒超
	 */
	public static Map<String, String> getFormParam(String html) {
		Document document = parse(html);
		return getFormParam(document);
	}
	
	/**
	 * 根据元素集合获取元素文本列表集合
	 * <br><br>
	 * 
	 * @param els
	 * @return
	 * @date 2018年1月11日
	 * @author 舒超
	 */
	public static List<String> getTableTitle(Elements els){
		List<String> titleList = new ArrayList<String>();
		if(els != null && els.size() > 0) {
			for(Element e: els){
				String text = e.text();
				if(text==null) text="";
				titleList.add(text.trim());
			}
		}
		return titleList;
	}
	
	/**
	 * 获取文档节点值
	 * <br><br>
	 * 
	 * @param html 
	 * @param query 查询节点
	 * @return
	 * @date 2018年1月11日
	 * @author 舒超
	 */
	public static String selectText(String html, String query) {
		return selectText(parse(html), query);
	}
	
	/**
	 * 
	 * <br><br>
	 * 移除文档中某个节点
	 * @param document
	 * @param query
	 * @return
	 * @date 2018年3月1日
	 * @author 舒超
	 */
	public static boolean removeEl(Element element, String query) {
		try {
			element.select(query).remove();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/** 移除某些节点 */
	public static boolean removeEl(Document document, String query) {
		try {
			document.select(query).remove();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/** 移除某些节点 */
	public static boolean removeEl(Elements elements, String query) {
		try {
			elements.select(query).remove();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
