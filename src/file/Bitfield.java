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

    public Bitfield(int length, Boolean hasfile) {
        this.length = length;
        bits = new ArrayList<Integer>(length);
        for (int i = 0; i < bits.size(); i++) {
            if (hasfile) {
                bits.set(i, 1);
            } else {
                bits.set(i, 0);
            }
        }
    }

    public int getBit(int index) {
        return bits.get(index);
    }

    public void setBit(int index, int value) {
        bits.set(index, value);
    }

    public Message genBitFieldMessage() throws IOException {
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
