package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "user_otp")
@Data
public class UserOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_otp_id")
    private Integer id;
    @Column(name = "key")
    private String key;
    @Column(name = "expiration_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationTime;
    @Column(name = "code")
    private Integer code;
}
