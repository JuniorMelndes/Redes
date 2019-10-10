package transporte_redes;

import java.io.*;
import java.net.*;
import java.time.Duration;

public class UDPClient {
	private static final String ACK = "ACK";
	private static final int TIMEOUT = 2;
	private static final int PORT = 9876;
	private static final int BYTES = 2014;
	private static final String IP = "localhost"; 
	
	public static void main(String args[]) throws Exception { 
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		InetAddress IPAddress = InetAddress.getByName(IP);

		String sentence = inFromUser.readLine();

		String retorno = realizaEnvio(IPAddress, sentence);
		System.out.println("FROM SERVER:" + retorno);
	    } 
	
	private static String realizaEnvio(InetAddress IPAddress, String sentence) throws SocketException {
		DatagramSocket clientSocket = new DatagramSocket();
		String retorno = "";
		try {
			byte[] receiveData = new byte[BYTES];
			byte[] sendData;
			sendData = sentence.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);

			clientSocket.send(sendPacket);

			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.setSoTimeout((int)Duration.ofSeconds(TIMEOUT).toMillis());
			clientSocket.receive(receivePacket);

			String response = new String(receivePacket.getData());
			if (equals(response,ACK)) {
				clientSocket.setSoTimeout((int)Duration.ofSeconds(TIMEOUT).toMillis());
				clientSocket.receive(receivePacket);
				retorno = new String(receivePacket.getData());
				sendData = String.valueOf(ACK).getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
				clientSocket.send(sendPacket);
			} else {
				retorno = realizaEnvio(IPAddress, sentence);
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			retorno = realizaEnvio(IPAddress, sentence);
		
		}catch (IOException e) {
			e.printStackTrace();
		}

		clientSocket.close();
		return retorno;
	}

	private static boolean equals(String valor1, String valor2) {
		String[] array1 = valor1.split("");
		String[] array2 = valor2.split("");
		boolean retorno = true;
		int tamanho = array1.length<array2.length?array1.length:array2.length;
		for(int i = 0; i < tamanho && retorno; i++) {
			retorno = array1[i].equals(array2[i]);
		}
		return retorno;
	}

}

