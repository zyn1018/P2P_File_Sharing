package message;

import java.io.IOException;

import message.MessageType;

public class ActualMessage extends Message{
	protected static final long serialVersionUID = 3L;
	protected int msgLength;
	protected MessageType msgType = null;
        
	public ActualMessage(MessageType msgType) throws InterruptedException, IOException{
		this.msgLength = 1;
		this.msgType = msgType;
	}
        
	public ActualMessage(int msgLength, MessageType msgType) throws InterruptedException, IOException{
		this.msgType = msgType;
		this.msgLength = msgLength;
	}
	
	public int getMsgLength(){
		return msgLength;
	}

	public void setMsgLength(int msgLength){
		this.msgLength = msgLength;
	}

	public void setMsgType(MessageType msgType){
		this.msgType = msgType;
	}
	
	public MessageType getMsgType(){
		return msgType;
	}
    
	public int getMessageLength(){
    	return this.msgLength;
	}

	@Override
	public long getUID(){
		// TODO Auto-generated method stub
		return serialVersionUID;
	}
}
