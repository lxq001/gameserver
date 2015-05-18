package packet.out;

import io.netty.buffer.ByteBuf;
import packet.OutBoundPacket;

public class OutBound_Test extends OutBoundPacket{



	@Override
	public void writeData(ByteBuf tmpBuf) {
		tmpBuf.writeByte(1);
		tmpBuf.writeByte(2);
		tmpBuf.writeByte(3);
		tmpBuf.writeByte(4);
	}

	@Override
	protected int getOP() {
		return 0;
	}
public static void main(String[] args) {
	new OutBound_Test().fireMessage();
}
}
