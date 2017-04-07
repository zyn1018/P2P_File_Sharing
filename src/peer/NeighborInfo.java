package peer;

import java.net.Socket;

public class NeighborInfo implements Comparable{
    private int peerID;
    private Socket socket;
    private String hostName;
    private int port;
    private boolean preferred;
    private boolean optimisticallyUnchoked;
    private double downloadRate;
    private boolean hasCompleteFile;
    private boolean isInterested;

    public NeighborInfo(int peerID, String hostName, int port, Boolean hasCompleteFile) {
        this.peerID = peerID;
        this.hostName = hostName;
        this.port = port;
        this.hasCompleteFile = hasCompleteFile;
    }

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public boolean isOptimisticallyUnchoked() {
        return optimisticallyUnchoked;
    }

    public void setOptimisticallyUnchoked(boolean optimisticallyUnchoked) {
        this.optimisticallyUnchoked = optimisticallyUnchoked;
    }

    public double getDownloadRate() {
        return downloadRate;
    }

    public void setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
    }

    public boolean isHasCompleteFile() { return hasCompleteFile;}

    public void setHasCompleteFile(boolean hasCompleteFile) { this.hasCompleteFile = hasCompleteFile;}

    public boolean isInterested() { return isInterested;}

    public void setInterested(boolean interested) { isInterested = interested;}


    public String getHostName() { return hostName;}

    public void setHostName(String hostName) { this.hostName = hostName;}

    public int getPort() { return port;}

    public void setPort(int port) { this.port = port;}

    @Override
    public int compareTo(Object o) {
        NeighborInfo neighborInfo = (NeighborInfo)o;
        return (int) (this.downloadRate - neighborInfo.downloadRate);
    }
}