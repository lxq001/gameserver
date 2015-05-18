package network;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import packet.InBoundPacket;
import utils.OPUtils;
import config.Config;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NetworkHandler extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		try {
			if (!in.isReadable()) {
				return;
			}
			int op = in.readShort();
			InBoundPacket inBound = (InBoundPacket) OPUtils.getInstance(op + "");
			inBound.setData(in);
			inBound.hander();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
