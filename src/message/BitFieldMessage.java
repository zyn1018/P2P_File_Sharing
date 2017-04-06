package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class BitFieldMessage extends ActualMessage{
	private static final long serialVersionUID = 9L;
	private byte[] bitfield;
	  
	public BitFieldMessage(byte[] bitfield) throws IOException, InterruptedException{
		super(1 + bitfield.length, MessageType.bitfield);
		this.bitfield = bitfield;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(Utilities.intToByteArray(this.msgLength));
		out.write(this.msgType.getMessageType());
		out.write(this.bitfield);
        this.messageBytes = out.toByteArray();
        out.close();
	}
	
	public byte[] getBitfield(){
		return bitfield;
	}
	
	public void setBitfield(byte[] bitfield){
		this.bitfield = bitfield;
	}
	
	  
}
