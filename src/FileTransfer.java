import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class FileTransfer extends Thread {

	FileObject file;
	Socket socket;
	String serverIP;
	String recDir;
	int mode;

	public FileTransfer(FileObject file, Socket socket, String serverIP) {
		this.file = file;
		this.socket = socket;
		this.serverIP = serverIP;
		mode = 0;

	}

	public FileTransfer(String recDir, Socket socket, String serverIP) {
		this.recDir = recDir;
		this.socket = socket;
		this.serverIP = serverIP;
		mode = 1;
	}

	public void run() {
		if (mode == 0) {
			sendFile();
		} else {
			receiveFile();
		}
	}

	public void sendFile() {
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

	public void receiveFile() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println("SendFile" + file.checkSum);
			out.flush();
			InputStream inputStream = socket.getInputStream();
			File temp = new File(recDir + "\\" + file.name);
			GUI.print(temp.getAbsolutePath());
			OutputStream outputStream = new FileOutputStream(temp);
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			print(temp.getName() + " saved to " + temp.getAbsolutePath());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void print(String input) {
		GUI.print(input);
	}
}
