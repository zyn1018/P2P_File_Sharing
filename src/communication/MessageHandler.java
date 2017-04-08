package communication;

import java.io.IOException;
import java.net.Socket;

public class MessageHandler implements Runnable{
	private Socket socket;
	
	public MessageHandler(Socket socket){
		this.socket = socket;
	}
	
	@Override
	public void run() {

		try{
			System.out.println("Socket: "+socket.getInetAddress()+": "+socket.getPort());
			System.out.println("MessageHandler Running...");
			Thread.sleep(10000);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		while(true){
			try{
				Thread.sleep(10000);
			}
			catch (Exception e){
				e.printStackTrace();
				try {
					this.socket.close();
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			}
		}
	}
}