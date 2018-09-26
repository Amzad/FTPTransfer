import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class ClientHelper extends Thread {

	Socket socket;
	String receivingDir;
	FileObject file;
	boolean isAlive = true;
	static Map<Integer,FileObject> dataQueue = new HashMap<Integer,FileObject>();
	
	public ClientHelper(Socket socket, String receivingDir) {
		this.socket = socket;
		this.receivingDir = receivingDir;
	}

	public void run() {
		while(isAlive) {
			parseReply();
		}
	}
	
	private void parseReply() {
		try {
			//print("waiting for input");
			String input;
			InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
			PrintWriter outputStreamWriter = new PrintWriter(socket.getOutputStream());
			BufferedReader inputStream = new BufferedReader(inputStreamReader);
			
			while ((input = inputStream.readLine()) == null) {
			}

			print(input);
			if (input.equals("PermissionToSendObject")) {
				outputStreamWriter.println("PermissionGranted");
				outputStreamWriter.flush();
				if (isValidObject()) { // If object is valid, request file.
					dataQueue.put(file.checkSum, file);	
					print(file.checkSum + " inserted into DB");
					outputStreamWriter.println("SendFile");
					outputStreamWriter.flush();
				} else {
					outputStreamWriter.println("InvalidObject");
					outputStreamWriter.flush();
				}

			} else if (input.equals("RequestingLeave")) {
				outputStreamWriter.println("LeaveGranted");
				outputStreamWriter.flush();
				socket.close();
				isAlive = false;

			} else if (input.substring(0,9).equals("NewStream")) {
				receiveFile(input);
			}
			
			
			
		} catch (SocketException e) {
			print("Connection reset");
			isAlive = false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private boolean isValidObject() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			FileObject object = (FileObject) in.readObject();
			print("Server: " + object.name);
			print("Server: " + Double.toString(object.size));
			print("Server: " + Integer.toString(object.checkSum));
			file = object;
			//in.close();
			String checksum = object.name + object.size;
			if (checksum.hashCode() == object.checkSum) {
				print("Valid object. Requesting file");
				return true;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			print("Connection abbruptedly closed");
			try {
				socket.close();
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			print("Invalid Class Received.");
			try {
				socket.close();
				return false;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
		return false;
	}
	
	private void receiveFile(String reply) {	
		try {
			int hash = Integer.parseInt(reply.substring(9, reply.length()));
			//print("Hash " + hash);
			//print("Hash2 " + dataQueue.get(hash).checkSum);
			if (hash == dataQueue.get(hash).checkSum) {
				//print("Valid hash");
				FileObject file = dataQueue.get(hash);
				//Thread t = (new FileTransfer(file, socket, 1));
				//t.start();
				//print("New thread for file transfer.");
				//t.join();
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println("SendFile" + file.checkSum);
				out.flush();
				InputStream inputStream = socket.getInputStream();
				File temp = new File(receivingDir + "\\" + file.name);
				GUI.print(temp.getAbsolutePath());
				OutputStream outputStream = new FileOutputStream(temp);
				IOUtils.copy(inputStream, outputStream);
				outputStream.close();
				dataQueue.remove(hash);
				print(temp.getName() + " saved to " + temp.getAbsolutePath());
			} else {
				print("Invalid checksum");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private void sendFile() {
		
		
	}
	
	private void waitForFile() {
		
		
	}

	private void print(String input) {
		GUI.print(input);
	}

}
