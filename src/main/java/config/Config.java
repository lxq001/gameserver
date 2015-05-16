package config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import network.NetworkChannelInitializer;

import org.junit.Test;

import packet.C2SPacket;
import utils.FileUtils;
import utils.OP;

public class Config {
	public static final String MAIN_PORT_KEY = "main.port";
	public static final String BOSS_THREAD_KEY = "boss.thread";
	public static final String WORK_THREAD_KEY = "work.thread";
	
	
	NetworkChannelInitializer<Channel> channelInitializer = new NetworkChannelInitializer<Channel>();
	private static final Map<String,String> confings = new HashMap<String,String>();
	
	private static final Map<String,String> ops = new HashMap<String, String>(); 
	
	/**
	 * 加载配置文件
	 * @param filename
	 */
	public static void initConfig(String filename){
		ResourceBundle bundle = ResourceBundle.getBundle(filename);
		
		System.out.println(bundle.getBaseBundleName());
		Enumeration<String> enumeration = bundle.getKeys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String value = bundle.getString(key);
			confings.put(key, value);
		}
	}
	@SuppressWarnings("unchecked")
	public static void initInBoundPacket(String packetName,String prefix){
		try {
			List<String> simplenames = FileUtils.createObjectFromJarOrFile(packetName, prefix);
			for (String simplename : simplenames) {
				Class<C2SPacket> clazz = (Class<C2SPacket>) Class.forName(simplename);
				//过期标记
				if(clazz.isAnnotationPresent(Deprecated.class)){
					continue;
				}
				OP[] opArr = clazz.getAnnotationsByType(OP.class);
				if(opArr.length > 0){
					String op = opArr[0].op();
					if(op.equals(simplename)){
						ops.put(op, simplename);
					}
				}
				
				
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
}
