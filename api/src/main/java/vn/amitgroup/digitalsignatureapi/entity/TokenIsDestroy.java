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
@Data
@Table(name = "token_destroy")
@Entity()
public class TokenIsDestroy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "token")
    private String token;
    @Column(name = "expiration_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationTime;
}
