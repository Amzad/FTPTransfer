import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server implements Runnable {

	private int portNumber = 1337;
	private static Socket socket = null;
	private static ServerSocket serverSocket;
	private static boolean connectionOpen = true;
	private static String receivingDir; 
	 
	public Server(String string) {
		receivingDir = string;
		
	}
	
	public void run() {
		connectionOpen = true;
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber, 50);
		} catch (BindException e0) {
			print("Port already in use.");
			GUI.btnStart.setEnabled(true);
			GUI.enableComponentsServer();
			closeServer();
			return;
		} catch (IOException e1) {
			print("IO Exception");
			e1.printStackTrace();
		}
		try {
			serverSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			print("Socket Exception");
			e.printStackTrace();
		}
		print("Waiting for incoming connections.");
		while (connectionOpen) {
			socket = null;
			try {
				socket = serverSocket.accept();			
				print("creating new helper");
				new Thread(new ClientHelper(socket, receivingDir)).start();
			} catch (SocketTimeoutException e3) {
				//print("Resetting Socket");
				if (connectionOpen == false) {
					GUI.btnStart.setEnabled(true);
					break;
				}
			} catch (IOException e2) {
				System.out.println(e2);
				print("Unable to open socket/IO Exception");
			} 
		}
		try {
			serverSocket.close();
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
