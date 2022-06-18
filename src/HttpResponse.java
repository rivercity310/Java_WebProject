import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
	public void send(OutputStream os, String file) throws IOException {
		// HTML ������ ����Ǵ� ��Ʈ ���丮�� web���� ����
		// class ������ ����Ǵ� bin ���丮 �Ʒ��� HTML ������ ������ web ���丮 ���� 
		// web/index.html
		file = "web" + file;    
		System.out.println("send file: " + file);
		
		String msg = "";
		if ((new File(file)).exists() == false) {
			msg = "<html><h1>404 Not Found</h1></html>";
		} else {
			// Files, Paths => java.nio(new io)�� ����
			byte bytes[] = Files.readAllBytes(Paths.get(file));
			msg = new String(bytes, "utf-8");      // �ѱ� ó��
		}
		
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		pw.println(msg);
		pw.flush();
		
	}
}
