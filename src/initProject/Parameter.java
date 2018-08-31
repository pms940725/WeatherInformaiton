package initProject;

import Tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@SuppressWarnings("unchecked")
public class Parameter {

//수정해야 할 부분들 : Parameter.java에서 final로 선언된 것들 
//함수는 기능별로 Response.perform 패키지에 Java 파일 만들어서 사용하기
//명령어 divide는 Analyze Request에서 나누기
	public final static String Container_Name = "WeatherInformation";
	public final static String PreprocessingMessage_unfinished = PreprocessingMessage_unfinished();
	public final static String PreprocessingMessage_finished = PreprocessingMessage_finished();
	
	public static ArrayList<Thread> threadArray= new ArrayList<Thread>();
	public static boolean operationRunning = false;
	public final static boolean DebugMode = false;
	public final static boolean PrintMode = false;

//FilePath 설정
	public final static String filePath_Property = "/WeatherInformation/Weather Information property.json";
	public final static String filePath_Configuration = "/WeatherInformation/Weather Information Configuration.json";
	public final static String filePath_GetForecastSpaceData = "/WeatherInformation/GetForecastSpaceData.xlsx";
//FilePath를 통해 읽은 JSON값을 저장
	public final static JSONObject propertyObject = GetFile.getJSON(filePath_Property);
	public final static String propertyString = GetFile.getString(filePath_Property);
	private final static String parse_definitions = "definitions";
	private final static String parse_items = "items";
	public final static JSONObject configurationObject = GetFile.getJSON(filePath_Configuration);
	private final static String parse_configuration = "configuration";
	public final static String parse_location = "location";
	public final static String parse_address = "address";

//Parsing한 Data들을 저장 - Container 종류에 따라 다름
	public final static ArrayList<String> resourceArrayList = new ArrayList<>();
	private final static String parse_resource = "resource";
	
	public final static ArrayList<ArrayList<String>> propertyArrayList = new ArrayList<>();
	private final static String parse_required = "required";
	
	public final static ArrayList<String> definitionArrayList = new ArrayList<>();
	public final static ArrayList<Object> definitinoTypeArrayList = new ArrayList<>();
	
	public final static ArrayList<String> endPointArrayList = new ArrayList<>();
	private final static String parse_endPoint = "End Point";
	
	public final static ArrayList<String> keyArrayList = new ArrayList<>();
	private final static String parse_key = "Key";


//Node 관리
	public final static Node root_Node = new Node("ROOT");
	public static ArrayList<Node> nodeArrayList = new ArrayList<Node>();
	public static int MaxDepth=0;

	// constructor
	public Parameter() {
		getItem();
		setNode();
	}
	
	public static String getContainerName() {
		String containerName = propertyObject.get("containerName").toString();
		
		return containerName;
		
		
	}
	
	public static boolean Check_Overlapping(String jsonObject_str) {

		JSONObject jsonObject = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonObject_str);
			jsonObject = (JSONObject) obj;

			if (!jsonObject.containsValue((long) 200)) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public static boolean Check_clearConfiguration(String jsonObject_str) {

		JSONObject jsonObject = new JSONObject();
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonObject_str);
			jsonObject = (JSONObject) obj;

			if (!jsonObject.containsValue((long)200)) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	

	private static String PreprocessingMessage_unfinished() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("message", "set");
		jsonObject.put("messageType", "status");
		jsonObject.put("direction", "request");
		jsonObject.put("containerName", Container_Name);
		jsonObject.put("resource", Container_Name);
		JSONArray requiredArray = new JSONArray();
		requiredArray.add("containerName");
		jsonObject.put("required", requiredArray);
		JSONObject propertiesObject = new JSONObject();
		propertiesObject.put("containerName", Container_Name);
		jsonObject.put("properties", propertiesObject);

