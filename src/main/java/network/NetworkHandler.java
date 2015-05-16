package network;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import config.Config;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NetworkHandler extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(!in.isReadable()){
			return;
		}
		int op = in.readShort();
		String op_hex = Integer.toHexString(op);
		
		
	}

}
