import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class FileTransfer extends Thread{
	
	FileObject file;
	Socket socket;
	String serverIP;

	public FileTransfer(FileObject file, Socket socket, String serverIP) {
		this.file = file;
		this.socket = socket;
		this.serverIP = serverIP;

	}
	
	public void run() {
		try {
			Socket socket = new Socket(serverIP, 1337);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String reply;

			out.println("NewStream" + file.checkSum);
			out.flush();

			while ((reply = bReader.readLine()) == null) {
			}

			if (reply.equals("SendFile" + file.checkSum)) {
				print("Sending file " + file.name);
				OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = new FileInputStream(file.thisFile);
				IOUtils.copy(inputStream, outputStream);
				inputStream.close();
				print("File: " + file.name + " sent.");
				outputStream.flush();
				outputStream.close();
				socket.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void print(String input) {
		GUI.print(input);
	}
}
