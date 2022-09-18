package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "ward")
@Entity
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ward_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "province_id")
    private Integer provinceId;
    @Column(name = "district_id")
    private Integer districtId;
}
