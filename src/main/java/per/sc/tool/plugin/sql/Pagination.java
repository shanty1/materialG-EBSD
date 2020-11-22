package per.sc.tool.plugin.sql;

public class Pagination {
	
	private int pageIndex=1;
	
	private int pageSize=10;
	
	private int pageCount;
	
	private int itemCount;
	
	private Pagination() {super();}
	
	/** 实例化一个分页 ，默认取第一页前10条 */
	public static Pagination newInstance() {
		return newInstance(1, 10);
	}
	public static Pagination newInstance(int pageIndex, int pageSize) {
		Pagination p = new Pagination();
		p.setPageIndex(pageIndex);
		p.setPageSize(pageSize);
		return p;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		double tp= itemCount / (double)pageSize;
		this.pageCount = (int) Math.ceil(tp);
		return pageCount;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public String getMysqlLimitSql() {
		return " limit "+ pageSize +" offset " + (pageIndex-1)*pageSize +" ";
	}
}
