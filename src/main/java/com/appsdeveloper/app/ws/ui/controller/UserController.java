package com.appsdeveloper.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloper.app.ws.service.AddressService;
import com.appsdeveloper.app.ws.service.UserService;
import com.appsdeveloper.app.ws.shared.dto.AddressDto;
import com.appsdeveloper.app.ws.shared.dto.UserDto;
import com.appsdeveloper.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloper.app.ws.ui.model.response.AddressRest;
import com.appsdeveloper.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;

	/* 
	 * @CrossOrigin(origins="*")
	 * @CrossOrigin(origins={"http://localhost:8083", "http://localhost:8084"})
	 * 
	*/
	@GetMapping (path = "/{id}", 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserRest getUser(@PathVariable String id) {
		UserRest userResponse = new UserRest();
		UserDto savedUser = userService.getUserByUserId(id);
		BeanUtils.copyProperties(savedUser, userResponse);
		return userResponse;
	}

	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequestModel userDetails) {

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		UserRest userResponse = modelMapper.map(createdUser, UserRest.class);
		
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@PutMapping(path="/{id}",
			consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest userResponse = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, userResponse);

		return userResponse;
	}

	@DeleteMapping(path="/{id}")
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);		
	}
	
	@GetMapping
	public List<UserRest> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		List<UserDto> allUsers = userService.getUsers(page, limit);
		List<UserRest> users = new ArrayList<>();
		
		allUsers.forEach(user->{
			UserRest userResponse = new UserRest();
			BeanUtils.copyProperties(user, userResponse);
			users.add(userResponse);
		});
		
		return users;
	}

	@GetMapping (path = "/{userId}/address", 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public List<AddressRest> getAllUserAddress(@PathVariable String userId) {
		
		List<AddressRest> addressResponse = new ArrayList<>();
		List<AddressDto> addresses = addressService.getAllUserAddress(userId);
		
		if(addresses!=null && !addresses.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			addressResponse = new ModelMapper().map(addresses, listType);
		}
		return addressResponse;
	}
	
	@GetMapping (path = "/{userId}/address/{addressId}", 
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public AddressRest getUserAddress(@PathVariable String addressId) {
		
		AddressDto address = addressService.getUserAddress(addressId);
		AddressRest addressResponse = new ModelMapper().map(address, AddressRest.class);
		return addressResponse;
	}
}
