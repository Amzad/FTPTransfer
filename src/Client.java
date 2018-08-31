import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
	ObjectOutputStream out;

	public Client(String ip, String send, String rec) {
		serverIP = ip;
		sendDir = send;
		recDir = rec;
		
	}

	public void run() {
		createConnection();
		if (s != null) {
			monitorSending();
			while (connectionValid) {
				if(fileQueue.size() > 0) {
					print("found file");
					object = fileQueue.remove();
					print(object.name);
					sendRequest();
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
		new Thread(new FolderWatcher(sendDir, recDir)).start();
	}
	
	
	private void createConnection() {
		print("Connecting to " + serverIP);
		try {
			s = new Socket(serverIP, 1337);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Server found");
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
				while ((reply = bReader.readLine()) == null) {
				}

				print("got reply");
				print(reply);

				if (reply.equals("PermissionGranted")) {
					print("Sending object");
					sendObject();
				} else

				if (reply.equals("SendFile")) {
					print("Sending file " + object.name);
					if (sendFile(object)) {
						waiting = false;
					}
				} else

				if (reply.equals("InvalidObject")) {
					print("Sending object again");
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
				Thread t = new FileTransfer(file, s, 2);
				t.start();
				print("New thread for file transfer.");
				t.join();
				print("Client done");
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

	private void print(String input) {
		GUI.print(input);
	}
	
	public static boolean validateIP(String IP) {
		Pattern PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		return PATTERN.matcher(IP).matches();
	}

}
