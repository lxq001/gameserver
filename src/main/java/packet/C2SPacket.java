package packet;

import io.netty.buffer.ByteBuf;

public abstract class C2SPacket extends Packet{
	/**
	 * 数据
	 */
	private ByteBuf data ;
	
	private void read(){
		int op = data.readShort();
		//根据op 映射到对应的packet
		
		
	}
}
