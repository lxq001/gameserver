package packet;

import io.netty.buffer.ByteBuf;

public abstract class InBoundPacket extends Packet {

	public void hander() {
		read();
	}

	protected abstract void read();

}
