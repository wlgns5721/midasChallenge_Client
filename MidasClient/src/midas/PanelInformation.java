package midas;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 화면을 구성하는 패널의 정보를 저장하는데 사용하는 클래스
 * 
 * @author dnjsd
 *
 */
public class PanelInformation implements Serializable{
	private ArrayList<ClassObject> classList;
	private ArrayList<RelationshipArrow> arrowList;
	private ClassObject srcObject;
	private ClassObject dstObject;
	private String documentName = "";
	
	
	@Override
	public String toString() {
		return "PanelInformation [documentName=" + documentName + "]";
	}

	public PanelInformation(String name) {
		this.documentName = name;
		classList = new ArrayList<ClassObject>();
		arrowList= new ArrayList<RelationshipArrow>();
	}

	public void addClassObject(ClassObject obj){
		classList.add(obj);
	}
	
	public void addRelationshipArrow(RelationshipArrow arrow) {
		arrowList.add(arrow);
	}
	
	public ArrayList<ClassObject> getClassList() {
		return classList;
	}
	
	public ArrayList<RelationshipArrow> getRelationshipArrowList() {
		return arrowList;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	

	public void setSrcObject(ClassObject srcObject) {
		// TODO Auto-generated method stub
		this.srcObject = srcObject;
	}
	
	public void setDstObject(ClassObject dstObject) {
		// TODO Auto-generated method stub
		this.dstObject = dstObject;
	}
	
	public ClassObject getSrcObject() {
		// TODO Auto-generated method stub
		return this.srcObject;
	}
	
	public ClassObject getDstObject() {
		// TODO Auto-generated method stub
		return this.dstObject;
	}
	
	
}
