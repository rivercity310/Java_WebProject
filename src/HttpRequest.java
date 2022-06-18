import java.io.*;

public class HttpRequest {
	public String receive(InputStream is) throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		byte buf[] = new byte[1024];
		int cnt;
		
		while ((cnt = is.read(buf)) != -1) {
			bao.write(buf);
			if (cnt < buf.length) break;
		}
		
		return bao.toString();
	}
	
	public String getFile(String msg) {
		String lines[] = msg.split("\n");
		if (lines.length == 0) return null;
		
		//System.out.println("first: " + lines[0]);
		String tokens[] = lines[0].trim().split(" ");
		if (tokens.length < 2) return null;
		if (!(tokens[0].equals("GET") || tokens[0].equals("POST"))) return null;
		
		return tokens[1];
	}
}
