package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class RequestMessage extends ActualMessage {
	private static final long serialVersionUID = 10L;
	private int index;
	
	public RequestMessage(int index) throws IOException, InterruptedException{
		super(5,MessageType.request);
		this.index = index;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(Utilities.intToByteArray(this.msgLength));
		out.write(this.msgType.getMessageType());
		out.write(Utilities.intToByteArray(this.index));
		this.messageBytes = out.toByteArray();
		out.close();
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	  
}
