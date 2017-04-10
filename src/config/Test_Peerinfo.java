package config;

import java.io.IOException;
import java.util.List;

public class Test_Peerinfo {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        String filepath = "PeerInfo.cfg";
        Parser parser = new Parser(filepath);
        List<PeerInfo> peers = parser.parsePeerInfo();
        for (int i = 0; i < peers.size(); i++) {
            System.out.println("Peer " + peers.get(i).getPeerID() + " ----> Host name: " + peers.get(i).getHostName() + "   Port number: "
                    + peers.get(i).getPort() + "   File status: " + peers.get(i).getFileStatus());
        }
    }

}
