package initProject;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import Tree.*;
import rabbitMQ.SendToServer;
import Response.perform.*;

public class AnalyzeRequest {

	public static void analyzeRequest(String routingKey, String mqMessage) {
		try {
			// 01. message를 parsing. resource 결과를 가져온다.
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(mqMessage);
			JSONObject jsonObject = (JSONObject) obj;

			String message = jsonObject.get("message").toString();
			String message_type = jsonObject.get("messageType").toString();
			String resource = jsonObject.get("resource").toString();

			// 02_1. resource 분석
			Node requestNode = Tree.findData(Parameter.root_Node, resource);
			if (requestNode == null) {
				System.out.println("Can't use That resource : " + resource);
			}
			ArrayList<Node> nodeArrayList = new ArrayList<>();

			// 02_2. 요청한 node의 tail노드를 가져와 nodeArrayList에 저장한다.
			Tree.getTailNode(requestNode, 0, nodeArrayList);
			String topic = message + "." + message_type;

			if (topic.equals("get.property")) {
				SendToServer.send("get.property", Parameter.propertyObject.toString());
				return;
			}
			ArrayList<JSONObject> responseArrayList = new ArrayList<>();
			for (Node node : nodeArrayList) {
				if (topic.equals("get.status")) {
					

					// 대기 오염 정보 요청일 경우
					if (node.getData().equals(Parameter.Container_Name +".ArpltnInforInqireSvc.CtprvnMesureLIst")) {
						System.out.println("Complete AirPollutionInformationInquireService");
						ArrayList<JSONObject> airPollutionInfoArray = new ArrayList<>();
						airPollutionInfoArray = AirPollutionInfo.getJSONArrayList();
						AirPollutionInfo.getPropertyArray(); // 속성 정보를 반환
						ArrayList<String> RequestPropertyArray = AirPollutionInfo.getRequestPropertyArray(jsonObject);
						boolean Error_check = AirPollutionInfo.comparePropertyArray(RequestPropertyArray);
						if (Error_check == false) {
							AirPollutionInfo.responseError();
							return;
						} else {
							responseArrayList.add(AirPollutionInfo.decidegetResponse(airPollutionInfoArray, RequestPropertyArray));
						}
					}
					// 동네 날씨 정보 요청일 경우
					if (node.getData().equals(Parameter.Container_Name +".SecndSrtpdFrcstInfoService2.ForecastSpaceData")) {
						System.out.println("Complete ForecastInformationInquireService");
						JSONObject forcastObject = FrcstInfo.getJSONObject(); // 요청한 JSON 정보를 반환
						FrcstInfo.getPropertyArray(); // 속성 정보를 반환
						ArrayList<String> RequestPropertyArray = FrcstInfo.getRequestPropertyArray(jsonObject);
						boolean Error_check = FrcstInfo.comparePropertyArray(RequestPropertyArray);
						if (Error_check == false) {
							FrcstInfo.responseError();
							return;
						} else {
							responseArrayList.add(FrcstInfo.decidegetResponse(forcastObject, RequestPropertyArray));
						}
					}
					

				} else {
					System.out.println("Dont have that Request");
					//ResponseResult.responseError(jsonObject);
				}
			}
			ResponseResult.responseGetStatusRequest(responseArrayList);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
