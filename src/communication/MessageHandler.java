package communication;

import commonutil.Utilities;
import file.Bitfield;
import file.FileManager;
import message.*;
import peer.NeighborInfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MessageHandler implements Runnable {
	private int connectedPeerID;
	private int myPeerID;
	private CommunicationManager communicationManager;
	private Client client;
	private PipedInputStream inputFromClient;
	private Bitfield myBitField;
	private Bitfield connectedPeerBitfield;
	private Set<Integer> interestingPieces;
	private boolean amIChoked = true;
	private boolean amIInterested = false;
	private FileManager dataFile;
	private DataInputStream inputstream;
	int num = 0;

	public MessageHandler(Client client, CommunicationManager communicationManager) {
		this.client = client;
		this.communicationManager = communicationManager;
		this.inputFromClient = new PipedInputStream();
		this.myPeerID = this.communicationManager.getMyPeerID();
		this.myBitField = this.communicationManager.getMyBitfield();
		this.dataFile = this.communicationManager.getFile();
		this.interestingPieces = new HashSet<Integer>();
	}

	public MessageHandler(int connectedPeerID, Client client, CommunicationManager communicationManager) {
		this.connectedPeerID = connectedPeerID;
		this.client = client;
		this.communicationManager = communicationManager;
		this.inputFromClient = new PipedInputStream();
		this.myPeerID = this.communicationManager.getMyPeerID();
		this.myBitField = this.communicationManager.getMyBitfield();
		this.dataFile = this.communicationManager.getFile();
		this.interestingPieces = new HashSet<Integer>();
	}

	public void sendHandShake() {
		Message msg;
		try {
			msg = new HandshakeMessage(myPeerID);
			client.send(msg.getMessageBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(Message msg) {
		byte[] length = new byte[4];
		client.send(msg.getMessageBytes());
	}

	public void receiveAndProcessHandShake() {
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
			this.connectedPeerID = peerID;

			this.communicationManager.getNeighborList().put(this.connectedPeerID, new NeighborInfo(this.connectedPeerID,
					this.communicationManager.getPeers().get(this.connectedPeerID).getHostName(),
					this.communicationManager.getPeers().get(this.connectedPeerID).getPort(),
					this.communicationManager.getPeers().get(this.connectedPeerID).getFileStatus(), this.client));
			this.sendMessage(this.myBitField.genBitFieldMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processChokeMessage() {
		this.amIChoked = true;
		communicationManager.getNeighborList().get(connectedPeerID).setChoke(true);
		// System.out.println("Receive a Chokemessage!");
	}

	public void processUnchokeMessage() {
		try {
			this.amIChoked = false;
			communicationManager.getNeighborList().get(connectedPeerID).setChoke(false);
			if (!this.interestingPieces.isEmpty()) {
				Integer[] indices = this.interestingPieces.toArray(new Integer[0]);
				Random random = new Random();
				int position = random.nextInt(this.interestingPieces.size());
				int requestIndex = indices[position];
				Message request = new RequestMessage(requestIndex);
				this.sendMessage(request);
			}
			System.out.println("Receive an UnChokemessage!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processInterestedMessage() {
		this.amIInterested = true;
		communicationManager.getNeighborList().get(connectedPeerID).setInterest(true);
		System.out.println("Receive an Interestmessage!");
	}

	public void processNotinterestedMessage() {
		this.amIInterested = false;
		communicationManager.getNeighborList().get(connectedPeerID).setInterest(false);
		System.out.println("Receive a Notinterestmessage!");
	}

	public void processHaveMessage(int length) {
		try {
			byte[] indexBuffer = new byte[4];
			this.inputstream.readFully(indexBuffer);
			int index = Utilities.byteArrayToInt(indexBuffer);
			this.connectedPeerBitfield.setBit(index, 1);
			if (this.myBitField.getBit(index) == 0) {
				this.interestingPieces.add(index);
				if (this.communicationManager.getNeighborList().get(this.connectedPeerID).isInterested() == false) {
					this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(true);
					Message msg = new InterestedMessage();
					this.sendMessage(msg);
				}
			}
			System.out.println("Receive a Havemessage!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processBitFieldMessage(int length) {
		try {
			byte[] bitfieldBuffer = new byte[length];
			this.inputstream.readFully(bitfieldBuffer);
			System.out.println(this.communicationManager.getPieceNum());
			this.connectedPeerBitfield = new Bitfield(this.communicationManager.getPieceNum(), bitfieldBuffer);

			if (this.connectedPeerBitfield.isHasCompleteFile() == false) {
				this.sendMessage(new NotinterestedMessage());
				this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(false);
			} else {
				if (this.myBitField.isHasCompleteFile() == true) {
					this.sendMessage(new NotinterestedMessage());
					this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(false);
				} else {
					for (int i = 0; i < this.communicationManager.getPieceNum(); i++) {
						this.interestingPieces.add(i);
					}
					this.sendMessage(new InterestedMessage());
					this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(true);
				}
			}
			System.out.println("Receive a Bitfieldmessage!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processPieceMessage(int msgLength) {
		try {
			byte[] index = new byte[4];
			this.inputstream.readFully(index);
			int pieceID = Utilities.byteArrayToInt(index);
			byte[] payload = new byte[msgLength - 4];
			this.inputstream.readFully(payload);
			this.connectedPeerBitfield.setBit(pieceID, 1);
			this.myBitField.setBit(pieceID, 1);
			this.communicationManager.getNeighborList().get(this.connectedPeerID).downloadedPieceNumIncrement();
			num++;
			this.dataFile.writePiece(pieceID, payload);
			if (this.myBitField.isHasCompleteFile()) {
				this.sendMessage(new NotinterestedMessage());
				this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(false);
				this.interestingPieces.clear();
			}

			this.interestingPieces.clear();
			for (int i = 0; i < this.communicationManager.getPieceNum(); i++) {
				if (this.myBitField.getBit(i) == 0 && this.connectedPeerBitfield.getBit(i) == 1)
					this.interestingPieces.add(i);
			}

			if (!this.interestingPieces.isEmpty()) {
				Integer[] indices = this.interestingPieces.toArray(new Integer[0]);
				Random random = new Random();
				int position = random.nextInt(this.interestingPieces.size());
				int requestIndex = indices[position];
				Message request = new RequestMessage(requestIndex);
				this.sendMessage(request);
			} else {
				this.sendMessage(new NotinterestedMessage());
				this.communicationManager.getNeighborList().get(this.connectedPeerID).setInterested(false);
			}

			Message msg = new HaveMessage(pieceID);
			communicationManager.sendToAll(msg);

			System.out.println("Receive a Piecemessage!" + num + " " + this.interestingPieces.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processRequestMessage(int msgLength) {
		try {
			byte[] index = new byte[4];
			this.inputstream.readFully(index);
			int pieceID = Utilities.byteArrayToInt(index);
			byte[] data = this.dataFile.readPiece(pieceID);
			if (!this.communicationManager.getNeighborList().get(this.connectedPeerID).isChoked()) {
				Message msg = new PieceMessage(pieceID, data);
				sendMessage(msg);
			}
			System.out.println("Receive a Requestmessage!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processMessage() {
		try {
			byte[] lengthBuffer = new byte[4];
			this.inputstream.readFully(lengthBuffer);
			int length = Utilities.byteArrayToInt(lengthBuffer);

			byte[] type = new byte[1];
			this.inputstream.readFully(type);
			MessageType msgType = MessageType.getMessageType(type[0]);
			if (msgType == null)
				return;

			switch (msgType) {
			case choke:
				processChokeMessage();
				break;
			case unchoke:
				processUnchokeMessage();
				break;
			case interested:
				processInterestedMessage();
				break;
			case notinterested:
				processNotinterestedMessage();
				break;
			case have:
				processHaveMessage(length - 1);
				break;
			case bitfield:
				processBitFieldMessage(length - 1);
				break;
			case request:
				processRequestMessage(length - 1);
				break;
			case piece:
				processPieceMessage(length - 1);
				break;
			default:
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.inputFromClient.connect(this.client.getOutputToHandler());
			this.inputstream = new DataInputStream(this.inputFromClient);
			this.sendHandShake();
			this.receiveAndProcessHandShake();
			Thread clientThread = new Thread(this.client);
			clientThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {
			processMessage();
		}
	}
}