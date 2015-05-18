package packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public abstract class OutBoundPacket extends Packet {
	private Channel channel = null;
	/**
	 * 写出数据
	 */
	public  void fireMessage(){
		data = Unpooled.buffer();
		//写入全部数据
		write();
		//channel.writeAndFlush(data);
		
	}

	private void write() {
		//TODO 获取临时的bytebuf用来计算数据的长度(使用对象池来进行优化)
		ByteBuf tmpBuf = Unpooled.buffer();
		//写入OP
		writeOP(tmpBuf);
		
		//写入业务数据
		writeData(tmpBuf);
		
		//写入长度
		int length = writeLength(tmpBuf);
		copyTo(tmpBuf,length);
		
		
	}

	private void copyTo(ByteBuf tmpBuf,int length) {
		//写入业务的数据
		data.writeBytes(tmpBuf, length);
		//释放临时bytebuf
		tmpBuf.release();
		
	}

	private int writeLength(ByteBuf tmpBuf) {
		int length = tmpBuf.readableBytes();
		//写入长度
		data.writeInt(length);
		return length;
		
	}

	public abstract void writeData(ByteBuf tmpBuf) ;

	private void writeOP(ByteBuf tmpBuf) {
		tmpBuf.writeShort(getOP());
	}
	protected abstract int getOP();
	
	
}
