package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class ChokeMessage extends ActualMessage{
    private static final long serialVersionUID = 4L;

    public ChokeMessage() throws IOException{
        super(1,MessageType.choke);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(Utilities.intToByteArray(this.msgLength));
		out.write(this.msgType.getMessageType());
        this.messageBytes = out.toByteArray();
        out.close();
    }
}