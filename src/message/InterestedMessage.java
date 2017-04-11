package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class InterestedMessage extends ActualMessage{
	private static final long serialVersionUID = 6L;
	  
	public InterestedMessage() throws IOException{
		super(1,MessageType.interested);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(Utilities.intToByteArray(this.msgLength));
		out.write(this.msgType.getMessageType());
		this.messageBytes = out.toByteArray();
		out.close();
	}
}
