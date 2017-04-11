package file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import commonutil.Utilities;
import message.Message;
import message.BitFieldMessage;

public class Bitfield {

    private int length;
    private List<Integer> bits;
    private boolean hasCompleteFile;
    public Bitfield(int length,Boolean hasfile){
    	this.length = length;
    	bits = new ArrayList<Integer>(length);
    	for(int i = 0; i < this.length; i++){
    		if(hasfile){
    			bits.add(i, 1);
    			this.hasCompleteFile = true;
    		}
    		else{
    			bits.add(i, 0);
    			this.hasCompleteFile = false;
    		}
    	}
    }
    public Bitfield(int length, byte[] bitfieldBuffer){
    	this.length = length;
    	bits = new ArrayList<Integer>(length);
    	for(int i = 0; i < this.length; i++){
    		if(Utilities.byteToInt(bitfieldBuffer[0]) != 0){
    			bits.add(i, 1);
    			this.hasCompleteFile = true;
    		}
    		else{
    			bits.add(i, 0);
    			this.hasCompleteFile = false;
    		}
    	}
    	System.out.println(bits.size());
    }
    
    public int getLength() {
		return length;
	}
    
	synchronized public int getBit(int index){
    	return bits.get(index);
    }
    
    synchronized public void setBit(int index, int value){
    	bits.set(index, value);
    	if(this.hasCompleteFile == true){
    		return;
    	}
    	else{
    		this.hasCompleteFile = true;
    		for(int i = 0;i<bits.size();i++){
    			if(bits.get(i) == 0)
    				this.hasCompleteFile = false;
    		}
    	}
    }
    
    synchronized public boolean isHasCompleteFile(){
    	return this.hasCompleteFile;
    }
    
    synchronized public int getPieceNum(){
    	int pieceNum = 0;
    	for(int i = 0;i<this.length;i++){
			if(bits.get(i) == 1)
				pieceNum++;
		}
    	return pieceNum;
    	
    }
    
    synchronized public Message genBitFieldMessage() throws IOException{ 
    	int size = this.length;
    	int messagelength = this.length/8;
    	int leftbits = this.length%8;
    	byte[] data;
    	
    	if(leftbits == 0){
    		data = new byte[messagelength];
    	}
    	else{
    		data = new byte[messagelength + 1];
    	}
    	
    	for(int i = 0; i < messagelength; i++){
    		int value = 0;
    		for(int j = 0; j < 8; j ++){
    			value = value * 2;
    			value = value + bits.get(i *8 + j);
    		}
    		data[i] = Utilities.intToByte(value);
    	}
    	System.out.println(messagelength);
    	if(leftbits != 0){
    		int value = 0;
    		for(int j = 0; j < leftbits ; j ++){
    			value = value * 2;
    			System.out.println(j);
    			value = value + bits.get(messagelength * 8 + j);
    		}
    		for(int j = 0; j < 8 - leftbits ; j ++){
    			value = value * 2;
    		}
    		data[messagelength] = Utilities.intToByte(value);
    	}
		return new BitFieldMessage(data);
    }

    
}
