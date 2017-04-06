package config;

/*
 * Content in Common.cfg
 */
public class Commoncfg {
	int Num_Of_PreferredNeighbors;
	int Unchoking_Interval;
	int Optimistic_Unchoking_Interval;
	String fileName;
	int fileSize;
	int pieceSize;

	public Commoncfg() {

	}

	public Commoncfg(int nop, int ui, int oui, String fn, int fs, int ps) {
		Num_Of_PreferredNeighbors = nop;
		Unchoking_Interval = ui;
		Optimistic_Unchoking_Interval = oui;
		fileName = fn;
		fileSize = fs;
		pieceSize = ps;
	}

	public int getNum_Of_PreferredNeighbors() {
		return Num_Of_PreferredNeighbors;
	}

	public void setNum_Of_PreferredNeighbors(int num_Of_PreferredNeighbors) {
		Num_Of_PreferredNeighbors = num_Of_PreferredNeighbors;
	}

	public int getUnchoking_Interval() {
		return Unchoking_Interval;
	}

	public void setUnchoking_Interval(int unchoking_Interval) {
		Unchoking_Interval = unchoking_Interval;
	}

	public int getOptimistic_Unchoking_Interval() {
		return Optimistic_Unchoking_Interval;
	}

	public void setOptimistic_Unchoking_Interval(int optimistic_Unchoking_Interval) {
		Optimistic_Unchoking_Interval = optimistic_Unchoking_Interval;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getPieceSize() {
		return pieceSize;
	}

	public void setPieceSize(int pieceSize) {
		this.pieceSize = pieceSize;
	}

}
