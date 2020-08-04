package vn.inspiron.mcontract.modules.Entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "dvhc_districts")
@Data
public class DvhcDistrictEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String prefix;
    @Column(name = "fk_dvhc_city")
    private Long fkDvhcCity;

}
