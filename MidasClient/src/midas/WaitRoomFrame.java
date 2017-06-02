package midas;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class WaitRoomFrame extends JFrame {
	private Vector<Room> roomVector;
	private Vector<String> tableColumn;
	private Vector<String> tableRow;
	private JFrame jFrame;
	private JPanel jPanel;
	private JScrollPane jScrollPane;
	private Container container;
	private JTable jTable;
	private DefaultTableModel tableModel;
	public static boolean isWaitRoom = true;
	public WaitRoomFrame() {
		roomVector = new Vector<Room>();
		tableColumn = new Vector<String>();
		tableRow = new Vector<String>();
		
		jFrame = new JFrame("Room List");
		container = jFrame.getContentPane();
		jFrame.setLayout(new FlowLayout());
		
		jFrame.setSize(500, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		jPanel = new JPanel();   //패널 생성
		jPanel.setLayout(new BorderLayout());
		jFrame.add(jPanel,BorderLayout.CENTER);
		JButton createRoomButton = new JButton("Create Room");
		jFrame.add(createRoomButton);
		
		tableColumn.add("방 번호");
		tableColumn.add("방 제목");
		tableColumn.add("인원");
		tableModel = new DefaultTableModel(tableColumn,0) {  //테이블 내용 수정 불가
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		jTable = new JTable(tableModel); //Table 생성
		jTable.getTableHeader().setReorderingAllowed(false); //reordering 불가
		jTable.getTableHeader().setResizingAllowed(false); //resizing 불가
		
		jTable.setRowHeight(30);
		jTable.setFont(new Font("Serif",Font.BOLD,20));
		
		
		jScrollPane = new JScrollPane(jTable);   //스크롤 설정
		jPanel.add(jScrollPane,BorderLayout.CENTER);
		
		setResizable(false);
		jFrame.setVisible(true);
		
		createRoomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateRoom();
			}
		});
		
		jTable.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable)me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if(me.getClickCount()==2) {
					EnterRoom(roomVector.elementAt(row).GetRoomID());
				}
			}
		});
		
		RoomHandlingThread thread = new RoomHandlingThread();
		thread.start();
		System.out.println("run");
	}
	
	/**
	 * 서버로부터 방에 대한 정보를 받은 후 파싱을 하는 작업
	 */
	public void ReceiveRoomInfo() {
		byte arrlen[] = new byte[3];
        InputStream in;
        String roomName;
        int roomID;
        int limitPerson;
        int currentPerson;
        int infoLength;
        
		try {
			//room information의 전체 길이를 받는다. 현재로썬 사용하지 않음
			in = Client.secondClientSocket.getInputStream();
			in.read(arrlen);
			
			roomVector.clear();
			infoLength = Integer.parseInt(new String(arrlen));
			
			arrlen = new byte[5];
			in.read(arrlen);
	        int numberOfRoom = Integer.parseInt(new String(arrlen));
	        
	        //받은 데이터를 파싱한다.
	        for(int i=0; i<numberOfRoom; i++) {
	        	byte textBuffer[];
	        	
	        	//방 이름 길이를 얻는다.
	        	textBuffer = new byte[3];
		        in.read(textBuffer);
		        int roomNameLength = Integer.parseInt(new String(textBuffer));
		        
		        //방 id 길이를 얻는다.
		        textBuffer = new byte[5];
		        in.read(textBuffer);
		        int roomIDLength = Integer.parseInt(new String(textBuffer));
				
		        //방 이름을 얻는다
		        textBuffer = new byte[roomNameLength];
		        in.read(textBuffer);
		        roomName = new String(textBuffer);
		        
		        //방 id를 얻는다.
		        textBuffer = new byte[roomIDLength];
		        in.read(textBuffer);
		        roomID = Integer.parseInt(new String(textBuffer));
		        
		        //해당 방의 현재 인원 및 제한인원을  얻는다
		        textBuffer = new byte[2];
		        in.read(textBuffer);
		        int temp = Integer.parseInt(new String(textBuffer));
		        currentPerson = temp/10;
		        limitPerson = temp%10;
		        
		        Room room = new Room(roomName, roomID, currentPerson, limitPerson);
		        roomVector.add(room);
	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		
	}
	
	
	/**
	 * 방 리스트를 리셋한다.
	 */
	public void ResetRoomList() {
		if (tableModel.getRowCount() > 0) {   //table의 모든 row를 없앤다.
		    for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
		        tableModel.removeRow(i);
		    }
		}
		
		for(int i=0; i<roomVector.size(); i++) {   //벡터에 있는 만큼 방을 보여준다.
			tableRow = new Vector<String>();
			Room room = roomVector.elementAt(i);
			String personInfo = room.GetCurrentPerson()+"/"+room.GetLimitPerson(); 
	
			tableRow.addElement(Integer.toString(i+1));   //방 번호
			tableRow.addElement(room.GetRoomName());   //방 이름
			tableRow.addElement(personInfo);   //인원 정보
			tableModel.addRow(tableRow);
			
		}
		
	}
	
	/**
	 * 방을 생성하는 함수
	 * 
	 */
	public void CreateRoom() {
		new CreateRoomFrame(this.jFrame);
	}
	/**
	 * 방에 입장하는 함수
	 */
	public void EnterRoom(int roomID) {
		String command = "enterroom" + Integer.toString(roomID);
		String totalLength = Integer.toString(command.length());
		
		Client client = new Client();
		client.SendData(Client.secondClientSocket, totalLength+command, 3);
		String result = client.ReceiveData(Client.secondClientSocket, 3);
		if(result.equals("no"))  //서버로부터 돌아온 답변이 ok인지 no인지 확인
			JOptionPane.showMessageDialog(null, "인원이 꽉 차서 들어갈 수 없습니다.");
		
		else if(result.equals("ok")){
			//여기에 클래스 다이어그램 제작 프로그램 실행부분 넣는다.
			new MainFrame();
			isWaitRoom = false;
			jFrame.dispose();
		}
		
				
	}
	
	

	class RoomHandlingThread extends Thread {
		Client client = new Client();
		public void run() {
			while(true) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(isWaitRoom) {
					client.SendData(Client.secondClientSocket,"requestRoomInfo",3);
					ReceiveRoomInfo();
					ResetRoomList();
					continue;
				}
				break;
			}
			System.out.println("waitroom thread closed");			
			
		}
	}
	
	
}	

