package communication;

import config.Commoncfg;
import message.ChokeMessage;
import message.Message;
import message.UnchokeMessage;
import peer.NeighborInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimisticalUnchokedHandler implements Runnable {

    Map<Integer, NeighborInfo> neighborsMap;
    Map<Integer, NeighborInfo> interestedNeighborMap;
    List<NeighborInfo> chokedNeighbors;
    NeighborInfo optimisticalUnchokedNeighbor;
    Commoncfg commoncfg;


    public OptimisticalUnchokedHandler(Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborsMap) {
        this.neighborsMap = neighborsMap;
        interestedNeighborMap = new HashMap<>();
        chokedNeighbors = new ArrayList<>();
        this.commoncfg = commoncfg;
    }


    public void run() {
        try {
            selectOptimisticalUnchokedNeighbor();
            Thread.sleep(commoncfg.getOptimistic_Unchoking_Interval() * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void selectOptimisticalUnchokedNeighbor() {
        NeighborInfo prevOptimisticalUnchokedNeighbor = getPrevOptimisticalUnchokedNeighbor();

        getInteresetedNeighbor();
        getChokedNeighbors();

        if (prevOptimisticalUnchokedNeighbor != null) {
            Message msg = null;
            try {
                msg = new ChokeMessage();
                neighborsMap.get(prevOptimisticalUnchokedNeighbor.getPeerID()).setOptimisticallyUnchoked(false);
                neighborsMap.get(prevOptimisticalUnchokedNeighbor.getPeerID()).getClient().send(msg.getMessageBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (chokedNeighbors.size() > 0) {
            optimisticalUnchokedNeighbor = chokedNeighbors.get((int) Math.random() * chokedNeighbors.size());
            Message msg = null;
            try {
                msg = new UnchokeMessage();
                neighborsMap.get(optimisticalUnchokedNeighbor.getPeerID()).setOptimisticallyUnchoked(true);
                neighborsMap.get(optimisticalUnchokedNeighbor.getPeerID()).getClient().send(msg.getMessageBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getChokedNeighbors() {

        chokedNeighbors.clear();

        for (Integer peerID : interestedNeighborMap.keySet()) {
            NeighborInfo neighbor = interestedNeighborMap.get(peerID);
            if (neighbor.isChoked()) {
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

            if (neighbor.isInterest() == true) {
                interestedNeighborMap.put(peerID, neighbor);
            }
        }
    }
}