package vn.inspiron.mcontract.modules.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
@Data
public class UserEntity implements UserDetails {
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
    private Date phone;
    @Column
    private String street;
    @Column
    private String ward;
    @Column
    private String district;
    @Column
    private String city;

    @JsonIgnore
    @Column
    private Date dateOfRegistration;
    @JsonIgnore
    @Column
    private String password;
    @JsonIgnore
    @Column
    private boolean isEnabled;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }
}
