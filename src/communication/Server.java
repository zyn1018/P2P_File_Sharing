package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server implements Runnable {
    private int listenport;
    private ServerSocket serverSocket;
    private static final int ACCEPT_TIMEOUT = 1000;
    private CommunicationManager communicationManager;

    public Server(int port, CommunicationManager communicationManager) throws UnknownHostException, IOException {
        this.listenport = port;
        this.serverSocket = new ServerSocket(port);
        this.communicationManager = communicationManager;
        //this.serverSocket.setSoTimeout(ACCEPT_TIMEOUT);
    }

    public void run() {
        System.out.println("Start listening on port:" + this.listenport + ".");

        //listen for connections from other peers
        while (true) {
            System.out.println("Listen...");
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                Client newClient = new Client(socket);
                Thread handlerThread = new Thread(new MessageHandler(newClient, this.communicationManager));
                handlerThread.start();
            }
            /*catch(InterruptedIOException iioex){
        		try{
        			this.serverSocket.close();
        			break;
        		} 
        		catch (IOException e){ 
        			e.printStackTrace();  
                }
            }*/ catch (IOException e) {
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