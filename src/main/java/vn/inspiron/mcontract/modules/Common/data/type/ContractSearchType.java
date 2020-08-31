package vn.inspiron.mcontract.modules.Common.data.type;

/**
 * @author hoangdd
 * @created 25/08/2020 - 7:19 AM
 **/
public enum ContractSearchType {
	ALL_CONTRACT("ALL_CONTRACT"),
	SEND("SEND"),
	DRAFT("DRAFT"),
	BOOKMARK_STAR("BOOKMARK_STAR"),
	CANCEL("CANCEL"),
	WAIT_APPROVE("WAIT_APPROVE"),
	EXPIRY("EXPIRY"),
	SIGNED("SIGNED"),
	INVALID_CERTIFICATE("INVALID_CERTIFICATE"),
	NEED_SIGN("NEED_SIGN");
	
	private final String value;
	
	ContractSearchType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static ContractSearchType lookUp(String name) throws IllegalArgumentException {
		for (ContractSearchType val : values()) {
			if (val.name().equalsIgnoreCase(name))
				return val;
		}
		return null;
	}
}
