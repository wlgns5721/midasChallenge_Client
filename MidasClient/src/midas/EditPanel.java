package midas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * 실제 편집화면 탭 하나를 구성하게될 패널클래스
 * 
 * @author dnjsd
 *
 */
public class EditPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private PanelInformation info;
	private Location arrorwlocation;
	private ReceiveDataInRoomThread thread;
	public EditPanel(PanelInformation info) {
		this.info = info;
		this.addMouseListener(this);
		initUI();
		thread = new ReceiveDataInRoomThread();
	}

	public void initUI() {
		setBackground(Color.white);
	}

	/**
	 * 배경의 격자무늬를 생성하기 위해 PaintComponent 사용
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);
		removeAll();
		g.setColor(Color.GRAY);
		for (int x = 0; x < 980; x++) {
			g.drawLine(x, 0, x, 944);
			x += 24;
		}
		for (int y = 0; y < 944; y++) {
			g.drawLine(0, y, 980, y);
			y += 24;
		}


		for(int i = 0; i < info.getClassList().size(); i++){

			drawClass(info.getClassList().get(i), i);

		}

		for (RelationshipArrow arrow : info.getRelationshipArrowList()) {
			Location location = arrow.getArrowLocation();
			if (arrow.getRule().equals("Extends")) {

				drawArrow(g2, location.getStartX(), location.getStartY(), location.getEndX(), location.getEndY(),1);
			} else
				drawArrow(g2, location.getStartX(), location.getStartY(), location.getEndX(), location.getEndY(),0);
		}

	}

	private void drawArrow(Graphics2D g, int x, int y, int xx, int yy,int mode) {
		float arrowWidth = 10.0f;
		float theta = 0.423f;
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		float[] vecLine = new float[2];
		float[] vecLeft = new float[2];
		float fLength;
		float th;
		float ta;
		float baseX, baseY;

		xPoints[0] = xx;
		yPoints[0] = yy;

		// build the line vector
		vecLine[0] = (float) xPoints[0] - x;
		vecLine[1] = (float) yPoints[0] - y;

		// build the arrow base vector - normal to the line
		vecLeft[0] = -vecLine[1];
		vecLeft[1] = vecLine[0];

		// setup length parameters
		fLength = (float) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1]);
		th = arrowWidth / (2.0f * fLength);
		ta = arrowWidth / (2.0f * ((float) Math.tan(theta) / 2.0f) * fLength);

		// find the base of the arrow
		baseX = ((float) xPoints[0] - ta * vecLine[0]);
		baseY = ((float) yPoints[0] - ta * vecLine[1]);

		// build the points on the sides of the arrow
		xPoints[1] = (int) (baseX + th * vecLeft[0]);
		yPoints[1] = (int) (baseY + th * vecLeft[1]);
		xPoints[2] = (int) (baseX - th * vecLeft[0]);
		yPoints[2] = (int) (baseY - th * vecLeft[1]);
		
		if(mode == 1){
			float[] dashingPattern3 = {10f, 10f, 1f, 10f};
			Stroke stroke3 = new BasicStroke(4f, BasicStroke.CAP_SQUARE,
			        BasicStroke.JOIN_MITER, 1.0f, dashingPattern3, 0.0f);
			 
			g.setStroke(stroke3);
		}
		g.drawLine(x, y, (int) baseX, (int) baseY);
		g.fillPolygon(xPoints, yPoints, 3);
	}

	private void drawClass(ClassObject classObj, int index) {

		ClassPanel classPanel = new ClassPanel(classObj, this , index);
		Location location = classObj.getClassLocation();
		if(classObj.getClassType().equals("Class")){
			classPanel.setSize(classObj.getWidth()*12, classObj.getHeight()*15 +2);
		}else {
			classPanel.setSize(classObj.getWidth()*12, classObj.getHeight()*15 +2 + 15);
		}

		classPanel.setLocation(location.getStartX(), location.getStartY());
		location.setEndX(location.getStartX() + classObj.getWidth() * 12);
		location.setEndY(location.getStartY() + classObj.getHeight() * 15 + 2);
		classPanel.setBackground(Color.white);
		classPanel.setBorder(new LineBorder(Color.black));
		add(classPanel);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if (MainFrame.mode.equals("Class") || MainFrame.mode.equals("Interface") || MainFrame.mode.equals("Abstract")) {
			ClassObject classObject = new ClassObject("New Class" + (info.getClassList().size() + 1),
					new Location(e.getX(), e.getY(), e.getX() + 100, e.getY() + 200), MainFrame.mode);
			try {
				Client client = new Client();
				client.SendData(Client.secondClientSocket, "makeclass", 3);
				ObjectOutputStream oos = new ObjectOutputStream(Client.secondClientSocket.getOutputStream());
				oos.writeObject(classObject);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
			
			
			MainFrame.mode = "Normal";
		}else{
			int x = e.getX();
			int y = e.getY();
			
			for(RelationshipArrow arrow : this.info.getRelationshipArrowList()){
				Location location = arrow.getArrowLocation();
				Double distTo = Math.sqrt(Math.pow(Math.abs(location.getEndX()-x), 2)+Math.pow(Math.abs(location.getEndY()-y), 2));
				Double distFrom = Math.sqrt(Math.pow(Math.abs(location.getStartX()-x), 2)+Math.pow(Math.abs(location.getStartY()-y), 2));
				if(distTo < 50 || distFrom < 50){
					int response = JOptionPane.showConfirmDialog(null, "Do you want to delete(yes to delete/no to modify)?", "Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.NO_OPTION) {
						PanelInformation info = MainFrame.editPanel.getNowSelectedInfo();
						int tabNum = MainFrame.editPanel.getNowSelectedIndex();
						arrow.getToObject().getArrowList().remove(arrow);
						arrow.getFromObject().getArrowList().remove(arrow);
						this.info.getRelationshipArrowList().remove(arrow);
						if(info.getClassList().size() > 1){
							DialgFrame frame;
							if(arrow.getRule().equals("Dependency")){
								frame = new DialgFrame(info,tabNum,0);
							}else{
								frame = new DialgFrame(info,tabNum,1);
							}
							
							frame.setVisible(true);
							break;
						}
					} else if (response == JOptionPane.YES_OPTION) {
						arrow.getToObject().getArrowList().remove(arrow);
						arrow.getFromObject().getArrowList().remove(arrow);
						this.info.getRelationshipArrowList().remove(arrow);
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		repaint();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub


	}

	public void changeClassInfo(ClassObject classObj, int index) {

		ClassObject obj = info.getClassList().get(index);
		
		for (RelationshipArrow ele : obj.getArrowList()) {
			if (ele.getToObject().equals(obj)) {
				System.out.println("들어옴???");
				Location temp = classObj.getClassLocation();
				Location location = new Location((temp.getStartX() + temp.getEndX()) / 2, temp.getEndY(),
						ele.getArrowLocation().getEndX(), ele.getArrowLocation().getEndY());
				RelationshipArrow arrow = new RelationshipArrow(classObj, ele.getFromObject(), location, ele.getRule());
				classObj.addArrow(arrow);
				info.getClassList().set(index, classObj);
				this.info.getRelationshipArrowList().remove(ele);
				this.info.getRelationshipArrowList().add(arrow);
				break;
			}
		}

		validate();
		repaint();

	}
	
	public void ReceiveClassFromServer(ClassObject classObject) {
		info.addClassObject(classObject);
		validate();
		repaint();
	}

	public void deleteClassInfo(int index) {
		info.getClassList().remove(index);
		validate();
		repaint();

	}
	
	public void StartThread() {
		thread.start();
	}
	
	public void ReceiveDataInRoom() {
		Client client = new Client();
		String objectType = client.ReceiveData(Client.secondClientSocket,3);
		if(objectType.equals("ClassObject")) {
			try {
				ObjectInputStream ois = new ObjectInputStream(Client.secondClientSocket.getInputStream());
				ClassObject classObject = (ClassObject)ois.readObject();
				info.addClassObject(classObject);
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
	}
	
	class ReceiveDataInRoomThread extends Thread {
		public void run() {
			while(true) {
				ReceiveDataInRoom();
			}
		}
	}
}