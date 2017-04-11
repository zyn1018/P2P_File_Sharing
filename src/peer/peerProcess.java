package peer;

import communication.Client;
import communication.CommunicationManager;
import communication.MessageHandler;
import communication.Server;
import config.Commoncfg;
import config.Parser;
import config.PeerInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerProcess {
    private int myPeerID;
    private int myPort;
    private Commoncfg commoncfg;
    private List<PeerInfo> peers;
    private Map<Integer, NeighborInfo> neighborList;
    private CommunicationManager communicationManager;
    private boolean hasFile;

    public PeerProcess(int myPeerID) {
        try {
            this.myPeerID = myPeerID;
            String filepath = "Common.cfg";
            Parser parser1 = new Parser(filepath);
            commoncfg = parser1.parseCommon();
            String filepath_peer = "PeerInfo.cfg";
            Parser parser2 = new Parser(filepath_peer);
            peers = parser2.parsePeerInfo();
            neighborList = new ConcurrentHashMap<Integer, NeighborInfo>();
            System.out.println("peerID = " + myPeerID);
            System.out.println("NumberOfPreferredNeighbors = " + commoncfg.getNum_Of_PreferredNeighbors());
            System.out.println("UnchokingInterval = " + commoncfg.getUnchoking_Interval() + "s");
            System.out.println("OptimisticUnchokingInterval = " + commoncfg.getOptimistic_Unchoking_Interval() + "s");
            System.out.println("FileName is " + commoncfg.getFileName());
            System.out.println("FileSize = " + commoncfg.getFileSize() + "bytes");
            System.out.println("PieceSize = " + commoncfg.getPieceSize() + "bytes");

            for (int i = 0; i < peers.size(); i++) {
                System.out.println("Peer " + peers.get(i).getPeerID() + " ----> Host name: " + peers.get(i).getHostName() + "   Port number: "
                        + peers.get(i).getPort() + "   File status: " + peers.get(i).getFileStatus());
            }
            for (int i = 0; i < peers.size(); i++) {
                PeerInfo peer = peers.get(i);
                if (peer.getPeerID() != myPeerID) {
                    neighborList.put(peer.getPeerID(), new NeighborInfo(peer.getPeerID(), peer.getHostName(), peer.getPort(), peer.getFileStatus()));
                } else {
                    myPort = peer.getPort();
                    hasFile = peer.getFileStatus();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProcess() {
        Thread serverThread;
        try {
            communicationManager = new CommunicationManager(myPeerID, commoncfg, neighborList, hasFile);
            serverThread = new Thread(new Server(myPort, communicationManager));
            serverThread.start();


            for (Integer peerID : neighborList.keySet()) {
                if (peerID < myPeerID) {
                    System.out.println(peerID);
                    //peer has already been started, try to make a connection
                    Client newClient = new Client(neighborList.get(peerID).getHostName(),
                            neighborList.get(peerID).getPort());
                    Thread handlerThread = new Thread(new MessageHandler(peerID, newClient, communicationManager));
                    newClient.connect();

                    handlerThread.start();
                    neighborList.get(peerID).setClient(newClient);
                }
                System.out.println(peerID + neighborList.get(peerID).getHostName());
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        PeerProcess peer = new PeerProcess(Integer.valueOf(args[0]));
        peer.startProcess();
    }
}