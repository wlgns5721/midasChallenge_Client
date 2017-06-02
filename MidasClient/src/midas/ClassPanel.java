package midas;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
/**
 * 클래스 하나를 그리기 위한 패널
 * @author dnjsd
 *
 */
public class ClassPanel extends JPanel implements MouseListener{
	private JTextField className;
	private JTable attributes;
	private JTable operations;
	private Location location;
	private ClassObject classObjData;
	private EditPanel BelongToEditPanel;
	private int index;
	
	public ClassPanel(ClassObject classObj, EditPanel editPanel, int index) {
		this.classObjData = classObj;
		this.BelongToEditPanel = editPanel;
		this.index = index;
		initUI();
	}
	
	public void initUI() {
		int hieddenSpace = 0;
		if(classObjData.getClassType().equals("Interface")) {
			JTextField hiddenTitle = new JTextField("<<interface>>");
			hiddenTitle.setLocation(0, 0);
			hiddenTitle.setFont(new Font("돋움", Font.PLAIN, 15));
			hieddenSpace += 15;
			hiddenTitle.setSize(classObjData.getWidth()*12, 15);
			hiddenTitle.setBackground(Color.WHITE);
			hiddenTitle.setBorder(new LineBorder(Color.black));
			add(hiddenTitle);
		}else if(classObjData.getClassType().equals("Abstract")) {
			JTextField hiddenTitle = new JTextField("<<Abstract>>");
			hiddenTitle.setLocation(0, 0);
			hiddenTitle.setFont(new Font("돋움", Font.PLAIN, 15));
			hieddenSpace += 15;
			hiddenTitle.setSize(classObjData.getWidth()*12, 15);
			hiddenTitle.setBackground(Color.WHITE);
			hiddenTitle.setBorder(new LineBorder(Color.black));
			add(hiddenTitle);
		}
		className = new JTextField(classObjData.getClassName());
		location = classObjData.getClassLocation();
		className.setLocation(0, hieddenSpace);
		className.setFont(new Font("돋움", Font.PLAIN, 15));
		className.setSize(classObjData.getWidth()*12, 15);
		className.setBackground(Color.WHITE);
		className.setBorder(new LineBorder(Color.black));
		className.addMouseListener(this);
		className.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					classObjData.setClassName(className.getText());
				}
			}
		});
		className.addMouseListener(this);
		add(className);
		attributes = new JTable(convertData(classObjData.getAttributes()),new Object[]{"Attributes"});
		attributes.setFont(new Font("돋움", Font.PLAIN, 15));
		attributes.setTableHeader(null);
		attributes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		attributes.setLocation(0, hieddenSpace+15);
		attributes.getColumnModel().getColumn(0).setWidth(classObjData.getWidth()*15);
		attributes.setSize(classObjData.getWidth()*12, classObjData.getAttributes().size()*15);
		attributes.setBackground(Color.WHITE);
		attributes.setBorder(new LineBorder(Color.black));
		ListSelectionModel attributeSelectionModel = attributes.getSelectionModel();
		attributeSelectionModel.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				operations.clearSelection();
			}
		});
		attributes.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					int row = attributes.getSelectedRow();
					int column = attributes.getSelectedColumn();
					String inputData = (String) attributes.getValueAt(row, column);
					if(TypeChecker.arrtibuteTypeCheck(inputData)) {
						ArrayList<String> beforeData = classObjData.getAttributes();
						beforeData.set(row, inputData);
						classObjData.setAttributes(beforeData);
						validate();
						repaint();
					}else {
						JOptionPane.showMessageDialog(null,
								TypeChecker.getErrorCode(),
							    "Warnning",
							    JOptionPane.WARNING_MESSAGE);
					}
				}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
					ArrayList<String> beforeData = classObjData.getAttributes();
					beforeData.add("new attr"+ (beforeData.size()+1));
					classObjData.setAttributes(beforeData);
					
					validate();
					repaint();
					BelongToEditPanel.changeClassInfo(classObjData, index);
				}
			}
		});
		attributes.addMouseListener(this);
		add(attributes);
		operations = new JTable(convertData(classObjData.getOperations()),new Object[]{"Operations"});
		operations.setFont(new Font("돋움", Font.PLAIN, 15));
		operations.setTableHeader(null);
		operations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		operations.getColumnModel().getColumn(0).setWidth(classObjData.getWidth()*15);
		operations.setLocation(0, hieddenSpace+classObjData.getAttributes().size()*15 + 15);
		operations.setSize(classObjData.getWidth()*12, classObjData.getHeight()*15);
		operations.setBackground(Color.WHITE);
		operations.setBorder(new LineBorder(Color.black));
		ListSelectionModel operationSelectionModel = operations.getSelectionModel();
		operationSelectionModel.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				attributes.clearSelection();
			}
		});
		operations.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					int row = operations.getSelectedRow();
					int column = operations.getSelectedColumn();
					String inputData =  (String) operations.getValueAt(row, column);
					if(TypeChecker.operationTypeCheck(inputData)){
						ArrayList<String> beforeData = classObjData.getOperations();
						beforeData.set(row, (String) operations.getValueAt(row, column));
						classObjData.setOperations(beforeData);
						validate();
						repaint();
					}else {
						JOptionPane.showMessageDialog(null,
								TypeChecker.getErrorCode(),
							    "Warnning",
							    JOptionPane.WARNING_MESSAGE);
					}
					
				}else if(e.getKeyCode() == KeyEvent.VK_TAB) {
					ArrayList<String> beforeData = classObjData.getOperations();
					beforeData.add("new op"+ (beforeData.size()+1));
					classObjData.setOperations(beforeData);
					validate();
					repaint();
					BelongToEditPanel.changeClassInfo(classObjData, index);
				}
			}
		});
		operations.addMouseListener(this);
		add(operations);
	}
	
	/**
	 * Attribute와 Operation을 그리는 JTable을 위해 데이터를 변환하는 메서드 
	 * @param data
	 * @return
	 */
	private Object[][] convertData(ArrayList<String> data) {
		Object[][] returnVal = new Object[data.size()][1];
		for(int i = 0; i < data.size(); i++) {
			returnVal[i][0] = data.get(i);
		}
		return returnVal;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		if(MainFrame.mode.equals("Erase")) {
			BelongToEditPanel.deleteClassInfo(index);
			MainFrame.mode = "Normal";
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
