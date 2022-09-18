package vn.amitgroup.digitalsignatureapi.service;


import vn.amitgroup.digitalsignatureapi.dto.UserDto;
import vn.amitgroup.digitalsignatureapi.dto.UserJwt;
import vn.amitgroup.digitalsignatureapi.dto.UserProfile;
import vn.amitgroup.digitalsignatureapi.entity.User;


public interface UserService {
	User findByEmail(String email);

    UserDto save(String email);

    UserDto update(UserProfile user,String email);
    UserDto getUserById(Integer id);
    UserDto updateAcceptEmailNo(Boolean status,String email);
    UserJwt getUserByToken(String token);
}
