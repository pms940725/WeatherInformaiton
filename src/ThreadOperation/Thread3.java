package ThreadOperation;

import Response.perform.AirPollutionInfo;
import Response.perform.FrcstInfo;
import initProject.Parameter;

//주기적으로 정보가 update되면 notify 수행
public class Thread3 extends Thread {

	public void run() {

		try {
			while (true) {
				Thread.sleep(1000);
				if(Parameter.operationRunning == true) {
					
					Thread.sleep(60000);
					
					AirPollutionInfo.notifyUpdatedPM10();
					AirPollutionInfo.notifyUpdatedPM25();
					FrcstInfo.notifyUpdatedPTY();
					FrcstInfo.notifyUpdatedSKY();
					FrcstInfo.notifyUpdatedT3H();
		
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
