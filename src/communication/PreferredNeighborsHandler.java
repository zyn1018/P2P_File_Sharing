package communication;

import peer.NeighborInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreferredNeighborsHandler implements Runnable {
    Set<NeighborInfo> neighborSet;

    public PreferredNeighborsHandler (Map neighborInfoList) {
        neighborSet = new HashSet<NeighborInfo>();
    }
    public void run() {


    }
}
