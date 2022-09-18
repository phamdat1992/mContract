package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "contract")
public class Contract {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "contract_id", updatable = false, nullable = false)
	private String id;
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	@Column(name = "expiration_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationTime;
	@Column(name = "title")
	private String title;
	@Column(name = "normal_title")
	private String normalTitle;
	@Column(name = "content", length = 512)
	private String content;
	@Column(name = "normal_content", length = 512)
	private String normalContent;
	@Column(name = "file_id")
	private Long fileId;
	@Column(name = "sign_file_id")
	private Long signFileId;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "is_sign")
	private Boolean isSign;
	@Column(name = "is_valid_taxcode")
	private Boolean isValidTaxcode;
	@OneToMany(mappedBy = "contract")
	private List<Comment> comments;
	@OneToMany(mappedBy = "tag")
	private List<ContractTag> contractTags;
	@OneToMany(mappedBy = "contract")
	private List<SignerContract> signerContracts;
}
