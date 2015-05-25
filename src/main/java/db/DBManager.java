package db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v2.c3p0.DataSources;

import config.Config;

public class DBManager {

	private static DataSource dataSource = null;
	public static DataSource getDataSource(){
		return dataSource;
	}
	/**
	 * 生成dataSource
	 * @param jdbcUrl
	 * @param configData
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException 
	 */
	public static  DataSource createDataSource(Map<String,String> configData) throws SQLException, ClassNotFoundException{
		if(dataSource != null)
			return dataSource;
		DbUtils.loadDriver(configData.get("driverClass"));
		//生成datasource
		dataSource = DataSources.unpooledDataSource(configData.get("jdbcUrl"),configData.get("user"),configData.get("password"));
		//配置datasource参数,其中jdbcurl，user，password不被setting
		DataSources.pooledDataSource(dataSource, configData);
		return dataSource;
		
	}
	
	public static Connection openConnect() throws SQLException{
		return dataSource.getConnection();
	}
	/**
	 * 释放数据源
	 * @throws SQLException
	 */
	public static void destroyDataSource() throws SQLException{
		if(dataSource == null)
			return;
		DataSources.destroy(dataSource);
	}
	/**
	 * 关闭connection
	 * @param connection
	 * @throws SQLException
	 */
	public static void closeConnect(Connection connection) throws SQLException{
		if(connection == null)
			return;
		ConnectionUtils.attemptClose(connection);
	}
	/**
	 * 更新提交
	 * @param connection
	 * @throws SQLException
	 */
	public static void commit(Connection connection) throws SQLException{
		if(connection == null)
			return;
		connection.commit();
	}
	
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Map<String,String> map = new HashMap<String, String>(); 
		Config.initConfig("c3p0_config", map);
		createDataSource(map);
		
//		Address address1 = new Address("hhh");
//		Address address2= new Address("l:l:l");
//		List<Address> list = new ArrayList<Address>();
//		list.add(address1);
//		list.add(address2);
//		Person p = new Person(0L,1L,"lin",32,null);
//		p.setAddress(list);
//		QueryUtils.writePojo(p, DBHandleType.DEL);
//		Person p = QueryUtils.readPojo(Person.class, 1L);
		
//		System.out.println(p.getAge());
	}
	
	
}
