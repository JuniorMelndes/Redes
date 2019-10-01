package camda_transporte;

import java.io.*; 
import java.net.*;

public class UDPServer {
	public static void main(String args[]) throws Exception { 
	String [] argumentos;
	String arg0;
	float arg1;
	float arg2;
	float capitalizedSentenceTemp = 0;
	String capitalizedSentence;
	String confirmacao = "Recebido!" + "\n";
	
	DatagramSocket serverSocket = new DatagramSocket(9876); 
	
	byte[] receiveData = new byte[1024]; 
	byte[] sendData  = new byte[1024]; 
	byte[] sla = new byte[1024];
	
	while(true) { 
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
		serverSocket.receive(receivePacket); 
		String sentence = new String(receivePacket.getData()); 
		
		InetAddress IPAddress = receivePacket.getAddress(); 
		
		int port = receivePacket.getPort(); 
		
		if (!(receivePacket.equals(null))) {
			sla = confirmacao.getBytes();
			DatagramPacket sendPacket1 = new DatagramPacket(sla, sla.length, IPAddress, port);
			serverSocket.send(sendPacket1);
		}
		
		//Aplicação
	    argumentos = sentence.split(" ");
	    arg0 = argumentos[0];
	    arg1 = Float.parseFloat(argumentos[1]);
	    arg2 = Float.parseFloat(argumentos[2]);
	    
	    if (arg0.equals("ADD")) {
			capitalizedSentenceTemp = arg1 + arg2;
		}
		if (arg0.equals("SUB")) {
			capitalizedSentenceTemp = arg1 - arg2;
		}
		if (arg0.equals("MULT")) {
			capitalizedSentenceTemp = arg1*arg2;
		}
		if (arg0.equals("DIV")) {
			capitalizedSentenceTemp = arg1/arg2;
		}
		if (arg0.equals("EXP")) {
			capitalizedSentenceTemp = (float) Math.pow(arg1,arg2);
		}
	    
		capitalizedSentence = Float.toString(capitalizedSentenceTemp) + "\n";
		//Aplicação
		
		sendData = capitalizedSentence.getBytes(); 
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); 
		
		serverSocket.send(sendPacket); 
	} 
  } 
}
