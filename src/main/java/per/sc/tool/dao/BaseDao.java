package per.sc.tool.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import per.sc.tool.dao.BaseDao.SqlWhereStatementValues;
import per.sc.tool.entity.mapper.dao.ColumnMapper;
import per.sc.tool.plugin.sql.Pagination;
/**
 * dao 基类
 * @author shuchao
 * @date   2019年3月8日
 * @param <T>
 */

public abstract class BaseDao<T> {

	@Autowired  
	protected JdbcTemplate jdbcTemplate;
	
	// 主键ID(不适用于联合主键表)
	@NotNull
	protected final String ID_NAME = setIDName();
	// 映射的列
	@NotNull
	protected final String COLUMNS_NAME = setColumnsName();
	// 映射的表名
	@NotNull
	protected final String TABLE_NAME = setTableName();
	
	/** 设置表名 */
	protected abstract String setTableName();
	/** 设置主键ID(不适用于联合主键表) */
	protected abstract String setIDName();
	/** 设置表的列，以逗号分隔，首尾不能有多余的逗号 */
	protected abstract String setColumnsName();
	/** 复写映射处理 
	 * @throws SQLException */
	protected abstract T handleMapper(ResultSet rs, int rowNum/* 结果个数 */) throws SQLException;
	
	/**默认查询语句(无检索条件)*/
	protected final String DEFAULT_QUERY_STRING = "select "+ COLUMNS_NAME + " from "+ TABLE_NAME +" ";

	
	/** 实例化一个结果结果映射集 */
	protected ThisRowMapper newInstanceRowMapper() {
		return new ThisRowMapper();
	}
	
