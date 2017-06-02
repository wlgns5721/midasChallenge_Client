package midas;
/**
 * 속성과 변수의 입력이 정확한지 검사하기 위한 검사기 클래스
 * @author dnjsd
 *
 */
public class TypeChecker {
	private static String errorCode = "";
	/**
	 * 입력된 Attribute을 검사하는 메서드
	 * @param attr 입력받은 Attribute String
	 * @return 정상적인 입력이면 true, 아니면 false
	 */
	public static boolean arrtibuteTypeCheck(String attr) {
		char ch[] = attr.toCharArray();
		if(ch[0] == '+' || ch[0] == '-' || ch[0] == '#') {
			if(attr.contains(":")) {
				String splitString[] = attr.split(":");
				if(splitString[1].matches(".*int") || splitString[1].matches(".*long") || splitString[1].matches(".*float")
						|| splitString[1].matches(".*double") || splitString[1].matches(".*byte") || splitString[1].matches(".*short")
						|| splitString[1].matches(".*boolean") || splitString[1].matches(".*ch")) {
					return true;
				}else {
					errorCode = "Primitive type Mismatch Error";
				}
			}else {
				errorCode = "No Primitive type Error";
			}
		}else {
			errorCode = "Access Modifier Error";
		}
		return false;
	}
	/**
	 * 입력받은 Operation을 검사하는 메서드
	 * @param op 입력받은 Operation 메서드
	 * @return 정상적인 입력이면 true, 아니면 false
	 */
	public static boolean operationTypeCheck(String op) {
		char ch[] = op.toCharArray();
		if(ch[0] == '+' || ch[0] == '-'|| ch[0] == '#') {
			if(op.contains(":")) {
				String splitString[] = op.split(":");
				if(splitString[1].matches(".*int") || splitString[1].matches(".*long") || splitString[1].matches(".*float")
						|| splitString[1].matches(".*double") || splitString[1].matches(".*byte") || splitString[1].matches(".*short")
						|| splitString[1].matches(".*boolean") || splitString[1].matches(".*ch")) {
					if(splitString[0].matches(".*(.*).*")) {
						return true;
					}else {
						errorCode = "Has No ()";
					}
				}else {
					errorCode = "Return type Mismatch Error";
				}
			}else {
				errorCode = "No Return type Error";
			}
		}else {
			errorCode = "Access Modifier Error";
		}
		return false;
	}
	/**
	 * 에러가 발생 하였을 경우, 어떤 종류의 에러인지 알기위해 에러 코드를 가져오는 메서드
	 * @return 발생한 에러의 종류를 나타내는 클래스
	 */
	public static String getErrorCode() {
		return errorCode;
	}
}
