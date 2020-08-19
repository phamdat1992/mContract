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

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column(name = "uploaded_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
}
