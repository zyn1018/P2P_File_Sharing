package communication;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server implements Runnable{
	private int listenport;
	private ServerSocket serverSocket;
	
	public Server(int port) throws UnknownHostException, IOException {
		this.listenport = port;
		this.serverSocket = new ServerSocket(port);
	        
	        //this.serverSocket.setSoTimeout(ACCEPT_TIMEOUT);
	    }

    public void run() {
        System.out.println("Link Start!.");
        
        while (true) {
        	try {
                Socket socket = serverSocket.accept();
                SocketThread socketThread = new SocketThread(socket);
                socketThread.start();
            }
            catch(InterruptedIOException iioex) {
            	try {
            		this.serverSocket.close();
            		break;
            	} 
            	catch (IOException e){
            		e.printStackTrace();
                            
            	}
            }
        	catch (IOException e) {
        		e.printStackTrace();
        	}
        }   
    }
    
	public int getListenport() {
		return listenport;
	}
	
	public void setListenport(int listenport) {
		this.listenport = listenport;
	}

}