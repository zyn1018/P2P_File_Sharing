package message;

import commonutil.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NotinterestedMessage extends ActualMessage {
    private static final long serialVersionUID = 7L;

    public NotinterestedMessage() throws IOException {
        super(1, MessageType.notinterested);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(Utilities.intToByteArray(this.msgLength));
        out.write(this.msgType.getMessageType());
        this.messageBytes = out.toByteArray();
        out.close();
    }
}
