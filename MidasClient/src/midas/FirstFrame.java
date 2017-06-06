package midas;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
		startButton.setBounds(240,300,100,40);
		this.setLayout(null);
		this.add(startButton);
		this.setSize(600,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ImageIcon mainIcon = new ImageIcon(getClass().getClassLoader().getResource("main.png"));
		JLabel label = new JLabel(mainIcon);
		label.setBounds(80, 30, 400, 250);
		this.add(label);
		this.setVisible(true);
		
		
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


