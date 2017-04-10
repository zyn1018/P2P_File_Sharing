package communication;

import config.Commoncfg;
import message.Message;
import peer.NeighborInfo;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CommunicationManager {
    private int myPeerID;
    private Map<Integer, NeighborInfo> neighborList;
    private Commoncfg commoncfg;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int pieceNum;
    private int pieceSize;
    private int lastPieceSize;
    private boolean hasFile;

    public CommunicationManager(int myPeerID, Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborList, boolean hasFile) {
        this.commoncfg = commoncfg;
        this.neighborList = neighborList;
        this.myPeerID = myPeerID;
        this.hasFile = hasFile;
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
    }


    public void AddClient(int peerID, Client client) {
        this.neighborList.get(peerID).setClient(client);
    }

    public void sendToAll(Message msg) {

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

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public void setMyPeerID(int myPeerID) {
        this.myPeerID = myPeerID;
    }

    public int getMyPeerID() {
        return myPeerID;
    }


}