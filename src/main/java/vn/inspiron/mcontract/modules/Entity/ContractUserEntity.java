package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contract_user")
@Data
public class ContractUserEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long fkContract;
    @Column
    private Long fkContractStatus;
    @Column
    private Long fkContractUserRole;
    @Column
    private Long fkEmail;
    @Column
    private Long fkMst;
    @Column
    private String name;
    @Column(length = 1000)
    private String description;
}
