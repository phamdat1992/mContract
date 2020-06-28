package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "company_user")
@Data
public class CompanyUserEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long fkCompany;
    @Column
    private Long fkUser;
    @Column
    private Long fkCompanyUserRole;
}