	/** 获取列表 
	 * @param pagination 分页查询，传空则查询所有 
	 * */
	@Transactional(readOnly = true)
	public List<T> getList(Pagination pagination){
		return getList(null,pagination);
	}
	@Transactional(readOnly = true)
	public List<T> getList(Pagination pagination,String order){
		return getList(null,pagination,order);
	}
	
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getMapList(ColumnMapper[] columnMappers, Pagination pagination){
		return getMapList(columnMappers, pagination, null);
	}
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getMapList(ColumnMapper[] columnMappers, Pagination pagination, String order){
		// 查询语句
		StringBuffer sql = new StringBuffer(DEFAULT_QUERY_STRING);
		// 查询条件
		SqlWhereStatementValues sw = generateSqlWhere(columnMappers);
		sql.append(sw.getSqlWhere());
		// 排序
		if(order!=null) sql.append(order);
		// 分页
		if(pagination!=null) sql.append(pagination.getMysqlLimitSql());
		// 查询
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql.toString(),sw.getValues());
		if(pagination==null) { // 没采用分页，总条数就等于list大小
			int size = list.size();
			pagination=Pagination.newInstance(1,size);
			pagination.setItemCount(size);
		}else {				// 采用分页，需计算总条数
			pagination.setItemCount(getCount(columnMappers));
		}
		return list;
	}
	/** 根据条件获取列表 
	 * @param condition key:列名, value:列值
	 * */
	@Transactional(readOnly = true)
	public List<T> getList(ColumnMapper[] columnMappers, Pagination pagination){
		return getList(columnMappers, pagination,null);
	}
	/** 根据条件获取列表 
	 * @param condition key:列名, value:列值
	 * */
	@Transactional(readOnly = true)
	public List<T> getList(ColumnMapper[] columnMappers, Pagination pagination,String order){
		// 查询语句
		StringBuffer sql = new StringBuffer(DEFAULT_QUERY_STRING);
		// 查询条件
		SqlWhereStatementValues sw = generateSqlWhere(columnMappers);
		sql.append(sw.getSqlWhere());
		// 排序
		if(order!=null) sql.append(order);
		// 分页
		if(pagination!=null) sql.append(pagination.getMysqlLimitSql());
		// 查询
		List<T> list = jdbcTemplate.query(sql.toString(), sw.getValues(), newInstanceRowMapper());
		if(pagination==null) {
			int size = list.size();
			pagination=Pagination.newInstance(1,size);
			pagination.setItemCount(size);
		}else {
			pagination.setItemCount(getCount(columnMappers));
		}
		return list;
	}

	/** 通过主键值获取单个(如有多个返回第一个)*/
	@Transactional(readOnly = true)
	public T getById(Object id){
		return getOne(new ColumnMapper[] {new ColumnMapper(this.ID_NAME, id)});
	}
	
	/** 根据条件获取单个(如有多个返回第一个)
	 * @param condition key:列名, value:列值
	 * */
	@Transactional(readOnly = true)
	public T getOne(ColumnMapper[] columnMappers){
		List<T> list = getList(columnMappers,null);
		if(list!=null && list.size()>0) 
			return list.get(0);
		else 
			return null;
	};
	

	/** 插入数据*/
	public abstract int insert(final T entity);
	
	/** 插入数据
	 * @param columnMappers 插入数据列
	 * */
	public int insert(ColumnMapper[] columnMappers) {
		if(columnMappers==null || columnMappers.length<=0) return 0;
		StringBuffer sql = new StringBuffer("insert into "+ TABLE_NAME+"(");
		StringBuffer placeholder = new StringBuffer();
		int length = columnMappers.length;
		Object[] args = new Object[length];
		for(int i=0; i<length; i++) {
			ColumnMapper column = columnMappers[i];
			sql.append(column.getColumnName());
			sql.append(",");
			placeholder.append("?,");
			args[i] = column.getColumnValue();
		}
		sql.delete(sql.lastIndexOf(","),sql.length());
		placeholder.delete(placeholder.lastIndexOf(","),placeholder.length());
		sql.append(") values(").append(placeholder).append(")");
		return jdbcTemplate.update(sql.toString(), args);
	}

	/**
	 * 更新信息
	 */
	public abstract int update(final T entity) ;
	
	/**
	 * 更新信息
	 */
	public int update(Object id, ColumnMapper[] columnMappers) {
		if(columnMappers==null || columnMappers.length<=0) return 0;
		StringBuffer sql = new StringBuffer("update "+TABLE_NAME+" set ");
		int length = columnMappers.length;
		Object[] args = new Object[length+1];
		for(int i=0; i<length; i++) {
			ColumnMapper column = columnMappers[i];
			sql.append(column.getColumnName());
			sql.append("=?,");
			args[i] = column.getColumnValue();
		}
		sql.delete(sql.lastIndexOf(","),sql.length());
		sql.append(" where ").append(ID_NAME).append("=?");
		args[length] = id;
		return jdbcTemplate.update(sql.toString(), args);
	};
	

	/** 删除 */
	public int deleteById(final Object id) {
		String sql = "delete from "+ TABLE_NAME +" where "+ ID_NAME +" = ? ";
		int rowsNum = jdbcTemplate.update(sql,new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, id);
			}});
		return rowsNum;
	}
	/** 删除 */
	public int deleteByColumn(ColumnMapper[] columnMappers) {
		SqlWhereStatementValues sw = generateSqlWhere(columnMappers);
		String sql = "delete from "+ TABLE_NAME + sw.getSqlWhere();
		int rowsNum = jdbcTemplate.update(sql,sw.getValues());
		return rowsNum;
	}

	/** 获取记录数 */
	public Integer getCount() {
		return getCount(null);
	}
	/** 获取记录数 */
	public Integer getCount(ColumnMapper[] columnMappers) {
		SqlWhereStatementValues sw = generateSqlWhere(columnMappers);
		String sql = "select count("+ ID_NAME +") from "+ TABLE_NAME + sw.getSqlWhere();
		return jdbcTemplate.queryForObject(sql, sw.getValues(), Integer.class);
	}
	
	
	/*-------------------------------------抽出公共方法-------------------------------------*/
	
	
	/**
	 * 生成条件语句，例：sqlWhere: where 1=1 and name=? and id=? , values: [root,123]
	 * @author shuchao
	 * @data   2019年3月8日
	 * @param columnMappers
	 * @return SqlWhereStatementValues(非空)
	 */
	protected SqlWhereStatementValues generateSqlWhere(ColumnMapper[] columnMappers) {
		SqlWhereStatementValues sw = new SqlWhereStatementValues();
		if(columnMappers!=null) {
			int length = columnMappers.length;
			if(length>0) {
				Object[] args = new Object[length];
				StringBuffer queryWhere  = new StringBuffer(" where 1=1 ");
				for(int i=0; i<length; i++) {
					ColumnMapper cm = columnMappers[i];
					queryWhere.append(" and ");
					queryWhere.append(cm.getColumnName());
					queryWhere.append(" = ");
					queryWhere.append(" ? ");
					args[i] = cm.getColumnValue();
				}
				sw.setSqlWhere(queryWhere.toString());
				sw.setValues(args);
			}
		}
		return sw;
	}
	
	/*-------------------------------------以下是内部类-------------------------------------*/
	/** 
	 * 私有：结果集映射类
	 *
	 */
	private class ThisRowMapper implements RowMapper<T>{
		@Override
		public T mapRow(ResultSet rs, int rowNum) throws SQLException {
			T t = handleMapper(rs, rowNum) ;
			if(t==null) throw new IllegalArgumentException(t.getClass()+"未建立mapper映射");
			return handleMapper(rs, rowNum);
		}
	}
	
	protected class SqlWhereStatementValues{
		private String sqlWhere = "";
		private Object[] values ;
		
		public SqlWhereStatementValues() {super();}
		public SqlWhereStatementValues(String sqlWhere, Object[] values) {
			super();
			this.sqlWhere = sqlWhere;
			this.values = values;
		}
		public String getSqlWhere() {
			return sqlWhere;
		}
		public void setSqlWhere(String sqlWhere) {
			this.sqlWhere = sqlWhere;
		}
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}
		public boolean isEmpty() {
			if(sqlWhere==null||sqlWhere.trim().isEmpty()) return true;
			else return false;
		}
	}
}
