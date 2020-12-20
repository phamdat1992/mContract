package vn.inspiron.mcontract.modules.Common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hoangdd
 * @created 24/08/2020 - 11:26 PM
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MContractResponseBody<T> implements Serializable {
	private static final long serialVersionUID = 7536642547653822532L;
	
	private Integer status;
	private String msg;
	private T data;
	private Long totalCount;
}

