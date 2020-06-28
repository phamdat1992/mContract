package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "company")
@Data
public class CompanyEntity
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
    @Column(length = 500)
    private String address;
}