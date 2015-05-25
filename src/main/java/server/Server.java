package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import network.NetworkChannelInitializer;
import config.Config;

public class Server {
	
	static ServerBootstrap bootstrap = new ServerBootstrap();
	public static void main(String[] args) {
		Config.init();
		bootstrap.group(Config.BOSS_EVENTLOOPGROUP,Config.WORK_EVENTLOOPGROUP);
		//配置生成的channel类型
		bootstrap.channel(NioServerSocketChannel.class);
		//配置监听端口
		bootstrap.localAddress(Integer.parseInt(Config.netty_configs.get(Config.MAIN_PORT_KEY)));
		
		bootstrap.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		
		bootstrap.childHandler(new NetworkChannelInitializer<Channel>());
		try {
			ChannelFuture future = bootstrap.bind().sync();
			System.out.println("listen the port ...." );
			future.channel().closeFuture().sync();
			System.out.println("server is closed ...." );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			Config.BOSS_EVENTLOOPGROUP.shutdownGracefully();
			Config.WORK_EVENTLOOPGROUP.shutdownGracefully();
		}
		
	}
}
