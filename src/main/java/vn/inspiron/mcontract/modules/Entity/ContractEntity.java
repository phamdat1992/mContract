package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contract")
@Data
public class ContractEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String title;
    @Column(length = 10000)
    private String description;
    @Column
    private Long fkUser;
    @Column
    private Long fkContractStatus;
    @Column
    private Date expiryDateSigned;
    @Column
    private Long fkContractMessage;
    @Column
    private Long fkFile;
}