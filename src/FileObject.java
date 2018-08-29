import java.io.File;
import java.io.Serializable;

public class FileObject implements Serializable {
	
	String name;
	int size;
	int checkSum;
	File thisFile;

	public FileObject(String name, int size) {
		this.name = name;
		this.size = size;
		createHash();
	}
	
	public FileObject(File file) {
		name = file.getName();
		size = (int) file.length();
		thisFile = file;
	}
	
	private void createHash() {
		String pre = name + size;
		checkSum = pre.hashCode(); 
	}

}
