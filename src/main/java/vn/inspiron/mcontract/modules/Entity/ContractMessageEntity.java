package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contract_message")
@Data
public class ContractMessageEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long fkContract;
    @Column
    private Long fkMail;
    @Column
    private String message;
}
