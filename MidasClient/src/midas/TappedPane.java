package midas;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
/**
 * 화면 우측 탭들을 담는 Pane
 * @author dnjsd
 *
 */
public class TappedPane extends JTabbedPane {
	private ArrayList<PanelInformation> infos = new ArrayList<PanelInformation>();
	private EditPanel jp;
	public TappedPane() {
		initUI();
	}
	
	public void initUI() {
		setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
			@Override
			protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
				return 40;
			}
		});
	}
	/**
	 * 하나의 탭을 구성하는 PanelInformation을 추가하는 메서드
	 * @param getInfo 탭을 구성하는 PanelInformation
	 */
	public void addInfo(PanelInformation getInfo){
		this.infos.add(getInfo);
		jp = new EditPanel(getInfo);
		addTab(getInfo.getDocumentName(), jp);
		setTabComponentAt(infos.size()-1, new CustomTab(this));
	}
	/**
	 * 탭들을 구성하는 PanelInformation list를 반환하는 메서드
	 * @return 탭들을 구성하는 PanelInformation list
	 */
	public ArrayList<PanelInformation> getInfos() {
		return infos;
	}
	/**
	 * 탭을 한번에 여러개 띄우기 위해 Panelinformationc list를 설정해주는 메서드
	 * @param infos Panelinformationc list
	 */
	public void setInfos(ArrayList<PanelInformation> infos) {
		this.infos = infos;
	}
	/**
	 * 현재 선택된 패널의 인덱스를 반환해주는 메서드
	 * @return 현재 선택된 패널의 인덱스
	 */
	public int getNowSelectedIndex(){
		return this.getSelectedIndex();
	}
	/**
	 * 테스트용, 현재 선택된 패널의 정보출력
	 * @return 현재 선택된 패널의 정보
	 */
	public PanelInformation getNowSelectedInfo(){
		System.out.println("in getNowSelected : " + infos.get(this.getSelectedIndex()).getDocumentName());
		return infos.get(this.getSelectedIndex());
	}
	
	public EditPanel GetEditPanel() {
		return jp;
	}
}
