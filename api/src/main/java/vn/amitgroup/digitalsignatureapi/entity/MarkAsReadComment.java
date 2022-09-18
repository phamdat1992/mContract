package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "mark_as_read_comment")
public class MarkAsReadComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "comment_id")
    private Integer commentId;
    @Column(name = "email_receiver")
    private String email;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "contractId")
    private String contractId;
    @Column(name = "parent_id")
    private Integer parentId;
}
