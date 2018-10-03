import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;


public class Client extends Thread {
	String serverIP;
	Socket s = null;
	boolean connectionValid = true;
	FileObject object;
	String sendDir;
	String recDir;
	static Queue<FileObject> fileQueue = new LinkedList<>();
	static Queue<FileObject> returnQueue = new LinkedList<>();
	ObjectOutputStream out;

	public Client(String ip, String send, String rec) {
		serverIP = ip;
		sendDir = send;
		recDir = rec;
		
	}

	public void run() {
		createConnection(); // Connect to server
		if (s != null) {
			monitorSending(); // Monitor folder for new files
			while (connectionValid) {
				if(fileQueue.size() > 0) {
					object = fileQueue.remove(); // Remove file from queue
					print(object.name);
					sendRequest(); // Send file
				}
				else {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
	}

	private void monitorSending() {
		new Thread(new FolderWatcher(sendDir, 0)).start();
	}
	
	
	private void createConnection() {
		try {
			s = new Socket(serverIP, 1337);
			print("Connected to " + serverIP);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectException e) {
			print("Connection refused by server");
			GUI.stopButton();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	private void sendRequest() {
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String reply;
			out.println("PermissionToSendObject");
			out.flush();
			boolean waiting = true;
			while (waiting) {
				// Wait for valid response
				while ((reply = bReader.readLine()) == null) {
				}
				
				if (reply.equals("PermissionGranted")) {
					print("Sending metadata");
					sendObject();
				} else

				if (reply.equals("SendFile")) {
					if (sendFile(object)) {
						waiting = false;
					}
				} else
					
				if (reply.equals("FileReady")) {
					if (sendFile(object)) {
						waiting = false;
					}
				} else	

				if (reply.equals("InvalidObject")) {
					print("Checksum mismatch. Sending object again");
					sendObject();
				}

			}
			

		} catch (SocketException e) {
			print("Server closed the connection");
			connectionValid = false;
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void sendObject() {
		try {	
			out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(object);
			out.flush();
			print("Client: " + object.name);
			print("Object sent!");
			
		} catch (SocketException e) {
			print("Socket Remotely Closed");
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean sendFile(FileObject file) {
		try {

			if (file.thisFile.exists() && !file.thisFile.isDirectory()) {
				Thread t = new FileTransfer(file, s, serverIP);
				t.start();
				t.join();
				return true;
			} else {
				print("File not found");
			}

		} catch (InterruptedException e) {
			print("Thread interrupted");
			e.printStackTrace();
		}
		return false;

	}
	
	private boolean receiveFile() {
		
		
		
		
		return false;
	}

	private void print(String input) {
		GUI.print(input);
	}
	
	public static boolean validateIP(String IP) {
		Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		return PATTERN.matcher(IP).matches();
	}

}
