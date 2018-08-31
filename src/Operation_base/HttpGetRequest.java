package Operation_base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpGetRequest {
	
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	// HTTP GET request
		public static String sendGet(String targetUrl) throws Exception {
			//targetUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getTMStdrCrdnt?umdName=����Ư���� ���α� ��ȭ��&pageNo=1&numOfRows=10&ServiceKey=x6BVj%2BCjwdJZbUjUim9ZZI2vEQFkO2dwOqmPo%2BxxqtcMqEh6az5Dew2mN009kwtvAZw0fCI2sp%2Bbh05UL6SYYg%3D%3D";
			//targetUrl=URLEncoder.encode(targetUrl, "UTF_8");
			URL url = new URL(targetUrl);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET"); // optional default is GET
			con.setRequestProperty("User-Agent", USER_AGENT); // add request header
			
			//int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));	//UTF_8 Ÿ������ Get ��û�� �ؾ� �ȱ���
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			//System.out.println("HTTP ���� �ڵ� : " + responseCode);
			//System.out.println("HTTP body : " + response.toString());
			return response.toString();
		}

}
