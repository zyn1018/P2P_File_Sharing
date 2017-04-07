package communication;

import peer.NeighborInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimisticalUnchokedHandler implements Runnable {

    Map<Integer, NeighborInfo> neighborsMap;
    Map<Integer, NeighborInfo> interestedNeighborMap;
    List<NeighborInfo> chokedNeighbors;
    NeighborInfo optimisticalUnchokedNeighbor;


    public OptimisticalUnchokedHandler(Map<Integer, NeighborInfo> neighborsMap) {
        this.neighborsMap = neighborsMap;
        interestedNeighborMap = new HashMap<>();
        chokedNeighbors = new ArrayList<>();
    }


    public void run() {
        try {
            selectOptimisticalUnchokedNeighbor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void selectOptimisticalUnchokedNeighbor() {
        NeighborInfo prevOptimisticalUnchokedNeighbor = getPrevOptimisticalUnchokedNeighbor();

        getInteresetedNeighbor();
        getChokedNeighbors();

        if (prevOptimisticalUnchokedNeighbor != null) {
            neighborsMap.get(prevOptimisticalUnchokedNeighbor.getPeerID()).setOptimisticallyUnchoked(false);
        }

        if (chokedNeighbors.size() > 0) {
            optimisticalUnchokedNeighbor = chokedNeighbors.get((int) Math.random() * chokedNeighbors.size());
            neighborsMap.get(optimisticalUnchokedNeighbor.getPeerID()).setOptimisticallyUnchoked(true);
        }
    }


    private void getChokedNeighbors() {

        chokedNeighbors.clear();

        for (Integer peerID : interestedNeighborMap.keySet()) {
            NeighborInfo neighbor = interestedNeighborMap.get(peerID);
            if (neighbor.isPreferred() == false && neighbor.isOptimisticallyUnchoked() == false) {
                chokedNeighbors.add(neighbor);
            }
        }
    }

    private NeighborInfo getPrevOptimisticalUnchokedNeighbor() {

        for (Integer peerID : neighborsMap.keySet()) {
            NeighborInfo neighbor = neighborsMap.get(peerID);

            if (neighbor.isOptimisticallyUnchoked()) {
                return neighbor;
            }
        }
        return null;
    }

    private void getInteresetedNeighbor() {

        interestedNeighborMap.clear();

        for (Integer peerID : neighborsMap.keySet()) {
            NeighborInfo neighbor = neighborsMap.get(peerID);

            if (neighbor.isInterested() == true) {
                interestedNeighborMap.put(peerID, neighbor);
            }
        }
    }
}