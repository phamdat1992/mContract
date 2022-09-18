package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "mark_as_read")
public class MarkAsRead {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "notification_id")
    private UUID notificationId;
    @Column(name = "signer_id")
    private Integer signerId;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "contract_id")
    private String contractId;
    @Column(name = "comment_id")
    private Integer commentId;
    @Column(name = "type")
    private String type;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "email_receiver")
    private String email;
    @Column(name = "avatar")
    private String avatarPath;

}
