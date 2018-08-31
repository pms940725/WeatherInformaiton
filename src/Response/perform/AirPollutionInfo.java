package Response.perform;


import java.util.ArrayList;


import java.util.Iterator;

import java.util.StringTokenizer;

import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Operation_base.HttpGetRequest;
import initProject.Parameter;
import rabbitMQ.SendToServer;

public class AirPollutionInfo {
	private static String resourceName = Parameter.Container_Name + ".ArpltnInforInqireSvc.CtprvnMesureLIst";
	private static String requestAPI = "getCtprvnMesureLIst";
	private static String numOfRows = "1";
	private static String pageNo = "1";
	private static String address;
	private static String city1;
	private static String itemCode = null;
	private static String dataGubun = "HOUR";
	private static String searchCondition = "MONTH";
	private static String endPoint = null;
	private static String serviceKey = null;
	
	private static String pre_PM10=null;
	private static String pre_PM25=null;
	private static String cur_PM10=null;
	private static String cur_PM25=null;

	private static String targetURL;

	private static ArrayList<String> propertyArray = new ArrayList<>();
	
	
	@SuppressWarnings("unchecked")
	public static void notifyUpdatedPM10() {
		try {
			itemCode = "PM10";
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return;
			}
			String XML_response = HttpGetRequest.sendGet(targetURL);
			String response = XML.toJSONObject(XML_response).toString();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(response);


			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");

			JSONObject itemsObject = (JSONObject) bodyObject.get("items");

			String numOfRows_str = bodyObject.get("numOfRows").toString();

			JSONObject itemObject;
			if (numOfRows_str.equals("1")) {
				itemObject = (JSONObject) itemsObject.get("item");
			} else {
				// json이 배열일 경우 해당 배열의 가장 첫번째 값을 가져온다.
				JSONArray itemArray = (JSONArray) itemsObject.get("items");
				itemObject = (JSONObject) itemArray.get(0);
			}

			Iterator<String> iter = itemObject.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();

				// city 정보를 비교
				if (key.equals(city1)) {
					cur_PM10 = itemObject.get(key).toString();
					break;
				}
			}

