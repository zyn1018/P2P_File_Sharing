package peer;

import communication.Client;
import communication.Server;
import config.Commoncfg;
import config.Parser;
import config.PeerInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class peerProcess {
    static int peerID;
    static int port;
    static Commoncfg commoncfg;
    static List<PeerInfo> peers;
    static Map<Integer, NeighborInfo> neighborList;

    public static void main(String[] args) throws IOException {
        String filepath = "Common.cfg";
        Parser parser1 = new Parser(filepath);
        commoncfg = parser1.parseCommon();
        String filepath_peer = "PeerInfo.cfg";
        Parser parser2 = new Parser(filepath_peer);
        peers = parser2.parsePeerInfo();
        neighborList = new HashMap<Integer, NeighborInfo>();
        peerID = Integer.valueOf(args[0]);
        System.out.println("peerID = " + peerID);
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
        	if (peer.getPeerID() != peerID) {
                neighborList.put(peer.getPeerID(), new NeighborInfo(peer.getPeerID(), peer.getHostName(), peer.getPort(), peer.getFileStatus()));
            }
            else {
            	port = peer.getPort();
            }
        }
        Thread serverThread = new Thread(new Server(port));
        serverThread.start();
        
        for(Integer aPeerID : neighborList.keySet()){
            if(aPeerID < peerID){
                //peer has already been started, try to make a connection
                Client newClient = new Client (neighborList.get(aPeerID).getHostName(),
                		neighborList.get(aPeerID).getPort());
                Thread clientThread = new Thread(newClient);
                clientThread.start();
            }
        }
    }
}