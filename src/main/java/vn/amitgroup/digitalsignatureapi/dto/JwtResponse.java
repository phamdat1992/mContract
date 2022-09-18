package vn.amitgroup.digitalsignatureapi.dto;

import java.util.Date;

import lombok.Data;

@Data
public class JwtResponse {
	private String token;
	private String refreshToken;
	private String type = "Bearer";
	private String account;
	private String name;
	private String role;
	private Date expirationTime;

	public JwtResponse(String token,String refreshToken, String account, String name, String role,Date expirationTime) {
		this.account = account;
		this.name = name;
		this.token = token;
		this.refreshToken = refreshToken;
		this.role = role;
		this.expirationTime = expirationTime;
	}
}
