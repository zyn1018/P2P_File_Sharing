package communication;

import config.Commoncfg;
import config.PeerInfo;
import file.Bitfield;
import message.ChokeMessage;
import message.Message;
import message.UnchokeMessage;
import peer.NeighborInfo;

import java.util.*;

public class PreferredNeighborsHandler implements Runnable {
    Map<Integer, NeighborInfo> neighborsMap;
    Map<Integer, NeighborInfo> interestedNeighborMap;
    Map<Integer, NeighborInfo> prefferedNeighbors;
    List<NeighborInfo> downLoadRateList;
    Commoncfg commoncfg;
    PeerInfo myPeerInfo;
    Bitfield bitfield;


    //Constructor
    public PreferredNeighborsHandler(Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborsMap, PeerInfo myPeerInfo, Bitfield bitfield) {
        this.myPeerInfo = myPeerInfo;
        this.neighborsMap = neighborsMap;
        this.commoncfg = commoncfg;
        interestedNeighborMap = new HashMap<>();
        downLoadRateList = new ArrayList<>();
        prefferedNeighbors = new HashMap<>();
        this.bitfield = bitfield;

    }

    public void run() {
        try {

            selectPrefferedNeighbors();

            Thread.sleep(commoncfg.getUnchoking_Interval() * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void selectPrefferedNeighbors() throws Exception {

        Map<Integer, NeighborInfo> prevPreffered = getPrevPrefferedNeighbors();
        int count;
        getInteresetedNeighbor();
        sortByDownloadRate();
        for (int i = 0; i < prefferedNeighbors.size(); i++) {
            neighborsMap.get(prefferedNeighbors.get(i).getPeerID()).setPreferred(false);
        }

        prefferedNeighbors.clear();
        int numofPreffered = commoncfg.getNum_Of_PreferredNeighbors();
        if (numofPreffered <= 0) {
            throw new Exception("The number of preferred neighbors can not be less or equal than 0 !");
        } else if (numofPreffered >= interestedNeighborMap.size()) {
            count = interestedNeighborMap.size();
        } else {
            count = commoncfg.getNum_Of_PreferredNeighbors();
        }

        if (myPeerInfo.getFileStatus() == true) {
            for (int i = 0; i < count; i++) {
                NeighborInfo newPreffered = downLoadRateList.get((int) Math.random() * downLoadRateList.size());
                prefferedNeighbors.put(newPreffered.getPeerID(), newPreffered);
                neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
            }
        } else {
            for (int i = downLoadRateList.size() - 1; i > downLoadRateList.size() - 1 - count; i--) {
                NeighborInfo newPreffered = downLoadRateList.get(i);
                prefferedNeighbors.put(newPreffered.getPeerID(), newPreffered);
                neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
            }
        }

        if (prevPreffered != null) {
            for (Integer peerID : prefferedNeighbors.keySet()) {
                if (!prevPreffered.containsKey(peerID)) {
                    Message msg = new UnchokeMessage();
                    neighborsMap.get(peerID).getClient().send(msg.getMessageBytes());
                }
            }
        } else {
            for (Integer peerID : prefferedNeighbors.keySet()) {
                Message msg = new UnchokeMessage();
                neighborsMap.get(peerID).getClient().send(msg.getMessageBytes());
            }
        }

        for (Integer peerID : neighborsMap.keySet()) {
            NeighborInfo neighbor = neighborsMap.get(peerID);
            if (!neighbor.isOptimisticallyUnchoked() && !neighbor.isPreferred()) {
                Message msg = new ChokeMessage();
                neighbor.getClient().send(msg.getMessageBytes());
            }
        }

    }

    private void sortByDownloadRate() {

        downLoadRateList.clear();

        for (Integer peerID : interestedNeighborMap.keySet()) {
            downLoadRateList.add(interestedNeighborMap.get(peerID));
        }
        Collections.sort(downLoadRateList);
    }

    private void getInteresetedNeighbor() {

        interestedNeighborMap.clear();

        for (Integer peerID : neighborsMap.keySet()) {
            NeighborInfo neighbor = neighborsMap.get(peerID);

            if (neighbor.isInterest() == true) {
                interestedNeighborMap.put(peerID, neighbor);
            }
        }
    }

    private Map<Integer, NeighborInfo> getPrevPrefferedNeighbors() {
        return prefferedNeighbors;
    }
}
