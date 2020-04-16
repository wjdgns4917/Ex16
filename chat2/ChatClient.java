package com.chat2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
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
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends Frame implements ActionListener,Runnable {	
	Button btn_exit; //종료
	Button btn_send; //전송
	Button btn_connect; //접속

	TextArea txt_list; //채팅 내용 출력
	TextField txt_server_ip; //서버IP
	TextField txt_name; //대화명
	TextField txt_input; //채팅입력창
	
	Socket client; //소켓
	BufferedReader br; //입력
	PrintWriter pw; //출력
	String server_ip; //서버IP
	final int port=6000; //서버 포트 번호
	
	CardLayout cl; //레이아웃 선언
	
	public ChatClient() {
		setTitle("채팅 클라이언트");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		cl=new CardLayout();
		this.setLayout(cl); //레이아웃을 설정
		
		Panel connect=new Panel();
		connect.setBackground(Color.LIGHT_GRAY);
		connect.setLayout(new BorderLayout());
		
		btn_connect=new Button("서버접속");
		btn_connect.addActionListener(this); //버튼에 수신기 부착
		txt_server_ip=new TextField("127.0.0.1",15); //자신의 IP
		txt_name=new TextField("홍길동",15);
		Panel connect_sub=new Panel();
//		connect_sub 판넬에 내용물 부착
		connect_sub.add(new Label("서버아이피(IP):")); //라벨부착
		connect_sub.add(txt_server_ip);		
		connect_sub.add(new Label("대화명:")); //라벨부착
		connect_sub.add(txt_name);
		
		//채팅화면 판넬 구성
		Panel chat=new Panel();
		chat.setLayout(new BorderLayout());
		Label lblChat=new Label("채팅 접속화면",Label.CENTER);
		
		//connect 판넬에 내용부착
		connect.add(lblChat,BorderLayout.NORTH);
		connect.add(connect_sub,BorderLayout.CENTER);
		connect.add(btn_connect,BorderLayout.SOUTH);
		
		//채팅창 화면구성
		txt_list=new TextArea(); //채팅창 보여주는 부분
		txt_input=new TextField("", 25); //채팅 입력
		btn_exit=new Button("종료");
		btn_send=new Button("전송");
		
		//수신기 부착
		btn_connect.addActionListener(this);
		btn_send.addActionListener(this);
		txt_input.addActionListener(this);
		
		Panel chat_sub=new Panel(); //채팅창 하위 판넬
		
		//내용물 부착
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);
		
		Label lblChatTitle=new Label("채팅 프로그램 v1",Label.CENTER);
		chat.add(lblChatTitle,BorderLayout.NORTH);
		chat.add(txt_list,BorderLayout.CENTER);
		chat.add(chat_sub,BorderLayout.SOUTH);
		
		//프레임에 최종부착
		add(connect, "접속창");
		add(chat,"채팅창");
		
		cl.show(this, "접속창");
		setBounds(500, 250, 300, 300);
		setVisible(true);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println("신호수신asdf");
		Object obj=e.getSource();
		if(obj==btn_connect) {
			server_ip=txt_server_ip.getText();
			Thread th=new Thread(this);
			th.start(); //-->run
			cl.show(this, "채팅창");
			
		}else if (obj==btn_exit) {
			System.exit(0); //시스템 종료
		}else if (obj==btn_send || obj==txt_input) {
			String msg=txt_input.getText(); //메세지 호출
			pw.println(msg);
			pw.flush();
			txt_input.setText(""); //초기화 내용삭제
			txt_input.requestFocus();
			
		} 
		
		
	}
	@Override
	public void run() {
		try {
			client=new Socket(server_ip, port);
			InputStream is=client.getInputStream(); //입력
			br=new BufferedReader(new InputStreamReader(is));
			OutputStream os=client.getOutputStream(); //출력
			pw=new PrintWriter(new OutputStreamWriter(os));
			
			//메세지 전달
			String msg=txt_name.getText(); //대화명 가져오기
			pw.println(msg);
			pw.flush(); //나머지 전송
			txt_input.requestFocus(); //커서 이동
			while (true) {
				msg=br.readLine();
				txt_list.append(msg+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		new ChatClient();
	}

}
