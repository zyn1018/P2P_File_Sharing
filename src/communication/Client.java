package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

	private Socket socket;
	private OutputStream outputStream;
	private PrintWriter printWriter;
	private InputStream inputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private String IP_ADDR;   
    private int PORT;
	
    public Client(String IP_ADDR, int PORT){
    	this.IP_ADDR = IP_ADDR;
    	this.PORT = PORT;
    }
    
    private void connect() {
        try {
        	System.out.println("Connect to peer:" + IP_ADDR +".");
            socket=new Socket(IP_ADDR, PORT);
            outputStream = socket.getOutputStream();
            printWriter=new PrintWriter(outputStream);
            inputStream=socket.getInputStream();
            inputStreamReader=new InputStreamReader(inputStream);
            bufferedReader=new BufferedReader(inputStreamReader);            
        } 
        catch (UnknownHostException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void run() {
		connect();
		try {
            printWriter.print("Hello");
            printWriter.flush();
            socket.shutdownOutput();
            String info="";
            String temp=null;
            
            while((temp=bufferedReader.readLine())!=null){
            	info+=temp;
            	System.out.println("Message:"+info);
            }
		} 
		catch (UnknownHostException e) {
			 e.printStackTrace();
		} 
		catch (IOException e) {
			 e.printStackTrace();
		}
		finally{
			try{
				bufferedReader.close();
				inputStream.close();
				printWriter.close();
				outputStream.close();
				socket.close();
			}
			catch (UnknownHostException e) {
	            e.printStackTrace();
	        } 
			catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
}