package config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	BufferedReader br;

	// File path of configuration(Can be any one of Common.cfg and PeerInfo.cfg)
	public Parser(String filePath) throws FileNotFoundException {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
	}

	List<String> commonTokens = new ArrayList<String>();
	List<String> peerTokens = new ArrayList<String>();
	List<PeerInfo> peers = new ArrayList<PeerInfo>();

	/*
	 * Method used to parse Common.cfg
	 */
	public Commoncfg parseCommon() throws IOException {
		Commoncfg commoncfg = new Commoncfg();
		String Line = br.readLine();
		while (Line != null) {
			String[] LineTokens = Line.split(" ");
			commonTokens.add(LineTokens[1]);
			Line = br.readLine();
		}
		commoncfg.setNum_Of_PreferredNeighbors(Integer.parseInt(commonTokens.get(0)));
		commoncfg.setUnchoking_Interval(Integer.parseInt(commonTokens.get(1)));
		commoncfg.setOptimistic_Unchoking_Interval(Integer.parseInt(commonTokens.get(2)));
		commoncfg.setFileName(commonTokens.get(3));
		commoncfg.setFileSize(Integer.parseInt(commonTokens.get(4)));
		commoncfg.setPieceSize(Integer.parseInt(commonTokens.get(5)));
		return commoncfg;
	}

	/*
	 * Method used to parse PeerInfo.cfg, return the map of each peer and its information
	 */
	public List<PeerInfo> parsePeerInfo() throws IOException {

		String Line = br.readLine();
		while (Line != null) {
			PeerInfo peerInfo = new PeerInfo();
			String[] LineTokens = Line.split(" ");
			
			peerInfo.setPeerID(Integer.parseInt(LineTokens[0]));
			peerInfo.setHostName(LineTokens[1]);
			peerInfo.setPort(Integer.parseInt(LineTokens[2]));
			
			if (LineTokens[3].equals("1")) {
				peerInfo.setFileStatus(true);
			} else {
				peerInfo.setFileStatus(false);
			}
			
			peers.add(peerInfo);
			Line = br.readLine();
		}
		return peers;
	}
}