package vn.inspiron.mcontract.modules.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
@Data
public class UserEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String fullname;
    @Column
    private String gender;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column
    private String cmnd;
    @Column(name = "cmnd_issue_date")
    private Date cmndIssueDate;
    @Column(name = "cmnd_issue_place")
    private String cmndIssuePlace;
    @Column
    private String phone;
    @Column
    private String street;
    @Column
    private String ward;
    @Column
    private String district;
    @Column
    private String city;
    @Column
    private String token; // This is not jwt accessToken. This is a token used to identify a user (instead of exposing user id in jwt)

    @JsonIgnore
    @Column
    private Date dateOfRegistration;
    @JsonIgnore
    @Column
    private String password;
    @JsonIgnore
    @Column
    private boolean isEnabled;
}
