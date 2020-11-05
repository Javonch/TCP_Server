import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
public class TCPClient {
	public static void main (String[] args) throws Exception {
		String sentence;
		String modSentence;
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader (System.in));
		//Input stream reader
		Socket cSock = new Socket("", 5012);
		//Client Socket that connects to server using the 5012 port
		DataOutputStream outToServer = new DataOutputStream(cSock.getOutputStream());
		//Output stream from socket
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(cSock.getInputStream()));
		//Input stream to socket
		
		sentence = inFromUser.readLine(); 
		outToServer.writeBytes(sentence + '\n');
		
		modSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modSentence);
		
		cSock.close();
	}
}
