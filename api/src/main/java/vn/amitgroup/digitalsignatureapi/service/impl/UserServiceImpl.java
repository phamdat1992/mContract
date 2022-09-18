package vn.amitgroup.digitalsignatureapi.service.impl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.Company;
import vn.amitgroup.digitalsignatureapi.entity.Role;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.CompanyRepository;
import vn.amitgroup.digitalsignatureapi.repository.UserRepository;
import vn.amitgroup.digitalsignatureapi.service.UserService;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public User findByEmail(String email) {
		log.info("get user by email: " + email);
		try {
			return userRepository.findByEmail(email);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserDto save(String email) throws ApiException {
		log.info("save a user ");
		User user = userRepository.findByEmail(email);
		if (user != null) {
			log.error("UserService save Email already exists");
			throw new ApiException(ERROR.ALREADYEXIST);
		}
		User entity = new User();
		entity.setEmail(email);
		entity.setRole(Role.ROLE_MANAGER);
		entity.setCreatedTime(new Date());
		entity.setAcceptEmailNotification(true);
		return new ModelMapper().map(userRepository.save(entity), UserDto.class);

	}

	@Override
	public UserDto update(UserProfile user, String email) {
		log.info("update a user ");
		try {
			User entity = userRepository.findByEmail(email);
			if (entity != null) {
				entity.setBirthDate(user.getBirthDate());
				entity.setFullName(user.getFullName());
				entity.setIdNo(user.getIdNo());
				entity.setSex(user.getSex());
				entity.setIssuedOn(user.getIssuedOn());
				entity.setDistrictCode(user.getDistrictCode());
				entity.setProviceCode(user.getProviceCode());
				entity.setWardCode(user.getWardCode());
				entity.setAddress(user.getAddress());
				entity.setAddress(user.getAddress());
				entity.setPhoneNumber(user.getPhoneNumber());
				entity.setAvatarPath(user.getAvatarPath());
				return new ModelMapper().map(userRepository.save(entity), UserDto.class);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;

	}

	@Override
	public UserDto getUserById(Integer id) {
		log.info("get a user by id ");
		try {
			return new ModelMapper().map(userRepository.findById(id).get(), UserDto.class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserDto updateAcceptEmailNo(Boolean status, String email) {
		try {
			User entity = userRepository.findByEmail(email);
			if (entity != null) {
				entity.setAcceptEmailNotification(status);
				return new ModelMapper().map(userRepository.save(entity), UserDto.class);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;
	}

	@Override
	public UserJwt getUserByToken(String email) throws ApiException {
		User user = userRepository.findByEmail(email);
		Company company = companyRepository.findByUser_Email(email);
		if (user == null) {
			throw ErrorCodeException.NullException();
		}
		UserJwt userJwt = new ModelMapper().map(user, UserJwt.class);
		if (company != null) {
			CompanyDto dto = new ModelMapper().map(company, CompanyDto.class);
			userJwt.setCompany(dto);
		}

		return userJwt;

	}
	
}
