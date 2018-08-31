import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.IOException;
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
	static WatchService watchService;
	
	public FolderWatcher(String sending, String receiving) {
		watchDir = sending;
	}

	public void run() {

		try {
			print("Starting Watch Service");
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() throws IOException {
		//String watchDir = watchDir;
		//String moveDir = Main.prefs.get("moveDirectory", null);
		
		Path watchPath = Paths.get(watchDir);
		//Path movePath = Paths.get(moveDir);
		watchService = FileSystems.getDefault().newWatchService();
		WatchKey watchKey = watchPath.register(watchService, ENTRY_CREATE);
		//WatchKey moveKey = movePath.register(watchService, ENTRY_CREATE);

		while (!Thread.interrupted()) {
			try {
				watchKey = watchService.take();
				//moveKey = watchService.take();
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

						// new NameParser(watchDir + name);
						FileObject file = new FileObject(temp);
						Client.fileQueue.add(file);
						
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