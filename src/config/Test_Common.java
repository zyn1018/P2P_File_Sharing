package config;

import java.io.IOException;

public class Test_Common {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filepath = "Common.cfg";
		Parser parser = new Parser(filepath);
		Commoncfg commoncfg = parser.parseCommon();
		System.out.println("NumberOfPreferredNeighbors = " + commoncfg.Num_Of_PreferredNeighbors);
		System.out.println("UnchokingInterval = " + commoncfg.Unchoking_Interval + "s");
		System.out.println("OptimisticUnchokingInterval = " + commoncfg.Optimistic_Unchoking_Interval + "s");
		System.out.println("fileName is " + commoncfg.fileName);
		System.out.println("fileSize = " + commoncfg.fileSize + " bytes");
		System.out.println("pieceSize = " + commoncfg.pieceSize + " bytes");

	}

}
