package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class UnchokeMessage extends ActualMessage{
    private static final long serialVersionUID = 5L;

    public UnchokeMessage() throws IOException{
        super(1,MessageType.unchoke);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write(Utilities.intToByteArray(this.msgLength));
		out.write(this.msgType.getMessageType());
        this.messageBytes = out.toByteArray();
        out.close();
    }
}

