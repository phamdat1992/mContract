package vn.amitgroup.digitalsignatureapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s3file")
public class S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "s3file_id")
    private Long id;
    @Column(name = "key_name")
    private String keyName;
    @Column(name = "bucket_name")
    private String bucketName;
    @Column(name = "url")
    private String url;
    @Column(name = "email")
    private String email;

    public S3File(){}

    public S3File(String bucketName, String keyName, String url, String email){
        this.keyName = keyName;
        this.bucketName = bucketName;
        this.url = url;
        this.email = email;
    }
}
