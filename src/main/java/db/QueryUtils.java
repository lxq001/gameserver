package db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.fastjson.JSONObject;

import db.pojo.Pojo;

@SuppressWarnings({"unchecked" })
public class QueryUtils {

	static DataSource dataSource = DBManager.getDataSource();
	static QueryRunner runner = new QueryRunner(dataSource);

	private static Connection startTransfer() throws SQLException {
		Connection connection = DBManager.openConnect();
		connection.setAutoCommit(false);
		return connection;
	}

	private static void commit(Connection connection) throws SQLException {
		connection.commit();
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param qt
	 * @param clazz
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	static <T> T query(String sql, QueryType qt, Class<T> clazz, Object... params) throws SQLException {

		ResultSetHandler<T> handler = choiseRSHandler(qt, clazz);
		if(params == null ||params.length == 0)
			return runner.query(sql, handler);
		return runner.query(sql, handler, params);
	}

	private static <T> ResultSetHandler<T> choiseRSHandler(QueryType qt, Class<T> clazz) {
		ResultSetHandler<T> handler = qt.handlerIns(clazz);
		return handler;
	}
	

	/**
	 * 查询单条数据用map存储
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Object> queryMap(String sql, Object... params) throws SQLException {
		return (Map<String, Object>) query(sql, QueryType.MAP, null, params);
	}

	/**
	 * 查询多条数据 list<map>
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> queryMaps(String sql, Object... params) throws SQLException {
		return (List<Map<String, Object>>) query(sql, QueryType.MAPLIST, null, params);
	}
	/**
	 * 查询多条数据 list<map>
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> queryMapsNoParams(String sql) throws SQLException {
		return (List<Map<String, Object>>) query(sql, QueryType.MAPLIST, null);
	}

	/**
	 * 查询一条数据，组装成bean
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static <T> T queryBean(String sql, Class<T> clazz, Object... params) throws SQLException {
		return query(sql, QueryType.BEAN, clazz, params);
	}

	/**
	 * 查询多条数据，组装成bean
	 * 
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static <T> List<T> queryBeans(String sql, Class<T> clazz, Object... params) throws SQLException {
		return (List<T>) query(sql, QueryType.BEANLIST, clazz, params);

	}

	/**
	 * 增、删、改 数据行为
	 * 
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void update(String sql, Object... params) throws SQLException {
		Connection connection = null;
		try {
			connection = startTransfer();
			runner.update(sql, params);
			commit(connection);
		} finally {
			DbUtils.closeQuietly(connection);
		}
	}

	/**
	 * 一般以key-value的形式进行保存，增、删、改 数据行为
	 * 
	 * @param pojo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SQLException
	 */
	public static void writePojo(Pojo pojo, DBHandleType dbht) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, SQLException {
		//将pojo的名字做为表名
		String tabName = pojo.getClass().getSimpleName();
		String key = getTabKey(tabName);
		String value = getTabValue(tabName);

		writePo(pojo, dbht, tabName, key, value);

	}

	private static void writePo(Pojo pojo, DBHandleType dbht, String tabName, String key, String value)
			throws SQLException {
		String writeStr;
		String sql = "";
		switch (dbht) {
		case DEL:
			sql = "delete from " + tabName + " where " + key + "=?";
			update(sql, pojo.getId());
			break;
		case INSERT:
			writeStr = JSONObject.toJSONString(pojo);
			sql = "insert into " + tabName + "(" + key + "," + value + ") values(?,?)";
			update(sql, pojo.getId(), writeStr);
			break;
		case UPDATE:
			writeStr = JSONObject.toJSONString(pojo);
			sql = "update " + tabName + " set " + value + "=? where " + key + "=?;";
			update(sql, writeStr, pojo.getId());
			break;
		default:
			break;
		}
	}

	/**
	 * 读取pojos 多数据查询
	 * 
	 * @param clazz
	 * @param fields 匹配的表的字段
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException 
	 */
	public static <T> List<T> readPojo(Class<T> clazz,String[] fields,Object[][] params ) throws SQLException, IllegalAccessException {
		
		if(clazz == null)
			throw new IllegalAccessException("the clazz is null");
		startTransfer();
		String tabName = getTabName(clazz);
//		String key = getTabKey(tabName);
		String value = getTabValue(tabName);
		//查询当条数据
		String sql = "select * from " + tabName ; 
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			//参数填充
			if(params[i]!=null && params[i].length >= 1){
				StringBuilder queryparams = new StringBuilder();
				for (int j = 0; j < params[i].length; j++) {
					queryparams.append(params[i][j]).append(",");
				}
				String c = queryparams.subSequence(0, queryparams.length()-1).toString();
				if(i == 0){
					sql += "where "+ field+"in ("+c+")";
				}else{
					sql += "and " + field + "in ("+c+")";
				}
			}
		}
		
		List<Map<String, Object>> list = queryMaps(sql);
		if(list == null || list.isEmpty())
			return null;
		List<T> pojos = new ArrayList<T>();
		for (Map<String, Object> m : list) {
			String pojo2Str = (String) m.get(value);
			T pojo = JSONObject.parseObject(pojo2Str, clazz);
			if(pojo == null)
				continue;
			pojos.add(pojo);
		}
		return pojos;
		
	}
	/**
	 * 用pojo的唯一表示符获取pojo
	 * @param clazz
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static <T> T readPojo(Class<T> clazz,Long id) throws IllegalAccessException, SQLException{
		if(id == null){
			throw new IllegalAccessException("id is null");
		}
		startTransfer();
		String tabName = getTabName(clazz);
		String key = getTabKey(tabName);
		String value = getTabValue(tabName);
		String sql = "select * from "+ tabName + "where " + key + "=?";
		Map<String,Object> map = queryMap(sql, id);
		if(map == null || map.isEmpty())
			return null;
		return JSONObject.parseObject(map.get(value).toString(), clazz);
		
	}
	
	

	private static String getTabValue(String tabName) {
		String value = tabName + "_VAL";
		return value;
	}

	private static String getTabKey(String tabName) {
		String key = tabName + "_ID";
		return key;
	}

	private static <T> String getTabName(Class<T> clazz) {
		String tabName = clazz.getSimpleName();
		return tabName;
	}
	

}

enum DBHandleType {
	DEL, UPDATE, INSERT
}

@SuppressWarnings({ "rawtypes", "unchecked" })
enum QueryType {
	BEAN {

		@Override
		public <T> ResultSetHandler<T> handlerIns(Class<T> clazz) {
			return new BeanHandler(clazz);
		}
	},
	BEANLIST {
		public <T> ResultSetHandler<T> handlerIns(Class<T> clazz) {
			return new BeanListHandler(clazz);
		}
	},
	MAP {

		@Override
		public ResultSetHandler handlerIns(Class clazz) {
			return new MapHandler();
		}
	},
	MAPLIST {
		@Override
		public ResultSetHandler handlerIns(Class clazz) {

			return new MapListHandler();
		}
	};
	public abstract <T> ResultSetHandler<T> handlerIns(Class<T> clazz);
}
