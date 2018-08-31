package Response.perform;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import initProject.Parameter;
import rabbitMQ.SendToServer;

public class ResponseResult {

	
	@SuppressWarnings("unchecked")
	public static void responseGetStatusRequest(ArrayList<JSONObject> responseArrayList) {
		if(responseArrayList.isEmpty()) {
			System.out.println("responseGetStatusRequest is Empty");
			return;
		}
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("message", "get");
		responseObject.put("messageType", "status");
		responseObject.put("direction", "response");
		responseObject.put("responseCode", 200);
		responseObject.put("containerName", Parameter.Container_Name);
		JSONArray res_itemsArray = new JSONArray();
		
		for(JSONObject jsonObject : responseArrayList) {
			res_itemsArray.add(jsonObject);
		}
		
		responseObject.put("items", res_itemsArray);
		//System.out.println(responseObject);
		SendToServer.send("get.status", responseObject.toString());
	}
	
	@SuppressWarnings("unchecked")
	public static void responseError(JSONObject reqObject) {
		JSONObject responseObject = new JSONObject();
		
		
		responseObject.put("message", reqObject.get("message").toString());
		responseObject.put("messageType", reqObject.get("messageType").toString());
		responseObject.put("direction", "response");
		responseObject.put("responseCode", 403);
		responseObject.put("containerName", Parameter.Container_Name);
		
		SendToServer.send("get.status", responseObject.toString());
	}
	
}