		return jsonObject.toString();
	}

	private static String PreprocessingMessage_finished() {
		
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("message", "set");
		jsonObject.put("messageType", "status");
		jsonObject.put("direction", "request");
		jsonObject.put("containerName", Container_Name);
		jsonObject.put("resource", Container_Name);
		JSONArray requiredArray = new JSONArray();
		requiredArray.add("configuration");
		jsonObject.put("required", requiredArray);
		JSONObject propertiesObject = new JSONObject();
		propertiesObject.put("configuration", "completed");
		jsonObject.put("properties", propertiesObject);
		
		return jsonObject.toString();
	}

	public static void setNode() {
		ArrayList<ArrayList<String>> eachResourceArrayList = new ArrayList<>();
		ArrayList<ArrayList<Node>> nodeGroupArrayList = new ArrayList<>();

		//ArrayList를 이용하여 Node 및 String 배열 생성
		for (String resource : resourceArrayList) {
			
			ArrayList<String> resourceElementArrayList = new ArrayList<>();
			ArrayList<Node> nodeElementArrayList = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(resource, ".");
			
			String pre_token=null;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if(pre_token != null) {
					token = pre_token+ "." + token;
				}
				resourceElementArrayList.add(token);
				nodeElementArrayList.add(new Node(token));
				pre_token = token;
			}
			nodeGroupArrayList.add(nodeElementArrayList);
			eachResourceArrayList.add(resourceElementArrayList);
		}

		//node를 연결하여 tree 구조로 변경 //MaxDepth 결정
		Node parent_Node = root_Node;
		Node child_Node = nodeGroupArrayList.get(0).get(0);
		Tree.add(parent_Node, child_Node);
	
		for(ArrayList<Node> nodeArray : nodeGroupArrayList){
			int tmp_MaxDepth=0;
			 parent_Node = root_Node;
			 child_Node =  nodeGroupArrayList.get(0).get(0);
			for(Node node : nodeArray) {
				tmp_MaxDepth++;
				if(node.getData().equals(Container_Name)) {
					parent_Node = child_Node;
					continue;
				}
				child_Node = node;
				Tree.add(parent_Node, child_Node);
				parent_Node = child_Node;
			}
		
			if(tmp_MaxDepth > MaxDepth) {
				MaxDepth = tmp_MaxDepth;
			}
		}
	
	}
	

	private void getItem() {
		
		ArrayList<JSONObject> itemsArrayList = new ArrayList<>();
		
		JSONObject properties_definition = (JSONObject) propertyObject.get(parse_definitions);
		
		Iterator<String> iter = properties_definition.keySet().iterator();
		while (iter.hasNext()) {
			String keys = iter.next();
			definitionArrayList.add(keys);
			JSONObject definitionObject = (JSONObject) properties_definition.get(keys);
			//System.out.println(keys + " : "+definitionObject.get("type").getClass().toString());
			//type이 String일 경우
			if(definitionObject.get("type") instanceof String) {
				definitinoTypeArrayList.add(definitionObject.get("type").toString());
			}
			if(definitionObject.get("type") instanceof JSONObject) {
				JSONObject typeObject = (JSONObject) definitionObject.get("type");
				JSONArray enumArray = (JSONArray) typeObject.get("enum");
				definitinoTypeArrayList.add(enumArray);
			}
			
			
		}
	

		JSONArray itemsArray = (JSONArray) propertyObject.get(parse_items);

		for (int i = 0; i < itemsArray.size(); i++) {
			ArrayList<String> propertiesArrayList = new ArrayList<>();
			JSONObject itemObject = (JSONObject) itemsArray.get(i);

			itemsArrayList.add(itemObject);
			JSONArray propertiesArray = (JSONArray) itemObject.get(parse_required);
			
			
			for(int j=0; j<propertiesArray.size(); j++) {
				propertiesArrayList.add(propertiesArray.get(j).toString());
				
			}
		
			resourceArrayList.add(itemObject.get(parse_resource).toString());
			propertyArrayList.add(propertiesArrayList);
			//System.out.println("resourceArrayList : " + resourceArrayList);
		
			
			JSONArray configurationArray = (JSONArray) configurationObject.get(parse_configuration);
		
			for(int j=0; j<configurationArray.size(); j++) {
				JSONObject configObject = (JSONObject) configurationArray.get(j);
				
				String resource = configObject.get(parse_resource).toString();
				
				if(resource.equals(itemObject.get(parse_resource).toString())) {
					endPointArrayList.add(configObject.get(parse_endPoint).toString());
					keyArrayList.add(configObject.get(parse_key).toString());
					//System.out.println("endPointArrayList : " + endPointArrayList);
					break;
					
				}
				
			}
		}
	}

}
