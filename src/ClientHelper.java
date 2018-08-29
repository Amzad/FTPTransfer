import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class ClientHelper extends Thread {

	Socket socket;
	FileObject file;

	public ClientHelper(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		receiveObject();
		requestFile();
		receiveFile();
	}
	
	private void receiveObject() {
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

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			print("Connection abbruptedly closed");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			print("Invalid Class Received.");
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	}
	
	private void receiveFile() {	
		try {
			print("Receiving file");
			byte[] buffer = new byte[file.size];
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
	
	private void requestFile() {
		
		try {
			PrintWriter outputStreamWriter = new PrintWriter(socket.getOutputStream());
			outputStreamWriter.println("SendFile");
			outputStreamWriter.flush();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void print(String input) {
		GUI.print(input);
	}

}
