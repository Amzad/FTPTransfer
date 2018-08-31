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

public class FileTransfer extends Thread{
	
	FileObject file;
	Socket socket;
	int mode; // 1 - Receive, 2 - Send

	public FileTransfer(FileObject file, Socket socket, int mode) {
		this.file = file;
		this.socket = socket;
		this.mode = mode;
		
	}
	
	public void run() {
		try {
			if (mode == 1) {
				print("Receiving file");
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = new FileOutputStream(new File(file.name));
				IOUtils.copy(inputStream, outputStream);
				outputStream.close();
				inputStream.reset();
				print("File received");
				
			} else if (mode == 2) {
				Socket socket = new Socket("127.0.0.1", 1337);
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
