package vn.inspiron.mcontract.modules.Common.data.type;

/**
 * @author hoangdd
 * @created 25/08/2020 - 7:43 AM
 **/
public enum ContractStatusEnum {
	WAITING_FOR_APPROVAL("waiting_for_approval"),
	WAITING_FOR_SIGNATURE("waiting_for_signature"),
	SIGNED("signed"),
	APPROVED("approved"),
	CANCELLED("cancelled"),
	DRAFT("draft"),
	INVALID_CERT("invalid_cert"),
	INVALID_ALGORITHM("invalid_algorithm"),
	INVALID_SIGNATURE("invalid_signature"),
	EXPIRED_CERTIFICATE("expired_certificate"),
	REVOKED_CERTIFICATE("revoked_certificate"),
	MISMATCH_TAX_CODE("mismatch_tax_code");
	
	private final String value;
	
	ContractStatusEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static ContractStatusEnum lookUp(String name) throws IllegalArgumentException {
		for (ContractStatusEnum val : values()) {
			if (val.name().equalsIgnoreCase(name))
				return val;
		}
		return null;
	}
}