			if (!cur_PM10.equals(pre_PM10)) {
				JSONObject notifyObject = new JSONObject();
				notifyObject.put("message", "notify");
				notifyObject.put("messageType", "updated");
				notifyObject.put("containerName", Parameter.Container_Name);
				notifyObject.put("resource", resourceName);
				notifyObject.put("description", "status updated");
				JSONArray requiredArray = new JSONArray();
				requiredArray.add("PM10");
				notifyObject.put("required", requiredArray);
				JSONObject propertyObject = new JSONObject();
				propertyObject.put("PM10", Integer.parseInt(cur_PM10));
				notifyObject.put("properties", propertyObject);

				SendToServer.send("notify.updated", notifyObject.toString());
			}
			else {
				System.out.println("PM10 : 이전 상태와 동일");
			}
			pre_PM10 = cur_PM10;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	public static void notifyUpdatedPM25() {
		try {
			itemCode = "PM25";
			if (setURL() == false) {
				System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
				return;
			}
			String XML_response = HttpGetRequest.sendGet(targetURL);
			String response = XML.toJSONObject(XML_response).toString();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(response);

			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
	
			JSONObject itemsObject = (JSONObject) bodyObject.get("items");
			
			String numOfRows_str = bodyObject.get("numOfRows").toString();
			
			JSONObject itemObject;
			if(numOfRows_str.equals("1")) {
				itemObject = (JSONObject) itemsObject.get("item");
			}
			else {
				//json이 배열일 경우 해당 배열의 가장 첫번째 값을 가져온다.
				JSONArray itemArray = (JSONArray) itemsObject.get("items");
				itemObject = (JSONObject) itemArray.get(0);
			}

			Iterator<String> iter = itemObject.keySet().iterator();
			while (iter.hasNext()) {
		        String key = iter.next();
		     
		        //city 정보를 비교
		        if(key.equals(city1)) {
		        	cur_PM25 = itemObject.get(key).toString();
		        	break;
		        }
		    }

			if(!cur_PM25.equals(pre_PM25)) {
				JSONObject notifyObject = new JSONObject();
				notifyObject.put("message", "notify");
				notifyObject.put("messageType", "updated");
				notifyObject.put("containerName", Parameter.Container_Name);
				notifyObject.put("resource", resourceName);
				notifyObject.put("description", "status updated");
				JSONArray requiredArray = new JSONArray();
				requiredArray.add("PM2.5");
				notifyObject.put("required", requiredArray);
				JSONObject propertyObject = new JSONObject();
				propertyObject.put("PM2.5", Integer.parseInt(cur_PM25));
				notifyObject.put("properties", propertyObject);
				
				SendToServer.send("notify.updated", notifyObject.toString());
			}
			else {
				System.out.println("PM10 : 이전 상태와 동일");
			}
			pre_PM25 = cur_PM25;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	//요청 결과를 반환
		@SuppressWarnings("unchecked")
		public static JSONObject decidegetResponse(ArrayList<JSONObject> jsonArray, ArrayList<String> requestPropArrayList) {
		JSONObject res_itemObject = new JSONObject();
		JSONObject res_propertiesObject = new JSONObject();
		JSONArray res_requiredArray = new JSONArray();

		//properties 추가
		for (JSONObject jsonObject : jsonArray) {
			//System.out.println(jsonObject);
			JSONObject responseObject = (JSONObject) jsonObject.get("response");
			JSONObject bodyObject = (JSONObject) responseObject.get("body");
	
			JSONObject itemsObject = (JSONObject) bodyObject.get("items");
			
			String numOfRows_str = bodyObject.get("numOfRows").toString();
			
			JSONObject itemObject;
			if(numOfRows_str.equals("1")) {
				itemObject = (JSONObject) itemsObject.get("item");
			}
			else {
				//json이 배열일 경우 해당 배열의 가장 첫번째 값을 가져온다.
				JSONArray itemArray = (JSONArray) itemsObject.get("items");
				itemObject = (JSONObject) itemArray.get(0);
			}
			
			Iterator<String> iter = itemObject.keySet().iterator();
			while (iter.hasNext()) {
		        String key = iter.next();
		        //요청한 정보만 json으로 추가
		        String itemCode = itemObject.get("itemCode").toString();
		        if(!requestPropArrayList.contains(itemCode))
					continue;
		        //city 정보를 비교
		        if(key.equals(city1)) {
		        	res_propertiesObject.put(itemCode, Integer.parseInt(itemObject.get(key).toString()));
		        	break;
		        }
		    }
		}
		//resource 추가
		res_itemObject.put("resource", resourceName);
		
		//required 추가
		for(String reqProperty : requestPropArrayList) {
			res_requiredArray.add(reqProperty);
		}
		
		res_itemObject.put("resource", resourceName);
		res_itemObject.put("properties", res_propertiesObject);
		res_itemObject.put("required", res_requiredArray);

		//System.out.println(res_itemObject);
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
	
	
	
public static boolean comparePropertyArray(ArrayList<String> requestPropArray) {
		
		if (requestPropArray.isEmpty()) {
					return false;
		}
		//System.out.println("Clear");
		return true;
	}
	
	
	
	
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
	
	public static ArrayList<JSONObject> getJSONArrayList() {
		ArrayList<JSONObject> JSONArrayList = new ArrayList<>();
		getPropertyArray();
		
		try {
			for (String property : propertyArray) {
				itemCode = property;
				//itemCode 예외 처리.... PM2.5 PM25 통일 좀 시켜놓지 API 개떡같이 만들어놨어...ㄷㄷ
				if(itemCode.equals("PM2.5")) {
					itemCode = "PM25";
				}
				if (setURL() == false) {
					System.out.println("API 요청 과정에서 Error가 발생하였습니다.");
					return null;
				}
				String XML_response = HttpGetRequest.sendGet(targetURL);
				String response = XML.toJSONObject(XML_response).toString();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(response);
				JSONArrayList.add(jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//System.out.println(JSONArrayList);
		return JSONArrayList;
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
		if(cityArray.isEmpty()) {
			return 0;
		}
		
		city1 = cityArray.get(0);
		
	
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_location() {
		//(서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종)
		
		if(city1.equals("서울특별시")) {
			city1 = "seoul";
		}
		else if(city1.equals("부산광역시")) {
			city1 = "busan";
		}
		else if(city1.equals("대구광역시")) {
			city1 = "daegu";
		}
		else if(city1.equals("인천광역시")) {
			city1 = "incheon";
		}
		else if(city1.equals("광주광역시")) {
			city1 = "gwangju";
		}
		else if(city1.equals("대전광역시")) {
			city1 = "daejeon";
		}
		else if(city1.equals("울산광역시")) {
			city1 = "ulsan";
		}
		else if(city1.equals("경기도")) {
			city1 = "gyeonggi";
		}
		else if(city1.equals("강원도")) {
			city1 = "gangwon";
		}
		else if(city1.equals("충청북도")) {
			city1 = "chungbuk";
		}
		else if(city1.equals("충청남도")) {
			city1 = "chungnam";
		}
		else if(city1.equals("경상북도")) {
			city1 = "gyeongbuk";
		}
		else if(city1.equals("경상남도")) {
			city1 = "gyeongnam";
		}
		else if(city1.equals("전라북도")) {
			city1 = "jeonbuk";
		}
		else if(city1.equals("전라남도")) {
			city1 = "jeonnam";
		}
		else if(city1.equals("제주특별자치도")) {
			city1 = "jeju";
		}
		else if(city1.equals("세종특별자치시")) {
			city1 = "sejong";
		}
		else {
			return 0;
		}
		//sidoName = city1;
		
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int parsing_Key_endPoint() {
		for (int i = 0; i < Parameter.resourceArrayList.size(); i++) {
			String resource = Parameter.resourceArrayList.get(i);
			if (resourceName.equals(resource)) {
				
				serviceKey = Parameter.keyArrayList.get(i);
				endPoint = Parameter.endPointArrayList.get(i);
				propertyArray = Parameter.propertyArrayList.get(i);
				break;
			}
		}

		if (serviceKey == null || endPoint == null)
			return 0;
		return 1;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static boolean setURL() {

		// 01. configuration.json에서 필요한 정보들을 가져와 저장
		if (parsing_Key_endPoint() != 1) {
			return false;
		}
		if (parsing_address() != 1) {
			return false;
		}
		if (parsing_location() != 1) {
			return false;
		}

		targetURL = endPoint + "openapi/services/rest/ArpltnInforInqireSvc/" + requestAPI + "?" 
		+"itemCode=" + itemCode	
		+ "&dataGubun=" + dataGubun 
		+ "&searchCondition=" + searchCondition
		+ "&pageNo=" + pageNo 
		+ "&numOfRows=" + numOfRows 
		+ "&ServiceKey=" + serviceKey;
if(Parameter.PrintMode) {
		System.out.println("requestAPI : " + requestAPI);
		System.out.println("itemCode : " + itemCode);
		System.out.println(targetURL);
}
		return true;
	}

}
