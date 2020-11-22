package per.sc.tool.util.base;

import java.text.NumberFormat;

public class NumberUtil {

	/**
	 * 计算百分比(精确到小数点后两位)
	 * @author shuchao
	 * @data   2019年3月6日
	 * @param numerator 分子
	 * @param denominator 分母
	 */
	public static float calculatedPercentage(long numerator, long denominator) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		String result = numberFormat.format((float) numerator / (float) denominator * 100);
		return Float.valueOf(result);
	}
	public static void main(String[] args) {
		System.out.println(calculatedPercentage(234, 232));
	}
}
