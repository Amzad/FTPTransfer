import java.io.File;

public class FileObject {
	
	String name;
	double size;
	int checkSum;

	public FileObject(String name, double size) {
		this.name = name;
		this.size = size;
		createHash();
	}
	
	public FileObject(File file) {
		
	}
	
	private void createHash() {
		String pre = name + size;
		checkSum = pre.hashCode(); 
	}

}
