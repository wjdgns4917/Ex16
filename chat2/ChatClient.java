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
	Button btn_exit; //����
	Button btn_send; //����
	Button btn_connect; //����

	TextArea txt_list; //ä�� ���� ���
	TextField txt_server_ip; //����IP
	TextField txt_name; //��ȭ��
	TextField txt_input; //ä���Է�â
	
	Socket client; //����
	BufferedReader br; //�Է�
	PrintWriter pw; //���
	String server_ip; //����IP
	final int port=6000; //���� ��Ʈ ��ȣ
	
	CardLayout cl; //���̾ƿ� ����
	
	public ChatClient() {
		setTitle("ä�� Ŭ���̾�Ʈ");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		cl=new CardLayout();
		this.setLayout(cl); //���̾ƿ��� ����
		
		Panel connect=new Panel();
		connect.setBackground(Color.LIGHT_GRAY);
		connect.setLayout(new BorderLayout());
		
		btn_connect=new Button("��������");
		btn_connect.addActionListener(this); //��ư�� ���ű� ����
		txt_server_ip=new TextField("127.0.0.1",15); //�ڽ��� IP
		txt_name=new TextField("ȫ�浿",15);
		Panel connect_sub=new Panel();
//		connect_sub �ǳڿ� ���빰 ����
		connect_sub.add(new Label("����������(IP):")); //�󺧺���
		connect_sub.add(txt_server_ip);		
		connect_sub.add(new Label("��ȭ��:")); //�󺧺���
		connect_sub.add(txt_name);
		
		//ä��ȭ�� �ǳ� ����
		Panel chat=new Panel();
		chat.setLayout(new BorderLayout());
		Label lblChat=new Label("ä�� ����ȭ��",Label.CENTER);
		
		//connect �ǳڿ� �������
		connect.add(lblChat,BorderLayout.NORTH);
		connect.add(connect_sub,BorderLayout.CENTER);
		connect.add(btn_connect,BorderLayout.SOUTH);
		
		//ä��â ȭ�鱸��
		txt_list=new TextArea(); //ä��â �����ִ� �κ�
		txt_input=new TextField("", 25); //ä�� �Է�
		btn_exit=new Button("����");
		btn_send=new Button("����");
		
		//���ű� ����
		btn_connect.addActionListener(this);
		btn_send.addActionListener(this);
		txt_input.addActionListener(this);
		
		Panel chat_sub=new Panel(); //ä��â ���� �ǳ�
		
		//���빰 ����
		chat_sub.add(txt_input);
		chat_sub.add(btn_send);
		chat_sub.add(btn_exit);
		
		Label lblChatTitle=new Label("ä�� ���α׷� v1",Label.CENTER);
		chat.add(lblChatTitle,BorderLayout.NORTH);
		chat.add(txt_list,BorderLayout.CENTER);
		chat.add(chat_sub,BorderLayout.SOUTH);
		
		//�����ӿ� ��������
		add(connect, "����â");
		add(chat,"ä��â");
		
		cl.show(this, "����â");
		setBounds(500, 250, 300, 300);
		setVisible(true);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println("��ȣ����asdf");
		Object obj=e.getSource();
		if(obj==btn_connect) {
			server_ip=txt_server_ip.getText();
			Thread th=new Thread(this);
			th.start(); //-->run
			cl.show(this, "ä��â");
			
		}else if (obj==btn_exit) {
			System.exit(0); //�ý��� ����
		}else if (obj==btn_send || obj==txt_input) {
			String msg=txt_input.getText(); //�޼��� ȣ��
			pw.println(msg);
			pw.flush();
			txt_input.setText(""); //�ʱ�ȭ �������
			txt_input.requestFocus();
			
		} 
		
		
	}
	@Override
	public void run() {
		try {
			client=new Socket(server_ip, port);
			InputStream is=client.getInputStream(); //�Է�
			br=new BufferedReader(new InputStreamReader(is));
			OutputStream os=client.getOutputStream(); //���
			pw=new PrintWriter(new OutputStreamWriter(os));
			
			//�޼��� ����
			String msg=txt_name.getText(); //��ȭ�� ��������
			pw.println(msg);
			pw.flush(); //������ ����
			txt_input.requestFocus(); //Ŀ�� �̵�
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
