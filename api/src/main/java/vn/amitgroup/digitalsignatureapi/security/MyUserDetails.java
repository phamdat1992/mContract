package vn.amitgroup.digitalsignatureapi.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.amitgroup.digitalsignatureapi.entity.User;

public class MyUserDetails implements UserDetails {
	private String username ;
    private String passsword;
    private Integer id;

    private List<GrantedAuthority> authorites;
    public MyUserDetails(User user, String passsword){
        this.username = user.getEmail();
        this.passsword = passsword;
        this.id = user.getId();
        List<GrantedAuthority> role= new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        role.add(simpleGrantedAuthority);
        this.authorites= role;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorites;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return passsword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
