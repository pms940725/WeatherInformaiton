package ThreadOperation;

import initProject.CheckFile;
import initProject.Parameter;
import rabbitMQ.RPCClient;
import rabbitMQ.ReceiveFromServer;

public class Thread1 extends Thread {
	public void run() {
		try {

			// Container 중복 검사
			// 서버 내부 DB 오류 500
			// 중복 오류 403
			// 정상 응답 200
			while (true) {
				if (Parameter.Check_Overlapping(RPCClient.RPCRequest(Parameter.PreprocessingMessage_unfinished))) {
					break;
				}
				Thread.sleep(3000);
			}
			// property.json 파일 존재 여부 확인
			while (true) {
				if (true == CheckFile.haveFile(Parameter.filePath_Property)) {
					System.out.println("have Property.json");
					new Parameter();
					Thread.sleep(1000);
					break;
				}
			}

			// 서버로부터 receive 수행
			ReceiveFromServer.receive();
			
		
			// 환경 설정 완료 요청
			while (true) {
				if (Parameter.Check_clearConfiguration(RPCClient.RPCRequest(Parameter.PreprocessingMessage_finished))) {
					break;
				}
			}
			Parameter.operationRunning = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
