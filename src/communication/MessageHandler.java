package communication;

import commonutil.Utilities;
import file.Bitfield;
import message.HandshakeMessage;
import message.Message;

import java.io.IOException;
import java.io.PipedInputStream;

public class MessageHandler implements Runnable {
    private int connectedPeerID;
    private int myPeerID;
    private CommunicationManager communicationManager;
    private Client client;
    private PipedInputStream inputFromClient;
    private Bitfield myBitField;
    private boolean amIchoked = true;
    private boolean amIinterested;

    public MessageHandler(Client client, CommunicationManager communicationManager) {
        this.client = client;
        this.communicationManager = communicationManager;
        this.inputFromClient = new PipedInputStream();
        this.myPeerID = this.communicationManager.getMyPeerID();
        this.myBitField = new Bitfield(communicationManager.getPieceNum(), communicationManager.isHasFile());
    }

    public MessageHandler(int connectedPeerID, Client client, CommunicationManager communicationManager) {
        this.connectedPeerID = connectedPeerID;
        this.client = client;
        this.communicationManager = communicationManager;
        this.inputFromClient = new PipedInputStream();
        this.myPeerID = this.communicationManager.getMyPeerID();
        this.myBitField = new Bitfield(communicationManager.getPieceNum(), communicationManager.isHasFile());
    }

    public void sendHandShake() {
        Message msg;
        try {
            msg = new HandshakeMessage(myPeerID);
            client.send(msg.getMessageBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message msg) {
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
            this.communicationManager.AddClient(connectedPeerID, this.client);
            this.sendMessage(this.myBitField.genBitFieldMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processChokeMessage() {
        amIchoked = true;
        communicationManager.getNeighborList().get(connectedPeerID).setChoke(true);
    }

    public void processUnchokeMessage() {
        amIchoked = false;
        communicationManager.getNeighborList().get(connectedPeerID).setChoke(false);
    }

    public void processInterestedMessage() {
        amIinterested = true;
        communicationManager.getNeighborList().get(connectedPeerID).setInterest(true);
    }

    public void processUninterestedMessage() {
        amIinterested = false;
        communicationManager.getNeighborList().get(connectedPeerID).setInterest(false);
    }


    @Override
    public void run() {
        try {
            this.inputFromClient.connect(this.client.getOutputToHandler());
            this.sendHandShake();
            this.receiveAndProcessHandShake();
            Thread clientThread = new Thread(this.client);
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

        }
    }
}