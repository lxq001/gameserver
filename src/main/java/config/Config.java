package config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

public class Config {
	public static final String MAIN_PORT_KEY = "main.port";
	public static final String BOSS_THREAD_KEY = "boss.thread";
	public static final String WORK_THREAD_KEY = "work.thread";
	
	
	GateWayChannelInitializer<Channel> channelInitializer = new GateWayChannelInitializer<Channel>();
	private static final Map<String,String> confings = new HashMap<String,String>();
	
	/**
	 * 加载配置文件
	 * @param filename
	 */
	public static void initConfig(String filename){
		ResourceBundle bundle = ResourceBundle.getBundle(filename);
		
		Enumeration<String> enumeration = bundle.getKeys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String value = bundle.getString(key);
			confings.put(key, value);
		}
	}
	
	
	
	
}
