
package message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected byte[] messageBytes;
    protected static final long serialVersionUID = 1L;

    public Message() {
    }

    public Message(byte[] msg) {
        this.messageBytes = msg;
    }

    public byte[] getMessageBytes() {
        return messageBytes;
    }

    abstract long getUID();
}
