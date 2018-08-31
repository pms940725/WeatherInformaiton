package Response.perform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Operation_base.HttpGetRequest;
import Operation_base.ReadExcel;
import initProject.Parameter;
import rabbitMQ.SendToServer;

public class FrcstInfo {

	private static String resourceName = Parameter.Container_Name + ".SecndSrtpdFrcstInfoService2.ForecastSpaceData";
	private static String requestAPI = "ForecastSpaceData";
	private static String numOfRows = "10";
	private static String pageNo = "1";
	private static String address;
	private static String city1, city2, city3;
	private static String nxny[];
	private static String nx;
	private static String ny;
	private static String endPoint=null;
	private static String serviceKey=null;
	private static String base_date=null;
	private static String base_time=null;

	private static String pre_PTY = null;
	private static String pre_SKY = null;
	private static String pre_T3H = null;
	private static String cur_PTY = null;
	private static String cur_SKY = null;
	private static String cur_T3H = null;
	
	private static String targetURL;

	private static ArrayList<String> propertyArray = new ArrayList<>();

	
	
	
	@SuppressWarnings("unchecked")
	public static void notifyUpdatedSKY() {
		JSONObject jsonObject= new JSONObject();
		String fcstValue = null;
		String category = null;
		try {
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return ;
			}
			String response = HttpGetRequest.sendGet(targetURL);
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(response);
			//
			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
			JSONObject itemsObject = (JSONObject) bodyObject.get("items");
			JSONArray itemArray = (JSONArray) itemsObject.get("item");
			
			for(int i=0; i<itemArray.size(); i++) {
				JSONObject itemObject = (JSONObject) itemArray.get(i);
		
				category =itemObject.get("category").toString();
				
				if(category.equals("SKY")) {
					fcstValue =itemObject.get("fcstValue").toString();
					cur_SKY =  changeValueType(category, fcstValue).toString();
					
					break;
				}
			}
			
			if(!cur_SKY.equals(pre_SKY)) {
				JSONObject notifyObject = new JSONObject();
				notifyObject.put("message", "notify");
				notifyObject.put("messageType", "updated");
				notifyObject.put("containerName", Parameter.Container_Name);
				notifyObject.put("resource", resourceName);
				notifyObject.put("description", "status updated");
				JSONArray requiredArray = new JSONArray();
				requiredArray.add(category);
				notifyObject.put("required", requiredArray);
				JSONObject propertyObject = new JSONObject();
				propertyObject.put(category, cur_SKY);
				notifyObject.put("properties", propertyObject);
			
				SendToServer.send("notify.updated", notifyObject.toString());
			}	else {
				System.out.println(category + " : 이전 상태와 동일");
			}
			pre_SKY = cur_SKY;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void notifyUpdatedT3H() {
		JSONObject jsonObject= new JSONObject();
		String fcstValue = null;
		String category = null;
		try {
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return ;
			}
			String response = HttpGetRequest.sendGet(targetURL);
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(response);
			//
			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
			JSONObject itemsObject = (JSONObject) bodyObject.get("items");
			JSONArray itemArray = (JSONArray) itemsObject.get("item");
			
			for(int i=0; i<itemArray.size(); i++) {
				JSONObject itemObject = (JSONObject) itemArray.get(i);
		
				category =itemObject.get("category").toString();
				
				if(category.equals("T3H")) {
					fcstValue =itemObject.get("fcstValue").toString();
					cur_T3H = fcstValue;
					break;
				}
			}
			
			if(!cur_T3H.equals(pre_T3H)) {
				JSONObject notifyObject = new JSONObject();
				notifyObject.put("message", "notify");
				notifyObject.put("messageType", "updated");
				notifyObject.put("containerName", Parameter.Container_Name);
				notifyObject.put("resource", resourceName);
				notifyObject.put("description", "status updated");
				JSONArray requiredArray = new JSONArray();
				requiredArray.add(category);
				notifyObject.put("required", requiredArray);
				JSONObject propertyObject = new JSONObject();
				propertyObject.put(category, Integer.parseInt(fcstValue));
				notifyObject.put("properties", propertyObject);
			
				SendToServer.send("notify.updated", notifyObject.toString());
			}	else {
				System.out.println(category + " : 이전 상태와 동일");
			}
			pre_T3H = cur_T3H;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void notifyUpdatedPTY() {
		JSONObject jsonObject= new JSONObject();
		String fcstValue = null;
		String category = null;
		try {
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return ;
			}
			String response = HttpGetRequest.sendGet(targetURL);
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(response);
			//
			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
			JSONObject itemsObject = (JSONObject) bodyObject.get("items");
			JSONArray itemArray = (JSONArray) itemsObject.get("item");
			
			for(int i=0; i<itemArray.size(); i++) {
				JSONObject itemObject = (JSONObject) itemArray.get(i);
		
				category =itemObject.get("category").toString();
				
				if(category.equals("PTY")) {
					fcstValue =itemObject.get("fcstValue").toString();
					cur_PTY = changeValueType(category, fcstValue).toString();
					break;
				}
			}
			
			if(!cur_PTY.equals(pre_PTY)) {
				JSONObject notifyObject = new JSONObject();
				notifyObject.put("message", "notify");
				notifyObject.put("messageType", "updated");
				notifyObject.put("containerName", Parameter.Container_Name);
				notifyObject.put("resource", resourceName);
				notifyObject.put("description", "status updated");
				JSONArray requiredArray = new JSONArray();
				requiredArray.add(category);
				notifyObject.put("required", requiredArray);
				JSONObject propertyObject = new JSONObject();
				propertyObject.put(category, cur_PTY);
				notifyObject.put("properties", propertyObject);
			
				SendToServer.send("notify.updated", notifyObject.toString());
			}	else {
				System.out.println(category + " : 이전 상태와 동일");
			}
			pre_PTY = cur_PTY;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static Object changeValueType(String key, String value) {
		
		
		Object tempDefType =null;
		String tempDef = null;
		
	
		
		for(int i=0; i<Parameter.definitionArrayList.size(); i++) {
			if(Parameter.definitionArrayList.get(i).equals(key)) {
				tempDef = Parameter.definitionArrayList.get(i);
				tempDefType = Parameter.definitinoTypeArrayList.get(i);
				break;
			}
		}
		
	
		
		if(tempDefType instanceof String) {
			String checkString = tempDefType.toString();
			if(checkString.equals("integer")) {
				return Integer.parseInt(value);
			}
		}
		if(tempDefType instanceof JSONArray) {
			int tempValue = Integer.parseInt(value);
			if(tempDef.equals("SKY")) {
				tempValue -= 1;
			}
			
			JSONArray checkArray = (JSONArray) tempDefType;
			String returnValue = checkArray.get(tempValue).toString();
			return returnValue;
		}
		
		return null;
	}
	
	
	//요청 결과를 반환
	@SuppressWarnings("unchecked")
	public static JSONObject decidegetResponse(JSONObject jsonObject, ArrayList<String> requestPropArrayList) {
		
		JSONObject res_itemObject = new JSONObject();
		res_itemObject.put("resource", resourceName);
		JSONObject res_propertiesObject = new JSONObject();
		JSONArray res_requiredArray = new JSONArray();
		
		JSONObject responseObject = (JSONObject) jsonObject.get("response");
		if(responseObject == null) {
			return null;
		}
		JSONObject bodyObject = (JSONObject) responseObject.get("body");
		if(bodyObject == null) {
			return null;
		}
		JSONObject itemsObject = (JSONObject) bodyObject.get("items");
		if(itemsObject == null) {
			return null;
		}
		JSONArray itemArray = (JSONArray) itemsObject.get("item");
		if(itemArray == null) {		
			return null;
		}
		
		for(int i=0; i<itemArray.size(); i++) {
			JSONObject itemObject = (JSONObject) itemArray.get(i);
	
			String category =itemObject.get("category").toString();
			if(!requestPropArrayList.contains(category))
				continue;
			String fcstValue =itemObject.get("fcstValue").toString();
			//System.out.println(category + " : " + fcstValue);
			//System.out.println("---------------------------------------------");
			Object property = changeValueType(category, fcstValue);
			
			//property 정보를 추가
			res_propertiesObject.put(category, property);
			//System.out.println(res_statusArray);
		}
		
		for(String reqProperty : requestPropArrayList) {
			res_requiredArray.add(reqProperty);
		}
		
		res_itemObject.put("resource", resourceName);
		res_itemObject.put("properties", res_propertiesObject);
		res_itemObject.put("required", res_requiredArray);
		
		
		return res_itemObject;
	}
	//Error Response 하는 코드
	@SuppressWarnings("unchecked")
	public static void responseError() {
			JSONObject responseObject = new JSONObject();
			responseObject.put("message", "get");
			responseObject.put("messageType", "status");
			responseObject.put("direction", "response");
			responseObject.put("responseCode", 404);
			responseObject.put("containerName", Parameter.Container_Name);
			responseObject.put("resource", resourceName);
			
			String response = responseObject.toString();
			SendToServer.send("get.status", response);
	}
	//요청한 속성 정보와 사용할 수 있는 속성 정보 비교
public static boolean comparePropertyArray(ArrayList<String> requestPropArray) {
		
		if (requestPropArray.isEmpty()) {
					return false;
		}
		//System.out.println("Clear");
		return true;
	}
	//요청한 속성 정보를 반환
	public static ArrayList<String> getRequestPropertyArray(JSONObject jsonObject){
		ArrayList<String> requestPropertyArray = new ArrayList<>();
		JSONArray propertiesArray = null;

		propertiesArray = (JSONArray) jsonObject.get("properties");
		if (propertiesArray == null) {
			requestPropertyArray.addAll(propertyArray);
		} else {

			for (int i = 0; i < propertiesArray.size(); i++) {
				if(propertyArray.contains(propertiesArray.get(i).toString())) {
					requestPropertyArray.add(propertiesArray.get(i).toString());
				}
				
			}
		} 
		return requestPropertyArray;
	}
	//사용할 수 있는 속성 정보를 지닌 array를 반환
	public static ArrayList<String> getPropertyArray(){
		for (int i = 0; i < Parameter.resourceArrayList.size(); i++) {
			String resource = Parameter.resourceArrayList.get(i);
			if (resourceName.equals(resource)) {
				propertyArray = Parameter.propertyArrayList.get(i);
				break;
			}
		}
		return propertyArray;
	}
	//요청 정보에 대한 key값을 반환
	public static JSONObject getJSONObject() {
		JSONObject JSONObject=null;
		
		try {
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return null;
			}
			String response = HttpGetRequest.sendGet(targetURL);
			JSONParser jsonParser = new JSONParser();
			JSONObject = (JSONObject) jsonParser.parse(response);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Parameter.PrintMode) {
		System.out.println(JSONObject);
		}
		return JSONObject;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_address() {
		// parsing address
		address = ((JSONObject) Parameter.configurationObject.get(Parameter.parse_location))
				.get(Parameter.parse_address).toString();
		
		StringTokenizer st = new StringTokenizer(address);
		ArrayList<String> cityArray = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			cityArray.add(st.nextToken());
		}

		if (cityArray.size() > 3) {
			city1 = cityArray.get(0);
			city2 = cityArray.get(1) + " " + cityArray.get(2);
			city3 = cityArray.get(3);
		} else if(cityArray.size() == 3){
			city1 = cityArray.get(0);
			city2 = cityArray.get(1);
			city3 = cityArray.get(2);
		}
		else if(cityArray.size() == 2){
			city1 = cityArray.get(0);
			city2 = cityArray.get(1);
		}
		else if(cityArray.size() == 1) {
			city1 = cityArray.get(0);
		}
		else {
			return 0;
		}
		return 1;
	}	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_location() {
		// System.out.println("위치 정보를 가져옵니다.");

		
		
		nxny = ReadExcel.findnxny(city1, city2, city3, Parameter.filePath_GetForecastSpaceData);
		nx = nxny[0];
		ny = nxny[1];

		if (nx.equals("-1")) {
			System.out.println("존재하지 않는 위치 좌표입니다.");
			return 0;
		}
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_date() {
		// 현재 시각 구하기
		Date today = new Date();
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(today);
		// SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat time = new SimpleDateFormat("HHmm");

		// 시간에 따른 발표 날짜 결정
		int base_time_int = Integer.parseInt(time.format(today));
		
		if (300 <= base_time_int && base_time_int < 600) {
			base_time = "0200";
		} else if (600 <= base_time_int && base_time_int < 900) {
			base_time = "0500";
		} else if (900 <= base_time_int && base_time_int < 1200) {
			base_time = "0800";
		} else if (1200 <= base_time_int && base_time_int < 1500) {
			base_time = "1100";
		} else if (1500 <= base_time_int && base_time_int < 1800) {
			base_time = "1400";
		} else if (1800 <= base_time_int && base_time_int < 2100) {
			base_time = "1700";
		} else if (2100 <= base_time_int && base_time_int < 2400) {
			base_time = "2000";
		} else if (0 <= base_time_int && base_time_int < 300) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			base_time = "2300";
		}

		// 월, 일 날짜 변경
		int nYear = cal.get(Calendar.YEAR);
		int nMonth = cal.get(Calendar.MONTH) + 1;
		int nDate = cal.get(Calendar.DATE);

		String Year = String.format("%04d", nYear);
		String Month = String.format("%02d", nMonth);
		String Date = String.format("%02d", nDate);

		base_time_int = 800;
		base_date = Year + Month + Date;
		
		if(base_date == null || base_time == null)
			return 0;
		
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_Key_endPoint() {
		for (int i = 0; i < Parameter.resourceArrayList.size(); i++) {
			String resource = Parameter.resourceArrayList.get(i);
			if (resourceName.equals(resource)) {
				serviceKey = Parameter.keyArrayList.get(i);
				endPoint = Parameter.endPointArrayList.get(i);
				break;
			}
		}
		
		if(serviceKey == null || endPoint == null)
			return 0;
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static boolean setURL() {
		
		// 01. configuration.json에서 필요한 정보들을 가져와 저장
		if (parsing_Key_endPoint() != 1) {
			System.out.println("Error in parsing_Key_endPoint");
			return false;
		}
		if(parsing_address() != 1) {
			System.out.println("Error in parsing_address");
			return false;
		}
		if (parsing_location() != 1) {
			System.out.println("Error in parsing_location");
			return false;
		}
		if (parsing_date() != 1) {
			System.out.println("Error in parsing_date");
			return false;
		}
	
		targetURL = endPoint + "/" + requestAPI + "?" + "ServiceKey=" + serviceKey + "&base_date=" + base_date
				+ "&base_time=" + base_time + "&nx=" + nx + "&ny=" + ny + "&numOfRows=" + numOfRows + "&pageNo="
				+ pageNo + "&_type=json";
if(Parameter.PrintMode) {
		System.out.println("requestAPI : " + requestAPI);
		System.out.println("nx : " + nx);
		System.out.println("ny : " + ny);
		System.out.println("base_date = " + base_date);
		System.out.println("base_time= " + base_time);
		System.out.println(targetURL);
}
		return true;
	}

}
