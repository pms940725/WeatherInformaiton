package initProject;
//main
import java.util.ArrayList;

import ThreadOperation.Thread1;
import ThreadOperation.Thread2;
import ThreadOperation.Thread3;
import rabbitMQ.RPCClient;
import rabbitMQ.ReceiveFromServer;

public class MainLoop {

	private static void mainLoop() {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try {

			//mainThread
			Thread t1 = new Thread(new Thread1());
			t1.start();
			Parameter.threadArray.add(t1);
			//KeepAlive Thread
			Thread t2 = new Thread(new Thread2());
			//t2.start();
			Parameter.threadArray.add(t2);
			Thread t3 = new Thread(new Thread3());
			//t3.start();
			Parameter.threadArray.add(t3);


			// Thread를 join 시킴
			for (Thread t : Parameter.threadArray) {
				t.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//mainLoop();

		System.out.println("Main End");
	}

}
