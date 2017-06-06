package midas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

/**
 * 화면을 구성하게될 메인 프레임, 상단목록과 좌측 사이드바, 편집화면을 가지게됨
 * 
 * @author dnjsd
 *
 */
public class MainFrame extends JFrame {
	private int width = 1280;
	private int height = 1024;

	private final JSplitPane splitPane;
	private final SideBarPanel sidebarPanel;
	public final static TappedPane editPanel = new TappedPane();
	public static String mode = "Class";
	private Client client;
	private boolean isRoomLeader;
	// private final sideBarPenal sidePanel;

	public MainFrame(boolean _isRoomLeader) {
		sidebarPanel = new SideBarPanel();
		splitPane = new JSplitPane();
		isRoomLeader = _isRoomLeader;
		initUI();
		
		
		
		client = new Client();
		ReceiveDataInRoomThread thread = new ReceiveDataInRoomThread();
		thread.start();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("closing");
				client.SendData(Client.secondClientSocket,"exitroom",3);
				thread.interrupt();
				
			}
		});
	}

	/**
	 * UI를 초기화 하는 메서드
	 */
	public void initUI() {
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(300);
		splitPane.setLeftComponent(sidebarPanel);
		sidebarPanel.setBackground(Color.WHITE);
		splitPane.setRightComponent(editPanel);
		splitPane.setEnabled(false);
		setSize(width, height);
		createMenuBar();

		getContentPane().setLayout(new GridLayout());
		getContentPane().add(splitPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setTitle("MIDAS_UML");
		
	}
	
	
	

	/**
	 * 메뉴바를 만드는 메서드
	 */
	public void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		ImageIcon fileIcon = new ImageIcon(getClass().getClassLoader().getResource("fileIcon.png"));
		file.setMnemonic(KeyEvent.VK_F);
		file.setIcon(fileIcon);
		JMenu erase = new JMenu("Erase");
		ImageIcon eraseIcon = new ImageIcon(getClass().getClassLoader().getResource("eraseIcon.png"));
		erase.setMnemonic(KeyEvent.VK_E);
		erase.setIcon(eraseIcon);
		erase.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			} 
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				mode = "Erase";
				validate();
				repaint();
			}
		});
		JMenu saveImage = new JMenu("Image");
		ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("saveIcon.png"));
		saveImage.setMnemonic(KeyEvent.VK_I);
		saveImage.setIcon(saveIcon);
		saveImage.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		JMenuItem newFile = new JMenuItem("New");
		newFile.setEnabled(isRoomLeader);
		newFile.setToolTipText("새로 파일을 만듭니다");
		newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String input = JOptionPane.showInputDialog("Enter File Name:");
				client.SendData(Client.secondClientSocket, "createfile"+input, 3);
			}
		});
		JMenuItem openFile = new JMenuItem("Open File");
		openFile.setToolTipText("파일을 불러옵니다");
		openFile.setEnabled(isRoomLeader);
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FileChooser chooser = new FileChooser(MainFrame.this);
				PanelInformation getInfo = chooser.openFile();
				
				if (getInfo != null) {
					try {
						client.SendData(Client.secondClientSocket, "loadfile", 3);
						ObjectOutputStream oos = new ObjectOutputStream(Client.secondClientSocket.getOutputStream());
						oos.writeObject(getInfo);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				
			}
		});
		JMenuItem saveFile = new JMenuItem("Save");
		saveFile.setToolTipText("파일을 저장합니다"); // 그냥 저장하는거라서 filechooser를 바꿀까 고민
		saveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog.setDefaultLookAndFeelDecorated(true);
				int response = JOptionPane.showConfirmDialog(null, "Do you want to Save?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					System.out.println("No button clicked");
				} else if (response == JOptionPane.YES_OPTION) {
					System.out.println("Yes button clicked");
					PanelInformation saveInfo = editPanel.getNowSelectedInfo();
					try {
						FileOutputStream fos = new FileOutputStream(saveInfo.getDocumentName() + ".dat");
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						oos.writeObject((Object) saveInfo);
						oos.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (response == JOptionPane.CLOSED_OPTION) {
					System.out.println("JOptionPane closed");
				}
			}
		});
		
		JMenuItem saveAsFile = new JMenuItem("Save As...");
		saveAsFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				PanelInformation saveInfo = editPanel.getNowSelectedInfo();
				FileChooser chooser = new FileChooser(MainFrame.this);
				chooser.saveFile(saveInfo);
			}
		});
		saveAsFile.setToolTipText("파일을 다른 이름으로 저장합니다");
		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveAsFile);
		menuBar.add(file);
		menuBar.add(erase);
		menuBar.add(saveImage);
		setJMenuBar(menuBar);
	}
	
	
	public void ReceiveDataInRoom() {
		while(true) {
			try {
				Thread.sleep(10);
				String receivedData = client.ReceiveData(Client.secondClientSocket,3);
			
				if(receivedData.contains("createfile")) {
					String fileName = receivedData.substring(10);
					PanelInformation getInfo = new PanelInformation(fileName);
					editPanel.addInfo(getInfo);// 추가된 다큐먼트 반영 및 탭 다시그리
					sidebarPanel.refreshTree(getInfo);//트리 재구성 
					MainFrame.this.repaint();
					
				}
				
				else if(receivedData.contains("loaddata")) {
					//파일이 열려있으면 전달받은 파일 이름으로 문서를 새로 연다.
					if(Integer.parseInt(receivedData.substring(8, 9))==1) {
						String fileName = receivedData.substring(15);
						PanelInformation getInfo = new PanelInformation(fileName);
						editPanel.addInfo(getInfo);// 추가된 다큐먼트 반영 및 탭 다시그리
						sidebarPanel.refreshTree(getInfo);//트리 재구성 
					}
					else {
						PanelInformation getInfo = new PanelInformation("");
						editPanel.addInfo(getInfo);// 추가된 다큐먼트 반영 및 탭 다시그리
						sidebarPanel.refreshTree(getInfo);//트리 재구성 
					}
					
					int class_num = Integer.parseInt(receivedData.substring(9,12));
					int arrow_num = Integer.parseInt(receivedData.substring(12,15));
					
					try {
						ObjectInputStream ois = new ObjectInputStream(Client.secondClientSocket.getInputStream());
						for (int i=0; i<class_num; i++) {
							ClassObject classObject = (ClassObject)ois.readObject();
							editPanel.GetEditPanel().GetPanelInformation().addClassObject(classObject);
						}
						for(int i=0; i<arrow_num; i++) 
							editPanel.GetEditPanel().GetPanelInformation().addRelationshipArrow((RelationshipArrow)ois.readObject());
						
						repaint();

					}
					
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				
				}
				
				else if(receivedData.equals("ClassObject")) {
					try {
						ObjectInputStream ois = new ObjectInputStream(Client.secondClientSocket.getInputStream());
						ClassObject classObject = (ClassObject)ois.readObject();
						editPanel.GetEditPanel().GetPanelInformation().addClassObject(classObject);
						repaint();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(receivedData.equals("ArrowObject")) {
					try {
						ObjectInputStream ois = new ObjectInputStream(Client.secondClientSocket.getInputStream());
						RelationshipArrow arrowObject = (RelationshipArrow)ois.readObject();
						editPanel.GetEditPanel().GetPanelInformation().addRelationshipArrow(arrowObject);
						repaint();
						System.out.println("receive arrow information");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			catch (InterruptedException e) {
				System.out.println("mainFrame socket closed");
				break;
			}
			
		}
	
	}
	
	class ReceiveDataInRoomThread extends Thread {
		public void run() {
			ReceiveDataInRoom();
			
		}
	}

}
