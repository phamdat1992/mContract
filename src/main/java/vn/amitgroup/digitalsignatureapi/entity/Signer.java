package vn.amitgroup.digitalsignatureapi.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "signer")
public class Signer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "signer_id")
	private Integer id;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "normal_name")
	private String normalName;
	@Column(name = "email")
	private String email;
	@Column(name = "tax_code")
	private String taxCode;
	@Column(name = "company_name")
	private String companyName;
	@OneToMany(mappedBy = "signer")
	private List<Comment> comments;
	@ManyToOne()
	@JoinColumn(name = "user_id",referencedColumnName = "user_id")
	private User user;
	@OneToMany(mappedBy = "signer")
	private List<SignerContract> signerContracts;
}
