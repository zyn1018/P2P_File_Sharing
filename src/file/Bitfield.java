package file;

import java.util.ArrayList;
import java.util.List;

public class Bitfield {

    private int length;
    private List<Boolean> bits;
    
    public Bitfield(int length,Boolean hasfile){
    	this.length = length;
    	bits = new ArrayList<Boolean>(length);
    	for(int i = 0; i < bits.size(); i++){
    		if(hasfile){
    			bits.set(i, true);
    		}
    		else{
    			bits.set(i, false);
    		}
    	}
    }
    
    public Boolean getBit(int index){
    	return bits.get(index);
    }
    
    public void setBit(int index, Boolean value){
    	bits.set(index, value);
    }
    
    
}
