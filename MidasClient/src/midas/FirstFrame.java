package midas;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FirstFrame extends JFrame{
	private JButton startButton;
	

	public FirstFrame() {
		super("Hello 9 Team");
		MainScreen();
	}
	
	/**
	 * 첫 실행 시 보여지는 화면
	 */
	public void MainScreen() {
		startButton = new JButton("Start");
		startButton.setBounds(250,300,100,40);
		this.setLayout(null);
		this.add(startButton);
		this.setSize(600,400);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//URL url = getClass().getClassLoader().getResource("extendsarrow.png");
		//System.out.println(url.toString());
		
		
		//시작 버튼 리스너
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client client = new Client();
				client.ConnectWithServer();
				new WaitRoomFrame();
				dispose();
			}
		});
		
	}
	
	
	
	
	
	
	
}


