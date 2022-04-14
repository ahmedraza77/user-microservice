package com.appsdeveloper.app.ws.service.impl;

import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloper.app.ws.io.entity.AddressEntity;
import com.appsdeveloper.app.ws.io.entity.UserEntity;
import com.appsdeveloper.app.ws.io.repository.UserRepository;
import com.appsdeveloper.app.ws.shared.dto.AddressDto;
import com.appsdeveloper.app.ws.shared.dto.UserDto;
import com.appsdeveloper.app.ws.shared.utils.Utils;


class UserServiceImplTest {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepository userRepo;
	
	@Mock
	private Utils utils;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	UserEntity userEntity;
	private static final String encodedPassword = "ahbfew23d";
	private static final String userId = "hjksld32rf";
	private static final String addressId = "adhd32uyh";


	@SuppressWarnings("deprecation")
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUserId(userId);
		userEntity.setFirstName("Ahmed");
		userEntity.setLastName("Raza");
		userEntity.setAddresses(getAddressesEntity());
		userEntity.setEncryptedPassword(encodedPassword);
	}

	@Test
	void testGetUserByUserId() {
		
		when(userRepo.findByUserId(anyString())).thenReturn(userEntity);
		UserDto returnValue = userService.getUserByUserId(anyString());
		
		assertNotNull(returnValue);
		assertEquals("Ahmed", returnValue.getFirstName());
	}
	
	@Test
	void testGetUserByUserId_UsernameNotFoundException() {
		
		when(userRepo.findByUserId(anyString())).thenReturn(null);		
		assertThrows(UsernameNotFoundException.class, ()->userService.getUserByUserId(anyString()));
	}
	
	@Test
	void testCreateUser() {
		
		when(userRepo.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn(addressId);
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);

		UserDto user = new UserDto();
		user.setAddresses(getAddressesDto());
		user.setFirstName("Ahmed");
		user.setLastName("Raza");
		user.setPassword("Password");
		user.setEmail("test@test.com");
				
		UserDto returnValue = userService.createUser(user);
		
		assertNotNull(returnValue);
		assertEquals(userEntity.getFirstName(), returnValue.getFirstName());
		assertEquals(userEntity.getLastName(), returnValue.getLastName());
		assertNotNull(returnValue.getUserId());
		assertEquals(userEntity.getAddresses().size(), returnValue.getAddresses().size());
	}
	
	private List<AddressDto> getAddressesDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setCity("Toronto");
		addressDto.setCountry("Canada");
		addressDto.setStreetName("Bake Street");
		addressDto.setPostalCode("290089");
		addressDto.setType("Home");
		
		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setCity("Vancouver");
		billingAddressDto.setCountry("Canada");
		billingAddressDto.setStreetName("Alpha Street");
		billingAddressDto.setPostalCode("298889");
		billingAddressDto.setType("Office");
		
		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(addressDto);
		addresses.add(billingAddressDto);
		
		return addresses;
	}
	
	private List<AddressEntity> getAddressesEntity() {
		
		List<AddressDto> addresses = getAddressesDto();		
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		return new ModelMapper().map(addresses, listType);	
	}

}
