package vn.amitgroup.digitalsignatureapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "root_ca")
public class RootCa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "root_ca_id")
    private Integer id;
    @Column(name = "name",unique = true)
    private String name;
}
