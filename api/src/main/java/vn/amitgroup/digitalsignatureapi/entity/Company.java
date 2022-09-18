package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
@Data
@Table(name = "company")
@Entity
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "company_id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "found_date")
	private String foundDate;
	@Column(name = "logo_path")
	private String logoPath;
	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "tax_code")
	private String taxCode;
	@Column(name = "provice_code")
	private String proviceCode;
	@Column(name = "district_code")
	private String districtCode;
	@Column(name = "ward_code")
	private String wardCode;
	@Column(name = "address")
	private String address;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name ="user_id",referencedColumnName ="user_id")
	private User user;
}
