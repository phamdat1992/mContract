package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
@Data
@Table(name="users")
@Entity
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer id;
	@Column(name = "email",unique = true)
	private String email;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@Column(name = "birth_date")
	private String birthDate;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "sex")
	private String sex;
	@Column(name = "id_no")
	private String idNo;
	@Column(name = "issued_on")
	private String issuedOn;
	@Column(name = "avatar_path")
	private String avatarPath;
	@Column(name = "provice_code")
	private String proviceCode;
	@Column(name = "district_code")
	private String districtCode;
	@Column(name = "ward_code")
	private String wardCode;
	@Column(name = "address")
	private String address;
	@Column(name = "role",nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column(name = "accecpt_email_notification")
	private Boolean acceptEmailNotification;
	@OneToOne(mappedBy = "user")
	private Company company;
	@OneToMany(mappedBy = "user")
	private List<RefreshToken> refreshTokens;
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
	private List<Tag> tags;
	@OneToMany(mappedBy = "user")
	private List<Comment> comments;
	@OneToMany(mappedBy = "user")
	private List<Signer> signers;
}
