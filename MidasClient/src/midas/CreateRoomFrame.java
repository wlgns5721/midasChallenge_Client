package midas;


import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class CreateRoomFrame extends JFrame{
	private JButton createButton;
	private JButton cancelButton;
	
	private JLabel roomNameLabel;
	private JLabel personLimitLabel;
	
	private JTextField roomNameField;
	private JComboBox personLimitComboBox;
	
	public CreateRoomFrame(JFrame waitRoomFrame) {
		createButton = new JButton("Create");
		cancelButton = new JButton("Cancel");
		createButton.setBounds(200,200,80,40);
		cancelButton.setBounds(290,200,80,40);
		
		roomNameLabel = new JLabel("방 제목");
		personLimitLabel = new JLabel("인원");
		roomNameLabel.setBounds(60, 50, 80, 40);
		roomNameLabel.setFont(new Font("Serif",Font.BOLD,20));
		personLimitLabel.setFont(new Font("Serif",Font.BOLD,20));
		personLimitLabel.setBounds(85, 100, 200, 40);
		
		roomNameField = new JTextField();
		roomNameField.setBounds(160, 50, 200, 40);
		
		String number[] = {"1","2","3","4","5","6","7","8"};
		personLimitComboBox = new JComboBox(number);
		
		personLimitComboBox.setBounds(160, 100, 80, 40);

		this.setLayout(null);
		this.add(roomNameLabel);
		this.add(personLimitLabel);
		this.add(createButton);
		this.add(cancelButton);
		this.add(roomNameField);
		this.add(personLimitComboBox);
		
		this.setSize(400,300);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		
		//createButton을 눌렀을 때의 액션		
		createButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				SendRoomInfoToServer();
				WaitRoomFrame.isWaitRoom = false;
				try {
					Thread.sleep(300);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				new MainFrame();    //클래스 다이어그램 제작 프로그램 실행
				waitRoomFrame.dispose();
				dispose();
			}
		});
	
		//cancelButton을 눌렀을 때의 액션
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		
		
		
		
	}
	
	/**
	 * 방을 생성하면 그 정보를 서버에게 전송한다.
	 */
	public void SendRoomInfoToServer() {
		String roomInfo = MakeRoomInfo();   //생성한 방의 정보를 String 형태로 만든다.
		
		//서버에 전송한다.
		BufferedWriter out;
		try {
			out = new BufferedWriter( new OutputStreamWriter(Client.secondClientSocket.getOutputStream()));
			out.write(roomInfo);
	        out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String MakeRoomInfo() {
		
		//전체 길이 + command + 방제목 + 인원제한
		StringBuilder sb = new StringBuilder("");
		String command = "makeroom";
		String roomName = roomNameField.getText();
		String personLimit = (String)personLimitComboBox.getItemAt(personLimitComboBox.getSelectedIndex());
		DecimalFormat df = new DecimalFormat("000");
		String entireLength = df.format(command.length() + roomName.getBytes().length + 1);
		
		sb.append(entireLength);
		sb.append(command);
		sb.append(roomName);
		sb.append(personLimit);
		
		return sb.toString();
		
	}
	
	public JButton GetCreateButton() {
		return createButton;
	}
	
	public JButton GetCancelButton() {
		return cancelButton;
	}
	
}
