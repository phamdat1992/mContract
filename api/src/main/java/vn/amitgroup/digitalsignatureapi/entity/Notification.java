package vn.amitgroup.digitalsignatureapi.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "notification")
public class Notification {
	@Id
	@Column(name = "notification_id")
	private UUID id;
	private Integer commentId;
	private String contractId;
	@Column(name = "content_comment",length = 512)
	private String commentContent;
	private Integer parentId;
	private Float commentX;
	private Float commentY;
	private Float commentPage;
	private String contractName;
	@Column(name = "content_contract",length = 512)
	private String contractContent;
	private Integer userId;
	private String userName;
	private String userEmail;
	private String userAvatar;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	private String type;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiredTime;
	private Integer commentParentId;
	private String signerFullName;
	private String signerEmail;
	private Integer signerId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentCreatedTime;
	private Boolean status;
}
