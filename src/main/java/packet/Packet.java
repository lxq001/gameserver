package packet;

import io.netty.buffer.ByteBuf;
/**
 * 数据包
 * @author Administrator
 *
 */
public  class Packet {
	
	/**
	 * 数据
	 */
	protected ByteBuf data ;

	public ByteBuf getData() {
		return data;
	}

	public void setData(ByteBuf data) {
		this.data = data;
	}

	public Packet(ByteBuf data) {
		super();
		this.data = data;
	}

	public Packet() {
		super();
	}
	
}
