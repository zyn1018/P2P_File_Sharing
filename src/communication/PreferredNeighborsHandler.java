package communication;

import config.Commoncfg;
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
    Bitfield myBitfield;


    //Constructor
    public PreferredNeighborsHandler(Commoncfg commoncfg, Map<Integer, NeighborInfo> neighborsMap, Bitfield myBitfield) {
        this.myBitfield = myBitfield;
        this.neighborsMap = neighborsMap;
        this.commoncfg = commoncfg;
        interestedNeighborMap = new HashMap<>();
        downLoadRateList = new ArrayList<>();
        prefferedNeighbors = new HashMap<>();

    }

    public void run() {
        try {
            while (true) {
                selectPrefferedNeighbors();
                System.out.println("Select preffered neighbors as follows: ");
                for (Integer peerID : prefferedNeighbors.keySet()) {
                    System.out.println("Peer: " + peerID);
                }
                Map<Integer, Integer> prevPieceNumMap = getPieceNum();
                Thread.sleep(commoncfg.getUnchoking_Interval() * 1000);
                Map<Integer, Integer> nowPieceNumMap = getPieceNum();
                calculateAndUpdateDownloadRate(prevPieceNumMap, nowPieceNumMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized void selectPrefferedNeighbors() throws Exception {
        if (!neighborsMap.isEmpty()) {
            Map<Integer, NeighborInfo> prevPreffered = getPrevPrefferedNeighbors();
            int count;
            getInteresetedNeighbor();
            sortByDownloadRate();

            if (!prefferedNeighbors.isEmpty()) {
                prefferedNeighbors.clear();
            }

            int numofPreffered = commoncfg.getNum_Of_PreferredNeighbors();
            if (numofPreffered <= 0) {
                throw new Exception("The number of preferred neighbors can not be less or equal than 0 !");
            } else if (numofPreffered >= interestedNeighborMap.size()) {
                count = interestedNeighborMap.size();
            } else {
                count = commoncfg.getNum_Of_PreferredNeighbors();
            }

            if (myBitfield.isHasCompleteFile() == true) {
                Set<Integer> set = new HashSet<Integer>(count);
                while(set.size() < count){
                    Random random = new Random();
                    int index = random.nextInt(downLoadRateList.size());
                    set.add(index);
                }
                Iterator<Integer> iterator = set.iterator();

                while(iterator.hasNext()){
                    NeighborInfo newPreffered = downLoadRateList.get(iterator.next());
                    prefferedNeighbors.put(newPreffered.getPeerID(), newPreffered);
                    neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
                    neighborsMap.get(newPreffered.getPeerID()).setChoked(false);
                }
            } else {
                for (int i = downLoadRateList.size() - 1; i > downLoadRateList.size() - 1 - count; i--) {
                    NeighborInfo newPreffered = downLoadRateList.get(i);
                    prefferedNeighbors.put(newPreffered.getPeerID(), newPreffered);
                    neighborsMap.get(newPreffered.getPeerID()).setPreferred(true);
                    neighborsMap.get(newPreffered.getPeerID()).setChoked(false);

                }
            }

            if (prevPreffered != null) {
                for (Integer peerID : prefferedNeighbors.keySet()) {
                   if (!prevPreffered.containsKey(peerID)) {
                        Message msg = new UnchokeMessage();
                        System.out.println("Send an unchoke message when prevPreffered != null");
                        neighborsMap.get(peerID).getClient().send(msg.getMessageBytes());
                    }
                }
            } else {
                for (Integer peerID : prefferedNeighbors.keySet()) {
                    Message msg = new UnchokeMessage();
                    System.out.println("Send an unchoke message else");
                    neighborsMap.get(peerID).getClient().send(msg.getMessageBytes());

                }
            }

            for (Integer peerID : neighborsMap.keySet()) {
                NeighborInfo neighbor = neighborsMap.get(peerID);
                if ((neighbor.isOptimisticallyUnchoked() == false && neighbor.isChoked() == false)
                        && (neighbor.isPreferred() == false && neighbor.isChoked() == false)) {
                    Message msg = new ChokeMessage();
                    System.out.println("Send a choke message ");
                    neighbor.getClient().send(msg.getMessageBytes());

                }
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

    private Map<Integer, Integer> getPieceNum() {
        Map<Integer, Integer> pieceNumMap = new HashMap<>();
        for (Integer peerID : neighborsMap.keySet()) {
            NeighborInfo neighbor = neighborsMap.get(peerID);
            if (neighbor != null) {
                pieceNumMap.put(neighbor.getPeerID(), neighbor.getDownloadedPieceNum());
            }
        }
        return pieceNumMap;
    }

    private void calculateAndUpdateDownloadRate(Map<Integer, Integer> prev, Map<Integer, Integer> now) {
        for (Integer peerID : prev.keySet()) {
            int pieceNumDiff = now.get(peerID) - prev.get(peerID);
            if (pieceNumDiff >= 0) {
                double downloadRate = pieceNumDiff * commoncfg.getPieceSize() * 1.0 / commoncfg.getUnchoking_Interval();
                neighborsMap.get(peerID).setDownloadRate(downloadRate);
            }
        }
    }

    private Map<Integer, NeighborInfo> getPrevPrefferedNeighbors() {
    	Map<Integer, NeighborInfo> prev = new HashMap<Integer,NeighborInfo>();
        for(Integer peerID: this.prefferedNeighbors.keySet()){
        	prev.put(peerID, this.prefferedNeighbors.get(peerID));
        }
        return prev;
    }
}
