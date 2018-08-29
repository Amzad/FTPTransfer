import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server implements Runnable {

	private String serverIP = "127.0.0.1";
	private int portNumber = 1337;
	private static Socket socket = null;
	private static ServerSocket serverSocket;
	private static boolean connectionOpen = true;

	public Server() {
		
		
	}
	
	public void run() {
		connectionOpen = true;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber, 50);
		} catch (BindException e0) {
			print("Port already in use.");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		print("Waiting for incoming connections.");
		try {
			serverSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (connectionOpen) {
			socket = null;
			try {
				socket = serverSocket.accept();
				
				//new Thread(new ClientHelper(socket)).start();
				
			} catch (SocketTimeoutException e3) {
				//print("Resetting Socket");
				if (connectionOpen == false) {
					GUI.btnStart.setEnabled(true);
					break;
				}
			} catch (IOException e2) {
				System.out.println(e2);
				print("Unable to open socket");
			} 
		}
		try {
			serverSocket.close();
			//socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void print(String input) {
		GUI.print(input);
	}
	
	public static void closeServer() {
		connectionOpen = false;
	}

}
