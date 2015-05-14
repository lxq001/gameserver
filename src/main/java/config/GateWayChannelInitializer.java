package config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class GateWayChannelInitializer<T> extends ChannelInitializer<Channel> {


	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipline = ch.pipeline();
		pipline.addLast("idl",new IdleStateHandler(60, 0, 0));
		//去除length字段，只保留要解析的数据包
		pipline.addLast("",new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));
		
	}

}
