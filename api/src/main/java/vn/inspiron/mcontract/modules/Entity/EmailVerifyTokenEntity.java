package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "email_verify_token")
@Data
public class EmailVerifyTokenEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "fk_user")
    private UserEntity user;
}
