package vn.inspiron.mcontract.modules.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "files")
@Data
public class FileEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "token")
    private String token;
}
