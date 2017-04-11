package file;

import commonutil.Utilities;
import message.BitFieldMessage;
import message.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bitfield {

    private int length;
    private List<Integer> bits;
    private boolean hasCompleteFile;

    public Bitfield(int length, Boolean hasfile) {
        this.length = length;
        bits = new ArrayList<Integer>(length);
        for (int i = 0; i < bits.size(); i++) {
            if (hasfile) {
                bits.set(i, 1);
                this.hasCompleteFile = true;
            } else {
                bits.set(i, 0);
                this.hasCompleteFile = false;
            }
        }
    }

    synchronized public int getBit(int index) {
        return bits.get(index);
    }

    synchronized public void setBit(int index, int value) {
        bits.set(index, value);
        if (this.hasCompleteFile == true) {
            return;
        } else {
            this.hasCompleteFile = true;
            for (int i = 0; i < bits.size(); i++) {
                if (bits.get(i) == 0)
                    this.hasCompleteFile = false;
            }
        }
    }

    synchronized public boolean isHasCompleteFile() {
        return this.hasCompleteFile;
    }

    synchronized public int getPieceNum() {
        int pieceNum = 0;
        for (int i = 0; i < bits.size(); i++) {
            if (bits.get(i) == 1)
                pieceNum++;
        }
        return pieceNum;

    }

    synchronized public Message genBitFieldMessage() throws IOException {
        int size = bits.size();
        int messagelength = bits.size() / 8;
        int leftbits = bits.size() % 8;
        byte[] data;

        if (leftbits == 0) {
            data = new byte[messagelength];
        } else {
            data = new byte[messagelength + 1];
        }

        for (int i = 0; i < messagelength; i++) {
            int value = 0;
            for (int j = 0; j < 8; j++) {
                value = value * 2;
                value = value + bits.get(i * 8 + j);
            }
            data[i] = Utilities.intToByte(value);
        }

        if (leftbits != 0) {
            int value = 0;
            for (int j = 0; j < 8 - leftbits; j++) {
                value = value * 2;
                value = value + bits.get(messagelength * 8 + j);
            }
            for (int j = 0; j < leftbits; j++) {
                value = value * 2;
            }
            data[messagelength + 1] = Utilities.intToByte(value);
        }
        return new BitFieldMessage(data);
    }


}
