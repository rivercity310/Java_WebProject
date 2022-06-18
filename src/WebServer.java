import java.util.HashMap;
import java.io.*;
import java.net.*;


class ServerThread implements Runnable {
	private Socket sock;
	
	public ServerThread(Socket sock) {
		this.sock = sock;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("IP: " + sock.getInetAddress());
			System.out.println("Port: " + sock.getPort());
			
			HttpRequest req = new HttpRequest();
			String msg = req.receive(sock.getInputStream());
			
			String file = req.getFile(msg);		
			System.out.println("File name: " + file);
			
			Session ses = Session.getInstance();
			ses.set("localhost", file);
			
			HttpResponse res = new HttpResponse();
			res.send(sock.getOutputStream(), file);
			
			
			sock.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

// Session: 서버측에서 임시적으로 데이터를 저장하기 위함 (default: 30분), HashMap으로 구현
// getInstance() 메소드를 static으로 구현하여 공유 객체를 지원하도록 구현
// 굳이 파라미터를 전달하지 않고 어디서든 getInstance 메소드를 통해 객체를 얻어낼 수 있음
// -> Singleton 패턴: 객체가 오직 하나만 생성될 수 있도록 static으로 인스턴스 선언
class Session {
	private HashMap<String, Object> table;
	private static Session instance;
	
	// new Session() 형태로 생성자를 호출할 경우, 기존 방식처럼 객체를 별도로 생성
	public Session() {
		table = new HashMap<String, Object>();
	}
	
	// Session.getInstance()로 호출할 경우, 공유 객체를 리턴 
	public static Session getInstance() {
		if (instance == null) instance = new Session();
		return instance;
	}
	
	public void set(String key, Object val) {
		table.put(key, val);
	}
	
	public Object get(String key) {
		return table.get(key);
	}
	
	public Object remove(String key) {
		return table.remove(key);
	}
}

public class WebServer {
	public static void main(String[] args) throws InterruptedException {
		try {
			@SuppressWarnings("resource")
			ServerSocket srvsock = new ServerSocket(80);
			System.out.println("Server Started");
						
			while (true) {
				Socket sock = srvsock.accept();
				Thread thread = new Thread(new ServerThread(sock));
				thread.start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
