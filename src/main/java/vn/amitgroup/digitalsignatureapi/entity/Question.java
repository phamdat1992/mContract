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
@Entity
@Table(name = "question")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "question_id")
	private Integer id;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "email")
	private String email;
	@Column(name = "company_name")
	private String companyName;
	@Column(name = "content")
	private String content;
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@Column(name = "status")
	private Boolean status;
}
