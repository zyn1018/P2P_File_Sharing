package communication;

import config.Commoncfg;
import config.PeerInfo;
import file.Bitfield;
import file.FileManager;
import message.Message;
import peer.NeighborInfo;

import java.io.IOException;
import java.util.Map;

public class CommunicationManager {
    private int myPeerID;
    private Map<Integer, NeighborInfo> neighborList;
    private Commoncfg commoncfg;
    private int pieceNum;
    private int pieceSize;
    private int lastPieceSize;
    //private boolean hasFile;
    private Bitfield myBitfield;
    private FileManager file;
    private Map<Integer, PeerInfo> peers;

    public CommunicationManager(int myPeerID, Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborList, Bitfield bitfield, Map<Integer, PeerInfo> peers) {
        this.commoncfg = commoncfg;
        this.neighborList = neighborList;
        this.myPeerID = myPeerID;
        //this.hasFile = hasFile;
        this.pieceSize = commoncfg.getPieceSize();
        int fileSize = commoncfg.getFileSize();

        this.pieceNum = fileSize / this.pieceSize;
        int value = fileSize % this.pieceSize;
        if (value == 0) {
            this.lastPieceSize = this.pieceSize;
        } else {
            this.lastPieceSize = value;
            this.pieceNum = this.pieceNum + 1;
        }
        this.myBitfield = bitfield;
        try {
            this.file = new FileManager(this.myPeerID, this.commoncfg.getFileName(), this.commoncfg.getFileSize(), this.pieceSize);
            this.file.createFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.peers = peers;
    }


    public Map<Integer, PeerInfo> getPeers() {
        return peers;
    }


    public void setPeers(Map<Integer, PeerInfo> peers) {
        this.peers = peers;
    }


    public FileManager getFile() {
        return file;
    }


    public void setFile(FileManager file) {
        this.file = file;
    }


    public Commoncfg getCommoncfg() {
        return commoncfg;
    }


    public void setCommoncfg(Commoncfg commoncfg) {
        this.commoncfg = commoncfg;
    }


    public Bitfield getMyBitfield() {
        return myBitfield;
    }


    public void setMyBitfield(Bitfield myBitfield) {
        this.myBitfield = myBitfield;
    }


    public void AddClient(int peerID, Client client) {
        this.neighborList.get(peerID).setClient(client);
    }

    public void sendToAll(Message msg) {
        for (Integer peerID : this.neighborList.keySet()) {
            this.neighborList.get(peerID).getClient().send(msg.getMessageBytes());
        }
    }

    public boolean isQuit() {
        boolean quit;
        if (this.neighborList.size() != this.peers.size() - 1) {
            quit = false;
            return quit;
        }
        if (this.myBitfield.isHasCompleteFile()) {
            quit = true;
        } else {
            quit = false;
        }
        for (Integer peerID : this.neighborList.keySet()) {
            if (!this.neighborList.get(peerID).isHasCompleteFile()) {
                quit = false;
            }
        }
        return quit;

    }

    public Map<Integer, NeighborInfo> getNeighborList() {
        return neighborList;
    }

    public void setNeighborList(Map<Integer, NeighborInfo> neighborList) {
        this.neighborList = neighborList;
    }

    public int getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(int pieceNum) {
        this.pieceNum = pieceNum;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public void setPieceSize(int pieceSize) {
        this.pieceSize = pieceSize;
    }

    public int getLastPieceSize() {
        return lastPieceSize;
    }

    public void setLastPieceSize(int lastPieceSize) {
        this.lastPieceSize = lastPieceSize;
    }

    public void setMyPeerID(int myPeerID) {
        this.myPeerID = myPeerID;
    }

    public int getMyPeerID() {
        return myPeerID;
    }


}