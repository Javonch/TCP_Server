import java.net.*;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.io.*;
public class TCP_Server {
	
	public static LinkedList<String> getRequest(Scanner inputSet) {
		LinkedList<String> requestList = new LinkedList<String>();
		String request = inputSet.nextLine();
		
	while(inputSet.hasNextLine() && !request.equals("")) {
		System.out.println(request);
		requestList.add(request);
		String website = inputSet.nextLine();
		request = website.substring(website.lastIndexOf('/', 1));
		
	}
	return requestList;
	}
	
	public static void setCookie(String cookieName, String cookieVal, PrintWriter pw) {
		pw.write("Set-Cookie: " + cookieName +"=" + cookieVal + " \r\n");
	}
	
	public static String cookieCheck(BufferedReader requests, String cookieType) throws IOException {
		String looking = requests.readLine();
		while(!looking.contains("Cookie") && !looking.equals("")) {
			looking = requests.readLine();
		}
		
		while(looking.contains("Cookie")) {
			if (looking.contains(cookieType)) {
				return looking.substring(looking.indexOf("=") + 1);
			}
		}
		
		return "-1";
	}
	
	public static int checkVisits(BufferedReader br, PrintWriter pw, String fileName) throws NumberFormatException, IOException {
		if (fileName.equals("/visits.html") || fileName.equals("/test1.html") || fileName.equals("/jtc131/test2.html")){
			int visitCookie = Integer.parseInt(cookieCheck(br, "visits"));
			
			if(visitCookie == -1) {
				setCookie("visits", "1", pw);
				return 1;
			}
			setCookie("visits",String.valueOf(visitCookie + 1), pw);
			return visitCookie;
					}
		return -1;
		}
	public static void connect() throws IOException, InterruptedException {
		Properties prop = new Properties();
		FileInputStream ip = new FileInputStream("config.properties");
		prop.load(ip);
		String port = prop.getProperty("port");
		try (ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));) {
			while (true) {
				Socket connectionSocket = serverSocket.accept();
				Thread connection = new Connection(connectionSocket);
				connection.start();
				connection.join();
			}
		}
			
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connection Failed...");
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		connect();
	}
}
