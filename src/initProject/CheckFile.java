package initProject;

import java.io.File;

public class CheckFile {

	public static boolean haveFile(String filePath) {
		File f = new File(filePath);
		if (f.isFile()) {
			//System.out.println("hava " + filePath);
			return true;
		}
		
		System.out.println("don't have " + filePath);
		return false;
	}
}
