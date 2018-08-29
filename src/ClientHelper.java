import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHelper extends Thread {

	Socket socket;

	public ClientHelper(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			FileObject object = (FileObject) in.readObject();
			print("Server: " + object.name);
			print("Server: " + Double.toString(object.size));
			print("Server: " + Integer.toString(object.checkSum));
			in.close();
			socket.close();
			print("Socket Closed");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			print("Connection abbruptedly closed");
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			print("Invalid Class Received.");
		}
	}

	private void print(String input) {
		GUI.print(input);
	}

}
