package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "certificate_token")
@Data
public class CertificateTokenEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String privateKey;
    @Column(length = 1000)
    private String public_key;
    @Column
    private Long fkMst;
    @Column
    private Long fkEmail;
    @Column
    private Boolean active;
}
