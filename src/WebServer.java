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

// Session: ���������� �ӽ������� �����͸� �����ϱ� ���� (default: 30��), HashMap���� ����
// getInstance() �޼ҵ带 static���� �����Ͽ� ���� ��ü�� �����ϵ��� ����
// ���� �Ķ���͸� �������� �ʰ� ��𼭵� getInstance �޼ҵ带 ���� ��ü�� �� �� ����
// -> Singleton ����: ��ü�� ���� �ϳ��� ������ �� �ֵ��� static���� �ν��Ͻ� ����
class Session {
	private HashMap<String, Object> table;
	private static Session instance;
	
	// new Session() ���·� �����ڸ� ȣ���� ���, ���� ���ó�� ��ü�� ������ ����
	public Session() {
		table = new HashMap<String, Object>();
	}
	
	// Session.getInstance()�� ȣ���� ���, ���� ��ü�� ���� 
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
