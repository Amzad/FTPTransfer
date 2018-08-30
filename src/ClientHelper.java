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

import org.apache.commons.io.IOUtils;

public class ClientHelper extends Thread {

	Socket socket;
	FileObject file;
	boolean isAlive = true;

	public ClientHelper(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		while(isAlive) {
			parseReply();
			//receiveObject();
		}
		//requestFile();
		//receiveFile();
	}
	
	private void parseReply() {
		try {
			print("waiting for input");
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
					outputStreamWriter.println("SendFile");
					outputStreamWriter.flush();
					receiveFile(); // Receive the file

				} else {
					outputStreamWriter.println("InvalidObject");
					outputStreamWriter.flush();
				}

			} else if (input.equals("RequestingLeave")) {
				outputStreamWriter.println("LeaveGranted");
				outputStreamWriter.flush();
				socket.close();
				isAlive = false;

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
	
	private void receiveFile() {	
		try {
			print("Receiving file");
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = new FileOutputStream(new File(file.name));
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
			print("Done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void print(String input) {
		GUI.print(input);
	}

}
