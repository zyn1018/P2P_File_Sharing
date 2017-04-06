package peer;

import java.net.Socket;

public class NeighborInfo {
	private int peerID;
	private Socket socket;
	private boolean preferred;
	private boolean optimisticallyUnchoked;
	private double downloadRate;
	
	public NeighborInfo(int peerID, Socket socket){
		this.peerID = peerID;
		this.socket = socket;
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
	
	
}