package vn.amitgroup.digitalsignatureapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.repository.UserRepository;
import vn.amitgroup.digitalsignatureapi.service.OtpCodeService;
@Service
public class MyUserDetailsService implements UserDetailsService{
	private final String SIGNIN_TYPE = "SIGNIN13@98qt";
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private OtpCodeService codeService;
	@Value("${defult.password}")
    private String pass;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Integer integer=codeService.getOtp(SIGNIN_TYPE + username);
		User user= userRepository.findByEmail(username);
		if (user ==null) {
            throw new UsernameNotFoundException(username);
		}
		if(integer==0){
			return new MyUserDetails(user,encoder.encode(pass));
		}
		else{
			return new MyUserDetails(user,encoder.encode(String.valueOf(integer)));
		}
	}

}
