package com.appsdeveloper.app.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloper.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto userDto);
	UserDto getUserByEmail(String email);
	UserDto getUserByUserId(String id);
	UserDto updateUser(String id, UserDto userDto);
	void deleteUser(String id);
	List<UserDto> getUsers(int page, int limit);
}
