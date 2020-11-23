package com.kglab.tool.entity.mapper.dao;
/**
 * 列映射(特点：列名不为空)
 * @author shuchao
 * @date   2019年3月8日
 */
public class ColumnMapper {

	private String columnName;
	private Object columnValue;
	
	
	public ColumnMapper(String columnName) {
		this(columnName,null);
	}
	
	public ColumnMapper(String columnName, Object columnValue) {
		super();
		if(columnName==null || columnName.trim().isEmpty())
			throw new IllegalArgumentException("columnName can't be set empty ! ");
		this.columnName = columnName;
		this.columnValue = columnValue;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		if(columnName==null || columnName.trim().isEmpty())
			throw new IllegalArgumentException("columnName can't be set empty ! ");
		this.columnName = columnName;
	}
	public Object getColumnValue() {
		return columnValue;
	}
	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}
	
}
