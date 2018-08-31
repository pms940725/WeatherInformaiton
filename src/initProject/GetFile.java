package initProject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetFile {

	public static String getString(String filePath) {
		String message = new String();
		try {
			// 파일의 존재 여부를 확인

			if (!CheckFile.haveFile(filePath)) {
				return message;
			}

			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));
			String line = new String();
			while ((line = reader.readLine()) != null) {
				message += line;
			}

		} catch (Exception e) {
			System.out.println("Error in GetStringFile");
			e.printStackTrace();
		}
		
		return message;
	}

	public static JSONObject getJSON(String filePath) {
		JSONObject messageObject = null;
		try {
			if (!CheckFile.haveFile(filePath)) {
				return messageObject;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF8"));

			JSONParser parser = new JSONParser();
			Object obj = parser.parse(reader);

			messageObject = (JSONObject) obj;

		} catch (Exception e) {
			System.out.println("Error in GetJsonFile");
			e.printStackTrace();
		}

		return messageObject;
	}
}
