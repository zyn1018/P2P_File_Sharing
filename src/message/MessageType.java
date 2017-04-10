package message;

import java.io.Serializable;

public enum MessageType implements Serializable {
    choke(0),
    unchoke(1),
    interested(2),
    notinterested(3),
    have(4),
    bitfield(5),
    request(6),
    piece(7);

    private byte msgType;

    private MessageType(int msgType) {
        this.msgType = (byte) msgType;
    }

   /* public static MessageType getMessageType(int value){
        switch(value){
    		case 0: return MessageType.choke;
    		case 1: return MessageType.unchoke; 
    		case 2: return MessageType.interested;  
    		case 3: return MessageType.notinterested;  
		   	case 4: return MessageType.have;  
		   	case 5: return MessageType.bitfield; 
		   	case 6: return MessageType.request;  
		   	case 7: return MessageType.piece; 
		   	default: return null; 
		 }   
    }*/

    public byte getMessageType() {
        return this.msgType;
    }
}