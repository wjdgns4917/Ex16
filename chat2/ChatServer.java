package com.chat2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;   
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ChatServer extends Frame implements ActionListener{
	Button btnExit;
	TextArea ta;
	Vector vChatList;
	ServerSocket ss;
	Socket sockClient;
	
	public ChatServer() {
		//채팅화면구성
		setTitle("GUI 채팅 서버 ver 1.0");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("신호받기");
				dispose(); //자원 반납후 프레임닫기
			}
		});
		vChatList=new Vector<>();
		btnExit=new Button("서버종료");
		btnExit.addActionListener(this);//버튼에 수신자 부착
		ta=new TextArea();
		
		//판넬에 부착
		add(ta,BorderLayout.CENTER);
		add(btnExit,BorderLayout.SOUTH);
		
		setBounds(250, 250, 200, 200);
		setVisible(true);
		
		chatStart();	
	}
	public void chatStart() {
		try {
			ss=new ServerSocket(6000);
			while (true) { //무한 소켓 접속
				sockClient=ss.accept();
				ta.append(sockClient.getInetAddress().getHostAddress()
						+"접속함\n");
				ChatHandle threadChat=new ChatHandle();
				//채팅내용 vector에 저장
				vChatList.add(threadChat);
				threadChat.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
	class ChatHandle extends Thread{
		BufferedReader br=null; //입력담당
		PrintWriter pw=null; //출력담당
		
		public ChatHandle() {
			try {
				InputStream is=sockClient.getInputStream(); //입력
				br=new BufferedReader(new InputStreamReader(is));
				OutputStream os=sockClient.getOutputStream(); //출력
				pw=new PrintWriter(new OutputStreamWriter(os));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //입력
		}
		public void sendAllClient(String msg) {
			try {
				int size=vChatList.size();
				for (int i = 0; i < size; i++) {
					ChatHandle chr=(ChatHandle)vChatList.elementAt(i);
					chr.pw.println(msg);
					chr.pw.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {
				String name=br.readLine();
				sendAllClient(name+" 님께서 새로 입장");
				//채팅 내용 받는 while
				while (true) {
					String msg=br.readLine();
					String str=sockClient.getInetAddress().getHostName();
					ta.append(msg+"\n");
					if(msg.equals("@@Exit"))
						break;
					else {
						sendAllClient(name+" : "+msg);
					}
				}	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				vChatList.remove(this);
				try {
					br.close();
					pw.close();
					sockClient.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
	
}
