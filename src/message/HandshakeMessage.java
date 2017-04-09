package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import commonutil.Utilities;

public class HandshakeMessage extends Message{
	private static final long serialVersionUID = 2L;
	private static String header = "P2PFILESHARINGPROJ";
	private static byte[] zero= {0,0,0,0,0,0,0,0,0,0};
	private int peerID;
	
	public HandshakeMessage(int peerID) throws IOException{
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         out.write(header.getBytes());
         out.write(zero);  //10 bytes zero bits
         this.peerID = peerID;
         out.write(Utilities.intToByteArray(peerID));
         this.messageBytes = out.toByteArray();
         out.close();
    }

	public int getPeerID(){
		return peerID;
	}

	public void setPeerID(int peerID){
		this.peerID = peerID;
	}
	
	@Override
	public long getUID(){
		return serialVersionUID;
	}
}
