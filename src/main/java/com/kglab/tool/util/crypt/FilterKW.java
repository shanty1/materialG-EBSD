package com.kglab.tool.util.crypt;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

public class FilterKW {

	public static String filterKw(String kw) {
		if (StringUtils.isBlank(kw))
			return kw;
		kw = Jsoup.parse(kw).text();
		kw = htmlEncode(kw);
		kw = HasInjectionData(kw);
		return kw;
	}

	// / <summary>
	// / 验证是否存在注入代码(条件语句）
	// / </summary>
	// / <param name="inputData"></param>
	public static String HasInjectionData(String inputData) {
		if (StringUtils.isBlank(inputData))
			return "";
		// 里面定义恶意字符集合
		// 验证inputData是否包含恶意集合
		String input = inputData;
		try {
			String reg = " select | insert | delete | update | from |count\\(|drop table|truncate|asc\\(|mid\\(|char\\(|xp_cmdshell|exec   master|netlocalgroup administrators|:|net user|\"\"| or | and ";
			// reg = "[-|;|,|\\/|\\(|\\)|\\[|\\]|\\}|\\{|%|@|\\*|!|\\']";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(inputData);
			while (m.find()) {
				String s0 = m.group();
				inputData = inputData.replace(s0, "");
			}
		} catch (Exception e) {

		}
		input = StringUtils.isNotBlank(inputData) ? inputData : input;
		return input;
	}

	// html标签转化
	public static String htmlEncode(String aText) {
		if (StringUtils.isBlank(aText))
			return aText;
		final StringBuilder result = new StringBuilder();
		final StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '&') {
				result.append("&amp;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static void main(String[] args) throws Exception {
		HasInjectionData("*");
		System.out.println(HasInjectionData("worlde"));
	}
}
