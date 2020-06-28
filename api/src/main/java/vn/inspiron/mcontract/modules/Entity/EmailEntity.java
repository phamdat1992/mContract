package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "email")
@Data
public class EmailEntity
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private Long fkUser;
}