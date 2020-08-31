package vn.inspiron.mcontract.modules.Common.data.type;

/**
 * @author hoangdd
 * @created 24/08/2020 - 11:28 PM
 **/
public enum ResponseStatusType {
	SUCCESS("Success"),
	FAILED("Failed");
	
	private final String value;
	
	ResponseStatusType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static ResponseStatusType lookUp(String name) throws IllegalArgumentException {
		for (ResponseStatusType val : values()) {
			if (val.name().equalsIgnoreCase(name))
				return val;
		}
		return null;
	}
}
