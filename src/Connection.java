import java.io.*;
import java.net.*;

public class Connection extends Thread{
	private InputStream in;
	private OutputStream out;
	
	public Connection(Socket con) throws IOException {
		this.in = con.getInputStream();
		this.out = con.getOutputStream();
	}
	
	public void run() {
		try {
			PrintWriter printOut = new PrintWriter(out);
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			String reqType = input.readLine();
			if(reqType == null) {
				printOut.close();
				input.close();
				in.close();
				out.close();
			}
			else {
				int spacePlace = reqType.indexOf(" ");
				String website = reqType.substring(spacePlace, reqType.indexOf(" ", spacePlace + 1)).trim();
				String fileName = website.substring(website.indexOf('/', 1));
		
		if(fileName.equals("/test1.html") || fileName.equals("/test2.html")|| fileName.equals("/visits.html")) {
			printOut.write("HTTP/ 1.1 200 OK \r\n");
			printOut.write("Content type: text/html \r\n");
			printOut.write("Keep-Alive: timeout=100 \r\n");
			printOut.write("Connection: Keep-Alive, keep-alive \r\n");
			int visits = TCP_Server.checkVisits(input, printOut, fileName);
			
			if(fileName.equals("/visits.html")){
				String sentence = "You have visited this site " + visits +" times";
				printOut.write("content-length: " + sentence.length());
				printOut.write("\r\n\r\n");
				printOut.write(sentence);
			}
			else
				write(printOut,fileName);
		}
		
		else {
			printOut.write("HTTP/1.1 404 Not Found \r\n\r\n");
			printOut.write("404 Error: Page Not Found");
		} 
			printOut.flush();
			printOut.close();
			in.close();
			out.close();
			}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Connection Failed to Terminate");
				System.exit(-1);
				}
		}
	
	private static void write(PrintWriter pw, String _file) {
		try {
			String file = "web_files" + _file;
			File f = new File(file);
			BufferedReader br = new BufferedReader(new FileReader(f));
			//pw.write("content-length: " + f.length());
			String line;
			pw.write("\r\n\r\n");
			while((line = br.readLine()) != null) {
				pw.write(line);
			}
			br.close();
	}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println("Read Failed...");
			System.exit(-1);
		}
	}
}
