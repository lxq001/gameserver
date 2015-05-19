package config;

import io.netty.channel.Channel;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import packet.InBoundPacket;
import utils.OP;
import utils.OPUtils;
import network.NetworkChannelInitializer;

public class Config {
	public static final String MAIN_PORT_KEY = "main.port";
	public static final String BOSS_THREAD_KEY = "boss.thread";
	public static final String WORK_THREAD_KEY = "work.thread";

	NetworkChannelInitializer<Channel> channelInitializer = new NetworkChannelInitializer<Channel>();
	private static  Map<String, String> configs = new HashMap<String, String>();

	public static  Map<String, String> OPs = new HashMap<String, String>();
	public static void init(){
		//netty配置表加载
		initConfig("netty_config", configs);
	}
	/**
	 * 加载配置文件
	 * 
	 * @param filename
	 */
	public static void initConfig(String filename,Map<String,String> map) {
		ResourceBundle bundle = ResourceBundle.getBundle(filename);

		System.out.println(bundle.getBaseBundleName());
		Enumeration<String> enumeration = bundle.getKeys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String value = bundle.getString(key);
			map.put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	public static void initOPs(String packetName, String prefix) {
		try {
			List<String> simplenames = OPUtils.createObjectFromJarOrFile(packetName, prefix);

			for (String simplename : simplenames) {
				Class<InBoundPacket> clazz = null;
				try {
					clazz = (Class<InBoundPacket>) Class.forName(simplename);
				} catch (Exception e) {
					System.out.println("simplename"+simplename+" new instanse fail");
				}
				if(clazz == null)
					continue;
				//过期标记
				if (clazz.isAnnotationPresent(Deprecated.class)) {
					continue;
				}
				OP[] opArr = clazz.getAnnotationsByType(OP.class);
				if (opArr.length > 0) {
					String op = opArr[0].op();
						OPs.put(op, simplename);
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
