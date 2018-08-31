package ThreadOperation;

import org.json.simple.JSONObject;

import initProject.Parameter;
import rabbitMQ.SendToServer;

//Keep Alive를 수행하는 Thread
@SuppressWarnings("unchecked")
public class Thread2 extends Thread{
	
	
	
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000);
				if (Parameter.operationRunning == true) {
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("message", "notify");
					jsonObject.put("messageType", "keepAlive");
					jsonObject.put("containerName", Parameter.Container_Name);
					jsonObject.put("resource", Parameter.Container_Name);

					SendToServer.send("notify.keepAlive", jsonObject.toString());
					Thread.sleep(60000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
