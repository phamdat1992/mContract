package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "company_user_history")
@Data
public class CompanyUserHistoryEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long fkCompanyUser;
    @Column
    private Long fkCompanyUserRole;
}