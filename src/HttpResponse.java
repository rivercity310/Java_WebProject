import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpResponse {
	public void send(OutputStream os, String file) throws IOException {
		// HTML 파일이 저장되는 루트 디렉토리를 web으로 지정
		// class 파일이 저장되는 bin 디렉토리 아래에 HTML 파일을 저장할 web 디렉토리 생성 
		// web/index.html
		file = "web" + file;    
		System.out.println("send file: " + file);
		
		String msg = "";
		if ((new File(file)).exists() == false) {
			msg = "<html><h1>404 Not Found</h1></html>";
		} else {
			// Files, Paths => java.nio(new io)에 존재
			byte bytes[] = Files.readAllBytes(Paths.get(file));
			msg = new String(bytes, "utf-8");      // 한글 처리
		}
		
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(os, "utf-8"));
		pw.println(msg);
		pw.flush();
		
	}
}
