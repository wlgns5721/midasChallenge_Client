package midas;
import java.io.Serializable;

/**
 * 
 * @author wonjerry 클래스 또는 화살표의 위치정보 및 좌표연산 관련 클래스 좌표 연산 관련 메소드가 추가 될 수 있다.
 *
 */
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	private int startX, startY, endX, endY;

	public Location(int startX, int startY, int endX, int endY) {
		setStartX(startX);
		setStartY(startY);
		setEndX(endX);
		setEndY(endY);
	}
	

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}
	
	

	// TODO can insert another calculate method

	public boolean calculateBondary(Location arrowStartLocation){
		if(this.startX < arrowStartLocation.startX && this.endX > arrowStartLocation.startX){
			if(this.startY < arrowStartLocation.getEndY() && this.endY > arrowStartLocation.getEndY())
				return true;
			else return false;
		}else return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endX;
		result = prime * result + endY;
		result = prime * result + startX;
		result = prime * result + startY;
		return result;
	}
	
	public boolean calculateUpDown(Location arrowStartLocation){
		if(this.startY >= arrowStartLocation.endY) return true;
		else return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (endX != other.endX)
			return false;
		if (endY != other.endY)
			return false;
		if (startX != other.startX)
			return false;
		if (startY != other.startY)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Location [startX=" + startX + ", startY=" + startY + ", endX=" + endX + ", endY=" + endY + "]";
	}

}
