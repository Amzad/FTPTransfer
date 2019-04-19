import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FolderWatcher implements Runnable {
	static boolean active = true;
	String watchDir;
	String processedDir;
	static WatchService watchService;
	int mode;
	
	public FolderWatcher(String sending, int mode) {
		watchDir = sending;
		this.mode = mode;
		
		// Mode 0 = Client
		// 1 = Local
		// 2 = Server
	}

	public void run() {

		try {
			print("Starting Watch Service");
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClosedWatchServiceException e) {
			// Remove Stop watch error message
		}

	}

	public void start() throws IOException {

		Path watchPath = Paths.get(watchDir);
		watchService = FileSystems.getDefault().newWatchService();
		WatchKey watchKey = watchPath.register(watchService, ENTRY_CREATE);

		while (!Thread.interrupted()) {
			try {
				watchKey = watchService.take();
			} catch (InterruptedException e) {
				stop();
				break;
			}

			List<WatchEvent<?>> keys = watchKey.pollEvents();
			for (WatchEvent<?> watchEvent : keys) {
				Kind<?> watchEventKind = watchEvent.kind();
				if (watchEventKind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}
				if (watchEventKind == StandardWatchEventKinds.ENTRY_CREATE) {
					String name = watchEvent.context().toString();
					print("New File: " + name);
					
					File temp = new File(watchDir + name);
					if (temp.exists() && !temp.isDirectory()) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						FileObject file = new FileObject(temp);
						if (mode == 0) {
							Client.fileQueue.add(file);
						} else if (mode == 1) {
							Client.fileQueue.add(file);
						}
						else {
							Server.fileQueue.add(file);
							Server.fileQueue.notifyAll();
						}
					}	
				}
				if (watchKey == null) {
				}
				watchKey.reset();

			}
			if (Thread.interrupted()) {
				break;
			}

		}

	}
	
	public static void stop() {
		active = false;
		try {
			watchService.close();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		print("Stopping Watch Service");
	}
	
	private static void print(String input) {
		GUI.print(input);
	}

}