package file;

import java.io.*;

public class FileManager {
    private int ID;
    private File myFile;
    private int FileSize;
    private int PieceSize;
    private int pieceNum;
    private int lastPieceSize;
    private final int SIZEOFEACHCHUNK = 1 << 26; //file written in terms of 64 MB chunks

    public FileManager(int id, String PathofFile, int SizeofFile, int SizeofPiece) throws FileNotFoundException {
        this.setID(id);//peer id
        this.myFile = new File(PathofFile);
        this.FileSize = SizeofFile;
        this.PieceSize = SizeofPiece;
    }

    public boolean createFile() throws IOException {
        if (myFile.exists()) {
            if (!myFile.delete()) {
                return false;
            }
        }
        if (!myFile.createNewFile()) {
            return false;
        }

        FileOutputStream fos = new FileOutputStream(myFile);
        byte[] tempBuffer = new byte[SIZEOFEACHCHUNK];
        for (int i = SIZEOFEACHCHUNK; i < FileSize; i += SIZEOFEACHCHUNK) {
            fos.write(tempBuffer);
        }
        int remainingBytes = FileSize & (SIZEOFEACHCHUNK - 1);
        tempBuffer = new byte[remainingBytes];
        fos.write(tempBuffer);
        fos.close();
        return true;
    }

    /**
     * given a pieceID returns the corresponding chunk
     * data should be already present in file else request message would not come
     */
    public byte[] readPiece(int pieceID) throws IOException {
        RandomAccessFile read = new RandomAccessFile(myFile, "r");
        read.seek(Math.max(pieceID * PieceSize, 0));

        byte[] buffer;
        long remainingBytes = read.length() - (pieceID * PieceSize);
        if (remainingBytes < PieceSize)
            buffer = new byte[(int) remainingBytes];
        else
            buffer = new byte[PieceSize];
        read.read(buffer);
        read.close();
        return buffer;
    }

    public void writePiece(int pieceID, byte[] pieceData) throws IOException {
        RandomAccessFile read = new RandomAccessFile(this.myFile, "rws");
        int offset = pieceID * PieceSize;
        int length = pieceData.length;

        read.write(pieceData, offset, length);
        read.close();
    }

    public void writeLastPiece(byte[] pieceData) throws IOException {
        RandomAccessFile read = new RandomAccessFile(this.myFile, "rws");
        int offset = (pieceNum - 1) * PieceSize;
        int length = lastPieceSize;
        read.write(pieceData, offset, length);
        read.close();

    }

    public byte[] readLastPiece() throws IOException {
        RandomAccessFile read = new RandomAccessFile(myFile, "r");
        read.seek(Math.max((pieceNum - 1) * PieceSize, 0));

        byte[] buffer;
        long remainingBytes = read.length() - ((pieceNum - 1) * PieceSize);
        if (remainingBytes < PieceSize)
            buffer = new byte[(int) remainingBytes];
        else
            buffer = new byte[PieceSize];
        read.read(buffer);
        read.close();
        return buffer;
    }

    public int getPieceNum() {
        return pieceNum;
    }

    public void setPieceNum(int pieceNum) {
        this.pieceNum = pieceNum;
    }

    public int getLastPieceSize() {
        return lastPieceSize;
    }

    public void setLastPieceSize(int lastPieceSize) {
        this.lastPieceSize = lastPieceSize;
    }

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }

    public int getFileSize() {
        return FileSize;
    }

    public void setFileSize(int fileSize) {
        FileSize = fileSize;
    }

    public int getPieceSize() {
        return PieceSize;
    }

    public void setPieceSize(int pieceSize) {
        PieceSize = pieceSize;
    }

    public static boolean createDirectory(String DirectoryPath) {
        File directory = new File(DirectoryPath);
        if (directory.exists()) {
            return true;
        }
        return directory.mkdir();
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }


}