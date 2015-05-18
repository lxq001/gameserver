package utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import packet.InBoundPacket;
import config.Config;

public class OPUtils {
	
	public static Object getInstance(String op) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String simpleName = Config.OPs.get(op);
		if(op == null){
			System.out.println("op:["+op +"] is not exist");
			return null;
		}
		
		return Class.forName(simpleName).newInstance();
	}
	
	
	
	/**
	 * 
	 * @param packetname 完整的包名 firstName == null lastname == null
	 * @param prefix 文件的前缀
	 * @param lastName 文件的后缀
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static List<String> createObjectFromJarOrFile(String packetname, String prefix) throws IOException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {
		List<String> container = new ArrayList<String>();

		String path = packetname.replaceAll("\\.", "/");

		if (prefix != null) {
			String simpleName = packetname + "." + prefix.substring(0, prefix.lastIndexOf('.'));
			//			createObject(simpleName, objs);
			intoContainer(simpleName, container);
		} else {
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				String protocol = url.getProtocol();

				// 为普通文件
				if (protocol.equals("file")) {
					File file = new File(url.getPath());
					// 文件夹，获取所有文件
					if (file.isDirectory()) {

						File[] files = file.listFiles(new Filter(prefix));
						for (int i = 0; i < files.length; i++) {
							File f = files[i];
							String simpleName = f.getName();
							//						createObject(simpleName, objs);
							intoContainer(simpleName, container);
						}

					} else {
						throw new IllegalArgumentException("packetname 只能是完整的包，即目录");
					}

				} else if (protocol.equals("jar")) {
					// 获取jar的 连接
					JarURLConnection connection = (JarURLConnection) url.openConnection();
					JarFile jarFile = connection.getJarFile();
					Enumeration<JarEntry> enumeration = jarFile.entries();
					// jar下的所有文件都是全路径，以/为尾的是文件夹
					while (enumeration.hasMoreElements()) {
						JarEntry jarEntry = enumeration.nextElement();
						if (jarEntry.isDirectory()) {
							continue;
						}
						String name = jarEntry.getName();
						// 根目录
						if (name.startsWith("/")) {
							name.substring(1);
						}

						// 不是class文件
						if (!name.endsWith(".class")) {
							continue;
						}
						if (!name.startsWith(path)) {
							continue;
						}
						String simpleName = name.substring(name.lastIndexOf("/"), name.lastIndexOf("."));
						//					createObject(simpleName, objs);
						intoContainer(simpleName, container);

					}
				}
			}
		}
		return container;
	}

	/**
	 * 对所有的simplename进行保存
	 * 
	 * @param simpleName
	 * @param container
	 */
	private static void intoContainer(String simpleName, List<String> container) {
		System.out.println("initOPs------>"+simpleName);
		container.add(simpleName);
	}



}

class Filter implements FilenameFilter {
	private String firstName;

	// private String lastName;

	public Filter(String firstName) {
		super();
		this.firstName = firstName;
		// this.lastName = lastName;
	}

	public boolean accept(File dir, String name) {
		if (firstName != null) {
			if (name.startsWith(firstName)) {
				return true;
			}
		}
		if (name.endsWith(".class")) {
			return true;
		}

		return false;
	}

}
