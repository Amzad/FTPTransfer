import java.io.File;

public class AnimeFile {
	String rawDirectory;
	String rawName;
	String animePath;
	String animeTitle;
	String animeEpisode;
	String animeYear;
	String directory;
	String fangroup;
	String quality;
	String extension;
	File file;
	
	
	
	public AnimeFile(File file) {
		rawName = file.getName();
		rawDirectory = file.getPath();
		this.file = file;
		String space = " ";
		String name = "";
				
		/*
		 * [HorribleSubs] Shokugeki no Soma! - 01 [1080p].mkv 
		 * 0 - FanGroup 
		 * 1 -Name 
		 * 2 - Episode 
		 * 3 - Quality 
		 * 4 - Extension
		 */

		String inputFile = file.getName();
		String delims = "[ .]+";
		String[] tokens = inputFile.split(delims);

		extension = tokens[tokens.length - 1];
		quality = tokens[tokens.length - 2];
		animeEpisode = tokens[tokens.length - 3];
		fangroup = tokens[0];
		
		for (int i = 1; i < (tokens.length - 3); i++) {
			name = name + space + tokens[i];
		}
		
		animeTitle = name.substring(1, name.length()-1).trim();
		animePath = (animeTitle + space + "-" + space + animeEpisode + "." + extension).trim();
	
	}
}
