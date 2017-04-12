import communication.*;
import config.Commoncfg;
import config.Parser;
import config.PeerInfo;
import file.Bitfield;
import log.Logger;
import peer.NeighborInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerProcess {
    private int myPeerID;
    private int myPort;
    private Commoncfg commoncfg;
    private Map<Integer, PeerInfo> peers;
    private Map<Integer, NeighborInfo> neighborList;
    private CommunicationManager communicationManager;
    private boolean hasFile;
    private Bitfield myBitfield;
    private int pieceSize;
    private int pieceNum;
    private int lastPieceSize;
    private Logger logger;

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
            System.out.println("OptimisticUnchokingInt;rval = " + commoncfg.getOptimistic_Unchoking_Interval() + "s");
            System.out.println("FileName is " + commoncfg.getFileName());
            System.out.println("FileSize = " + commoncfg.getFileSize() + "bytes");
            System.out.println("PieceSize = " + commoncfg.getPieceSize() + "bytes");
            this.pieceSize = commoncfg.getPieceSize();
            int fileSize = commoncfg.getFileSize();
            logger = new Logger();

            this.pieceNum = fileSize / this.pieceSize;
            int value = fileSize % this.pieceSize;
            if (value == 0) {
                this.lastPieceSize = this.pieceSize;
            } else {
                this.lastPieceSize = value;
                this.pieceNum = this.pieceNum + 1;
            }
            for (Integer peerID : peers.keySet()) {
                System.out.println("Peer " + peers.get(peerID).getPeerID() + " ----> Host name: " + peers.get(peerID).getHostName() + "   Port number: "
                        + peers.get(peerID).getPort() + "   File status: " + peers.get(peerID).getFileStatus());
            }
            for (Integer peerID : peers.keySet()) {
                PeerInfo peer = peers.get(peerID);
                if (peer.getPeerID() != myPeerID) {
                    //neighborList.put(peer.getPeerID(), new NeighborInfo(peer.getPeerID(), peer.getHostName(), peer.getPort(), peer.getFileStatus()));
                } else {
                    myPort = peer.getPort();
                    hasFile = peer.getFileStatus();
                    this.myBitfield = new Bitfield(this.pieceNum, this.hasFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProcess() {
        Thread serverThread;
        logger.CreateLog("" + myPeerID);
        try {
            communicationManager = new CommunicationManager(myPeerID, commoncfg, neighborList, myBitfield, peers);
            serverThread = new Thread(new Server(myPort, communicationManager, logger));
            serverThread.start();


            for (Integer peerID : peers.keySet()) {
                if (peerID < myPeerID) {
                    //peer has already been started, try to make a connection
                    Client newClient = new Client(peers.get(peerID).getHostName(),
                            peers.get(peerID).getPort());
                    Thread handlerThread = new Thread(new MessageHandler(peerID, newClient, communicationManager, logger));
                    newClient.connect();
                    handlerThread.start();
                    logger.TcpConnectionOutgoing("" + myPeerID, "" + peerID);

                }
            }

            Thread preferredThread = new Thread(new PreferredNeighborsHandler(commoncfg, neighborList, myBitfield, logger, myPeerID));
            Thread optimisticThread = new Thread(new OptimisticalUnchokedHandler(commoncfg, neighborList, logger, myPeerID));
            preferredThread.start();
            optimisticThread.start();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        PeerProcess peer = new PeerProcess(Integer.valueOf(args[0]));
        peer.startProcess();
    }
}