import java.net.*;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.io.*;
public class TCP_Server {
	
	/*
	 * Method that takes the requests from the client and puts them into a 
	 * LinkedList in order to parse them easily
	 */
	public static LinkedList<String> getRequest(Scanner inputSet) {
		LinkedList<String> requestList = new LinkedList<String>();
		String request = inputSet.nextLine();
	
		// While there is a connection request needed to be parsed
	while(inputSet.hasNextLine() && !request.equals("")) {
		System.out.println(request);
		requestList.add(request);
		String website = inputSet.nextLine();
		request = website.substring(website.lastIndexOf('/', 1));
		
	}
	return requestList;
	}
	
	public static void setCookie(String cookieName, int cookieVal, PrintWriter pw) {
		pw.write("Set-Cookie: " + cookieName +"=" + cookieVal + " \r\n");
	}
	
	/*
	 *  Method to see if there is a visit to a certain cookie
	 *  Created so i didn't have to hard code cookie names
	 */
	
	public static int cookieCheck(BufferedReader requests, String cookieType) throws IOException {
		String looking = requests.readLine();
		while(!looking.contains("Cookie") && !looking.isBlank()) {
			looking = requests.readLine();
		}
		if(looking.contains("Cookie")) {
			String[] cookies = looking.split(";");
			
			for(String cookie: cookies) {
				if(cookie.contains(cookieType)) {
					return Integer.valueOf(cookie.substring(cookie.indexOf('=') + 1));
				}
			}
		}
		
		
		return -1;
	}
	
	// method to check and increment visits to the sites
	public static int checkVisits(BufferedReader br, PrintWriter pw, String fileName) throws NumberFormatException, IOException {
		if (fileName.equals("visits.html") || fileName.equals("test1.html") || fileName.equals("test2.html")){
			int visitCookie = cookieCheck(br, "jtc131visits");
			
			if(visitCookie == -1) {
				setCookie("jtc131visits", 1, pw);
				return 1;
			}
			setCookie("jtc131visits",visitCookie + 1, pw);
			return visitCookie;
					}
		return -1;
		}
	
	// Main method for server to initialize
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
