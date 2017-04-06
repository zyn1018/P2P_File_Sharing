package message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;


public class PieceMessage extends ActualMessage{
	private static final long serialVersionUID = 11L;
	private int index;
	private byte[] content;
	
	public PieceMessage(int index,byte[] content) throws IOException, InterruptedException{
	        super(5 + content.length, MessageType.piece);
	        this.index = index;
	        this.content = content;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(Utilities.intToByteArray(this.msgLength));
			out.write(this.msgType.getMessageType());
			out.write(Utilities.intToByteArray(this.index));
			out.write(this.content);
			this.messageBytes = out.toByteArray();
			out.close();
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	  
}
