import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileOperations {

	File[] listFiles;
	static boolean active = false;

	String[] fangroupList = { "HorribleSubs", "DeadFish", "BakedFish" };
	String[] qualityList = { "480", "480p", "720", "720p", "1080", "1080p" };

	public String parseName(File input) {
		String name = "";
		String episode;
		String quality;
		String fangroup;
		String extension;
		String space = " ";

		/*
		 * [HorribleSubs] Shokugeki no Soma! - 01 [1080p].mkv 0 - FanGroup 1 -
		 * Name 2 - Episode 3 - Quality 4 - Extension
		 */

		String inputFile = input.getName();
		String delims = "[ .]+";
		String[] tokens = inputFile.split(delims);

		extension = tokens[tokens.length - 1];
		quality = tokens[tokens.length - 2];
		episode = tokens[tokens.length - 3];
		fangroup = tokens[0];
		for (int i = 1; i < (tokens.length - 3); i++) {
			name = name + space + tokens[i];
		}
		name = name.substring(1, name.length()-1);
		name = name.trim();

		String newname = name + space + "-" + space + episode + "." + extension;
		return newname;
	}

	public static boolean renameAndMove(AnimeFile input) {
//		if (input.animeTitle.length() < (input.file.getName().length() / 2)) {
//			GUI.print("Failed. New name may have issues: " + input.animeTitle);
//			return false;
//		}

		try {
			//File newFile = new File(input.directory, input.animeTitle);
			
			String currentLocation = input.rawDirectory;
			String newLocation = input.directory + "\\" + input.animePath;
			System.out.println(currentLocation);
			System.out.println(newLocation);
			Path temp = Files.move(Paths.get(currentLocation),
					Paths.get(newLocation));

			if (temp != null) {
				System.out.println("File renamed and moved successfully");
			} else {
				System.out.println("Failed to move the file");
			}

		} catch (IOException e) {
			System.out.println("IO Exception: Moving file");
		}

		return true;
	}
	
	public static boolean move(AnimeFile input) {
		try {
			
			String currentLocation = input.rawDirectory;
			String newLocation = input.directory + "\\" + input.animePath;
			System.out.println(currentLocation);
			System.out.println(newLocation);
			Path temp = Files.move(Paths.get(currentLocation),
					Paths.get(newLocation));

			if (temp != null) {
				System.out.println("File renamed and moved successfully");
				return true;
			} else {
				System.out.println("Failed to move the file");
				return false;
			}

		} catch (IOException e) {
			System.out.println("IO Exception: Moving file");
			return false;
		}

	}

}