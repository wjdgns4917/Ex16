package com.chat1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEx1 {
	public static void main(String[] args) throws IOException {
		ServerSocket server=new ServerSocket(5005);
		System.out.println("ready~~~~~");
		Socket clientSocket=server.accept();
		//Ŭ���̾�Ʈ�� �޼��� ������
		OutputStream out=clientSocket.getOutputStream();//������ �����غ�
		byte[]arr=new String("Hello").getBytes();
		
		out.write(arr);
		out.flush();
		out.close();
		
		
		//close
		clientSocket.close();
		server.close();
		
	}
}
