import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
	
	public Client() {
		
		
	}
	
	public void send() {
		
		FileObject object = new FileObject("Naruto Shippuden", 324.25);
		Socket s;
		try {
			s = new Socket("192.168.1.12", 1337);
			print("Connecting to " + s.getInetAddress());
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(object);
			out.flush();
			print("Client: " + object.name);
			print("Client: " + Double.toString(object.size));
			print("Client: " + Integer.toString(object.checkSum));
			print("File sent!");
			
			s.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void print(String input) {
		GUI.print(input);
	}
	
	public void run() {
		send();
	}

}
