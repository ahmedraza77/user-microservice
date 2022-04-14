package com.appsdeveloper.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloper.app.ws.io.entity.AddressEntity;
import com.appsdeveloper.app.ws.io.entity.UserEntity;
import com.appsdeveloper.app.ws.io.repository.AddressRepository;
import com.appsdeveloper.app.ws.io.repository.UserRepository;
import com.appsdeveloper.app.ws.service.AddressService;
import com.appsdeveloper.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddressRepository addressRepo;

	@Override
	public List<AddressDto> getAllUserAddress(String userId) {
		
		UserEntity userEntity = userRepo.findByUserId(userId);
		List<AddressEntity> addressEntityList = addressRepo.findAllByUserDetails(userEntity);
		
		List<AddressDto> addressDtoList = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		addressEntityList.forEach(address->{
			AddressDto addressDto = modelMapper.map(address, AddressDto.class);
			addressDtoList.add(addressDto);
		});
		
		return addressDtoList;
	}

	@Override
	public AddressDto getUserAddress(String addressId) {
		
		AddressEntity addressEntity = addressRepo.findByAddressId(addressId);
		AddressDto addressDto = new ModelMapper().map(addressEntity, AddressDto.class);
		return addressDto;
	}

}
