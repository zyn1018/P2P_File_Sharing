package communication;

import config.Commoncfg;
import config.PeerInfo;
import peer.NeighborInfo;

import java.util.*;

public class PreferredNeighborsHandler implements Runnable {
    Map<Integer, NeighborInfo> neighborsMap;
    Map<Integer, NeighborInfo> interestedNeighborMap;
    List<NeighborInfo> prefferedNeighbors;
    List<NeighborInfo> downLoadRateList;
    Commoncfg commoncfg;
    PeerInfo myPeerInfo;

    //Constructor
    public PreferredNeighborsHandler(Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborsMap, PeerInfo myPeerInfo) {
        this.myPeerInfo = myPeerInfo;
        this.neighborsMap = neighborsMap;
        this.commoncfg = commoncfg;
        interestedNeighborMap = new HashMap<>();
        downLoadRateList = new ArrayList<>();
        prefferedNeighbors = new ArrayList<>();
    }

    public void run() {
        try {
            selectPrefferedNeighbors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void selectPrefferedNeighbors() throws Exception {

        int count = 0;
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
                prefferedNeighbors.add(newPreffered);
                neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
            }
        } else {
            for (int i = downLoadRateList.size() - 1; i > downLoadRateList.size() - 1 - count; i--) {
                NeighborInfo newPreffered = downLoadRateList.get(i);
                prefferedNeighbors.add(newPreffered);
                neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
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
}
