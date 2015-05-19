package db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import config.Config;

public class DataManager {

	private static DataSource dataSource = null;
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
		//生成datasource
		dataSource = DataSources.unpooledDataSource(configData.get("jdbcUrl"));
		//配置datasource参数
		DataSources.pooledDataSource(dataSource, configData);
		Class.forName(configData.get("driverClass"));
		return dataSource;
		
	}
	
	public static Connection openConnect() throws SQLException{
		return dataSource.getConnection();
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Map<String,String> map = new HashMap<String, String>(); 
		Config.initConfig("c3p0_config", map);
		createDataSource(map);
		Connection connection = openConnect();
		PreparedStatement ps = connection.prepareStatement("select * from PlayerCache");
		ps.execute();
		System.out.println(ps.getMaxRows());
	}
	
	
}
