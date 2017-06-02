package midas;


class Room {
	private String roomName;
	private int limitPerson;
	private int currentPerson;
	private int roomID;
	public Room(String _roomName, int _roomID, int _currentPerson, int _limitPerson ) {
		roomName = _roomName;
		limitPerson = _limitPerson;
		currentPerson = _currentPerson;
		roomID = _roomID;
	}
	
	public void SetRoomName(String _roomName) {
		roomName = _roomName;
	}
	
	public String GetRoomName() {
		return roomName;
	}
	
	public void SetLimitPerson(int _limitPerson) {
		limitPerson = _limitPerson;
	}
	
	public int GetLimitPerson() {
		return limitPerson;
	}
	
	public void SetCurrentPerson(int _currentPerson) {
		currentPerson = _currentPerson;
	}
	
	public int GetCurrentPerson() {
		return currentPerson;
	}
	
	public void SetRoomID(int _roomID) {
		roomID = _roomID;
	}
	
	public int GetRoomID() {
		return roomID;
	}
	
}
