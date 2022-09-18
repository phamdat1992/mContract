package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "district")
@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "district_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "province_id")
    private Integer provinceId;
    @OneToMany(mappedBy = "districtId")
    private List<Ward> wards;
}
