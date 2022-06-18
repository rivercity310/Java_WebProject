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
			if (!file.equals("/favicon.ico")) {
				System.out.println("File name: " + file);
				HttpResponse res = new HttpResponse();
				res.send(sock.getOutputStream(), file);
			}
			
			sock.close();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
