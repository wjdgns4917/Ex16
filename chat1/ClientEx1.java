package com.chat1;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientEx1 {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket=new Socket("192.168.0.80", 6000);
		System.out.println("����Ϸ�: "+socket);
		
		//�ޱ�
		InputStream in=socket.getInputStream();//���� ���� �غ�
		byte[]buffer=new byte[10];
		int count=in.read(buffer);
		System.out.println("����: "+count);
		String str=new String(buffer);
		System.out.println(str);
		
		in.close();
		socket.close();
		
	}
}
