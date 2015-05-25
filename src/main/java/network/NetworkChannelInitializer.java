package network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetworkChannelInitializer<T> extends ChannelInitializer<Channel> {
	EventExecutorGroup excutorGroup = new DefaultEventExecutorGroup(256);

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
//		pipline.addLast("idl",new IdleStateHandler(60, 0, 0));
		//去除length字段，只保留要解析的数据包
		pipline.addLast("lengthFrameDeCoder",new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
		pipline.addLast(excutorGroup, "networkHandler", new NetworkHandler());
		
	}

}
