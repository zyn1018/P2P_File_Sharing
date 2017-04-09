package communication;

/**
 * Created by yinan on 4/8/17.
 */
public class PeersCommunicator implements Runnable {
    OptimisticalUnchokedHandler optimisticalUnchokedHandler;
    PreferredNeighborsHandler preferredNeighborsHandler;

    public PeersCommunicator(OptimisticalUnchokedHandler optimisticalUnchokedHandler, PreferredNeighborsHandler preferredNeighborsHandler) {
        this.optimisticalUnchokedHandler = optimisticalUnchokedHandler;
        this.preferredNeighborsHandler = preferredNeighborsHandler;
    }


    @Override
    public void run() {

    }


    private void receiveInterested() {

    }
}
