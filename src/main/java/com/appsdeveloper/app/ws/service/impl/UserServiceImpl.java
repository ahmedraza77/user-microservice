package com.appsdeveloper.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloper.app.ws.exception.ErrorMessage;
import com.appsdeveloper.app.ws.io.entity.UserEntity;
import com.appsdeveloper.app.ws.io.repository.UserRepository;
import com.appsdeveloper.app.ws.service.UserService;
import com.appsdeveloper.app.ws.shared.dto.UserDto;
import com.appsdeveloper.app.ws.shared.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private Utils utils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		
		UserEntity storedUserEntity = userRepo.findByEmail(user.getEmail());
		if(storedUserEntity != null) {
			throw new RuntimeException(ErrorMessage.RECORD_ALREADY_EXISTS.getErrorMessages());
		}
		
		user.getAddresses().forEach(address-> {
			address.setUserDetails(user);
			address.setAddressId(utils.generateAddressId(30));
		});
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);	

		String userId = utils.generateUserId(30);
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserEntity savedUser = userRepo.save(userEntity);

		UserDto returnValue = modelMapper.map(savedUser, UserDto.class);
		return returnValue;
	}
	
	@Override
	public UserDto getUserByEmail(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		if(userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessages() + email);
		}
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}
	
	@Override
	public UserDto getUserByUserId(String id) {
		UserEntity userEntity = userRepo.findByUserId(id);
		if(userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessages() + id);
		}
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}
	
	@Override
	public UserDto updateUser(String id, UserDto userDto) {
		UserEntity userEntity = userRepo.findByUserId(id);
		if(userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessages() + id);
		}
		if(StringUtils.isNotBlank(userDto.getFirstName()) 
				|| StringUtils.isNotBlank(userDto.getLastName())) {
			
			userEntity.setFirstName(userDto.getFirstName());
			userEntity.setLastName(userDto.getLastName());
		}
		UserEntity savedUser = userRepo.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(savedUser, returnValue);
		
		return returnValue;
	}
	
	@Override
	public void deleteUser(String id) {
		UserEntity userEntity = userRepo.findByUserId(id);
		if(userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessages() + id);
		}
		
		userRepo.delete(userEntity);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByEmail(email);
		if(userEntity == null) {
			throw new UsernameNotFoundException(ErrorMessage.NO_RECORD_FOUND.getErrorMessages() + email);
		}
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();

		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> findAll = userRepo.findAll(pageableRequest);
		List<UserEntity> users = findAll.getContent();
		
		users.forEach(user->{
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnValue.add(userDto);
		});
		
		return returnValue;
	}


}