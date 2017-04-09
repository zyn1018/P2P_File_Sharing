package communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PipedInputStream;
import java.net.Socket;

import message.Message;
import message.HandshakeMessage;
import message.ActualMessage;
import commonutil.Utilities;

public class MessageHandler implements Runnable{
	private int connectedPeerID;
	private int myPeerID;
	private CommunicationManager communicationManager;
	private Client client;
	private PipedInputStream inputFromClient;
	
	public MessageHandler(Client client,CommunicationManager communicationManager){
		this.client = client;
		this.communicationManager = communicationManager;
		this.inputFromClient = new PipedInputStream();
		this.myPeerID = this.communicationManager.getMyPeerID();
	}
	
	public MessageHandler(int connectedPeerID, Client client,CommunicationManager communicationManager){
		this.connectedPeerID = connectedPeerID;
		this.client = client;
		this.communicationManager = communicationManager;
		this.inputFromClient = new PipedInputStream();
		this.myPeerID = this.communicationManager.getMyPeerID();
	}
	
	public void sendHandShake(){
		Message msg;
		try {
			msg = new HandshakeMessage(myPeerID);
			client.send(msg.getMessageBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public void receiveAndProcessHandShake(){
		try {
			this.client.receiveHandShake();
			byte[] handshakeMsg = new byte[32];
			this.inputFromClient.read(handshakeMsg);
			byte[] peerIDBuffer = new byte[4];
			peerIDBuffer[0] = handshakeMsg[28];
			peerIDBuffer[1] = handshakeMsg[29];
			peerIDBuffer[2] = handshakeMsg[30];
			peerIDBuffer[3] = handshakeMsg[31];
			int peerID = Utilities.byteArrayToInt(peerIDBuffer);
			System.out.println("Receive HandShake from peer" + peerID + ".");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			System.out.println("9999999999");
			this.inputFromClient.connect(this.client.getOutputToHandler());
			this.sendHandShake();
			this.receiveAndProcessHandShake();
			Thread clientThread = new Thread(this.client);
			clientThread.start();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		while(true){
			
		}
	}
}