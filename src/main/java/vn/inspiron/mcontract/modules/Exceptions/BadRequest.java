package vn.inspiron.mcontract.modules.Exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequest extends RuntimeException {
	public BadRequest(String message) {
		super(message);
	}
}
