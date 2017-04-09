package communication;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import config.Commoncfg;
import peer.NeighborInfo;

public class CommunicationManager{
	private int myPeerID;
	private Map<Integer, NeighborInfo> neighborList;
	private Commoncfg commoncfg;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public CommunicationManager(int myPeerID, Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborList){
		this.commoncfg = commoncfg;
		this.neighborList = neighborList;
		this.myPeerID = myPeerID;
	}
	
	public int getMyPeerID(){
		return myPeerID;
	}
	
	public void sendToAll(){
		
	}
	
}