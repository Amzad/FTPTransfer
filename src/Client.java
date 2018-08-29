import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.*;
import org.apache.commons.io.IOUtils;


public class Client extends Thread {
	String serverIP;
	Socket s = null;
	boolean connectionValid = true;
	FileObject object;

	public Client() {

	}

	public void run() {
		createConnection();
		if (s != null) {
			sendObject();
			getResponse();

		}
	}

	private void createConnection() {
		print("Connecting to " + serverIP);
		try {
			s = new Socket("127.0.0.1", 1337);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print("Server found");
	}
	
	private void parseResponse(String reply) {
		
	}

	private void getResponse() {
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			BufferedReader bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String reply;

			try {
				print("Waiting for reply");
				while ((reply = bReader.readLine()) == null) {
				}
				print(reply);
				if (reply.equals("SendFile")) {
					print("Sending file");

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				connectionValid = false;

			}

			sendFile(object);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void sendObject() {
		//FileObject object = new FileObject("Naruto Shippuden", 324.25);
		File myFile = new File("s.pdf");
		object = new FileObject(myFile);
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(object);
			out.flush();
			print("Client: " + object.name);
			print("Client: " + Double.toString(object.size));
			print("Client: " + Integer.toString(object.checkSum));
			print("Object sent!");
			//out.reset();
			//out.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void sendFile(FileObject file) {
		try {
			print("Sending file method");
			//File myFile = new File("s.pdf");
			if (file.thisFile.exists() && !file.thisFile.isDirectory()) {
				OutputStream outputStream = s.getOutputStream();
				InputStream inputStream = new FileInputStream(file.thisFile);
				IOUtils.copy(inputStream, outputStream);
				inputStream.close();
				outputStream.close();
				print("file sent");
			} else {
				print("File not found");
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
