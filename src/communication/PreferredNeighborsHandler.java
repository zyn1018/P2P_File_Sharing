package communication;

import config.Commoncfg;
import peer.NeighborInfo;

import java.util.*;

public class PreferredNeighborsHandler implements Runnable {
    Map<Integer, NeighborInfo> neighborsMap;
    Map<Integer, NeighborInfo> interestedNeighborMap;
    Set<NeighborInfo> prefferedNeighbors;
    List<NeighborInfo> downLoadRateList;
    Commoncfg commoncfg;

    //Constructor
    public PreferredNeighborsHandler(Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborsMap) {
        this.neighborsMap = neighborsMap;
        this.commoncfg = commoncfg;
        interestedNeighborMap = new HashMap<>();
        downLoadRateList = new ArrayList<>();
        prefferedNeighbors = new HashSet<>();

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
        prefferedNeighbors.clear();
        int numofPreffered = commoncfg.getNum_Of_PreferredNeighbors();

        if (numofPreffered <= 0) {
            throw new Exception("The number of preferredNeighbors can not be less or equal than 0 !");
        } else if (numofPreffered >= interestedNeighborMap.size()) {
            count = interestedNeighborMap.size();
        } else {
            count = commoncfg.getNum_Of_PreferredNeighbors();
        }

        for (int i = downLoadRateList.size() - 1; i > downLoadRateList.size() - 1 - count; i--) {
            prefferedNeighbors.add(downLoadRateList.get(i));
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
        for (Integer peerID : neighborsMap.keySet()) {
            if (neighborsMap.get(peerID).isInterested() == true) {
                interestedNeighborMap.put(peerID, neighborsMap.get(peerID));
            }
        }
    }
}
