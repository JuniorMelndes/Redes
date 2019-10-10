package transporte_redes;

import java.io.*;
import java.net.*;
import java.time.Duration;
public class UDPServer {
	
	private static final String ACK = "ACK";
	private static DatagramSocket serverSocket; ;
	private static final int PORT = 9999;
	private static final int BYTES = 2014;
	private static final int TIMEOUT = 3;
	
	public static void main(String args[]) throws Exception { 
		String [] argumentos;
		String arg0;
		float arg1;
		float arg2;
		
		
		byte[] receiveData = new byte[BYTES]; 
		byte[] sendData = new byte[BYTES]; 
		
		while(true) { 
			serverSocket = new DatagramSocket(PORT);
			
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
			serverSocket.receive(receivePacket); 
			InetAddress IPAddress = receivePacket.getAddress(); 
			
			String sentence = new String(receivePacket.getData()); 
			
			int port = receivePacket.getPort(); 
			
			sendData = String.valueOf(ACK).getBytes();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);			
			serverSocket.send(sendPacket); 
			
		    argumentos = sentence.split(" ");
		    arg0 = argumentos[0];
		    arg1 = Float.parseFloat(argumentos[1]);
		    arg2 = Float.parseFloat(argumentos[2]);
		    
		    float resultado = aplicacao(arg0,arg1,arg2); 
			sendData = String.valueOf(resultado).getBytes();
			
			envio(receivePacket,sendData);
		    
		} 
	} 
	private static void envio(DatagramPacket receivePacket,byte[] sendData) throws IOException{
		byte[] receiveData = new byte[BYTES]; 
		
		InetAddress IPAddress = receivePacket.getAddress(); 
		
		int port = receivePacket.getPort(); 
			
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);			
		serverSocket.send(sendPacket); 
		DatagramPacket receivePacketConfirm = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.setSoTimeout((int)Duration.ofSeconds(TIMEOUT).toMillis());
		serverSocket.receive(receivePacketConfirm);
		String sentence = new String(receivePacketConfirm.getData());
		if(!comparador(sentence, ACK)) {
			envio(receivePacketConfirm, sendData);
		}
		serverSocket.close();
	}
	
	private static boolean comparador(String valor1, String valor2) {
		String[] array1 = valor1.split("");
		String[] array2 = valor2.split("");
		boolean retorno = true;
		int tamanho = array1.length<array2.length?array1.length:array2.length;
		for(int i = 0; i < tamanho && retorno; i++) {
			retorno = array1[i].equals(array2[i]);
		}
		return retorno;
	}
	
	private static float aplicacao(String operacao, float num1, float num2) {
		float result = 0;
		if (operacao.equals("ADD")) {
			result = num1 + num2;
		}
		if (operacao.equals("SUB")) {
			result = num1 - num2;
		}
		if (operacao.equals("MULT")) {
			result = num1*num2;
		}
		if (operacao.equals("DIV")) {
			result = num1/num2;
		}
		if (operacao.equals("EXP")) {
			result = (float) Math.pow(num1,num2);
		}
		return result;
	}
}
