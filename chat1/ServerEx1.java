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
		//클라이언트에 메세지 보내기
		OutputStream out=clientSocket.getOutputStream();//보내는 빨대준비
		byte[]arr=new String("Hello").getBytes();
		
		out.write(arr);
		out.flush();
		out.close();
		
		
		//close
		clientSocket.close();
		server.close();
		
	}
}
