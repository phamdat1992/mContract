package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "contract_user_role")
@Data
public class ContractUserRoleEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
}