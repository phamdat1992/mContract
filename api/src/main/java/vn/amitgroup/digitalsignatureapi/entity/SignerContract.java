package vn.amitgroup.digitalsignatureapi.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "signer_contract")
public class SignerContract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "signer_contract_id")
    private Integer id;
    @Column(name = "is_watched")
	private Boolean isWatched;
    @Column(name = "is_delected")
	private Boolean isDelected;
    @Column(name = "contract_role",nullable = false)
	@Enumerated(EnumType.STRING)
    private ContractRole contractRole;
    @Column(name = "contract_status")
	@Enumerated(EnumType.STRING)
	private ContractStatus contractStatus;
    @Column(name = "is_signed")
    private Boolean isSigned;
    @Column(name = "signed_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date signedTime;
    @Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
    @Column(name = "pdf_sign_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pdfSignTime;
	@Column(name = "signed_file",columnDefinition = "text")
	private String signedFile;
	@Column(name = "pem_cert",columnDefinition = "text")
	private String pemCert;
	@Column(name = "x")
	private Float x;
	@Column(name = "y")
    private Float y;
	@Column(name = "page")
    private  Float page;
    @ManyToOne
    @JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
    private Contract contract;
    @ManyToOne
    @JoinColumn(name = "signer_id",referencedColumnName = "signer_id")
    private Signer signer;
    @Column(name = "email_signer")
    private String emailSigner;


}
