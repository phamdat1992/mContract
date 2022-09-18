package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "province")
@Entity
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "province_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "provinceId")
    private List<Ward> wards;
    @OneToMany(mappedBy = "provinceId")
    private List<District> districts;
}
