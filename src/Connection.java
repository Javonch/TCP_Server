import java.io.*;
import java.net.*;
// Method extending Thread that allows for multi-threading
public class Connection extends Thread{
	private InputStream in;
	private OutputStream out;
	
	public Connection(Socket con) throws IOException {
		this.in = con.getInputStream();
		this.out = con.getOutputStream();
	}
	/* Method for Multi-Threading to Properly Work
	 * This method writes the correct header and hands off the work onto 
	 * other abstracted methods
	 */
	public void run() {
		try {
			PrintWriter printOut = new PrintWriter(out);
			// Can't use Scanner because Java 7...
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			String reqType = input.readLine();
			// Base case: If nothing in response. Do nothing.
			if(reqType == null) {
				printOut.close();
				input.close();
				in.close();
				out.close();
			}
			else if (!reqType.contains("jtc131")) {
				writeError(printOut);
				printOut.flush();
				printOut.close();
				in.close();
				out.close();
			}
			
			else {
				int spacePlace = reqType.indexOf(" ");
				String website = reqType.substring(spacePlace, reqType.indexOf(" ", spacePlace + 1)).trim();
				String fileName = website.substring(website.indexOf('/', 1));
	
				/* If we have a file request we actually use, then start writing that
				 * file to them
				 */
				
		if(fileName.equals("/test1.html") || fileName.equals("/test2.html")|| fileName.equals("/visits.html")) {
			printOut.write("HTTP/ 1.1 200 OK \r\n");
			printOut.write("Content type: text/html \r\n");
			printOut.write("Keep-Alive: timeout=100 \r\n");
			printOut.write("Connection: Keep-Alive, keep-alive \r\n");
			int visits = TCP_Server.checkVisits(input, printOut, fileName);
			
			if(fileName.equals("/visits.html")){
				//“Your browser visited various URLs on this site X times”
				String sentence = "Your browser visited various URLs on this site " + visits + " times";
				printOut.write("content-length: " + sentence.length());
				printOut.write("\r\n\r\n");
				printOut.write(sentence);
			}
			else
				write(printOut,fileName);
		}
		
		//Else just send the 404 Error and close up.
		else {
			writeError(printOut);
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
	/* 
	 * The write method is a procedure that takes the given printWriter
	 * and writes what's in the html file to the client response. 
	 */
	private static void write(PrintWriter pw, String _file) {
		try {
			String file = "web_files" + _file;
			File f = new File(file);
			BufferedReader br = new BufferedReader(new FileReader(f));
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
	
	private static void writeError(PrintWriter pw) {
		pw.write("HTTP/1.1 404 Not Found \r\n\r\n");
		pw.write("404 Error: Page Not Found");
	}
}
