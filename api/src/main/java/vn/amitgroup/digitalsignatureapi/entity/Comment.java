package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "comment_id")
	private Integer id;
	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	@Column(name = "content",length = 512)
	private String content;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id",referencedColumnName = "contract_id")
	private Contract contract;
	@ManyToOne()
	@JoinColumn(name = "signer_id",referencedColumnName = "signer_id")
	private Signer signer;
	@ManyToOne()
	@JoinColumn(name = "user_id",referencedColumnName = "user_id")
	private User user;
	@Column(name = "parent_id")
	private Integer parentId;
	@Column(name = "x")
	private Float x;
	@Column(name = "y")
	private Float y;
	@Column(name = "page")
	private  Float page;
}
