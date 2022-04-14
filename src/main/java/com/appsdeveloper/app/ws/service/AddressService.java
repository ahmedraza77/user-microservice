package com.appsdeveloper.app.ws.service;

import java.util.List;

import com.appsdeveloper.app.ws.shared.dto.AddressDto;

public interface AddressService {

	List<AddressDto> getAllUserAddress(String userId);
	AddressDto getUserAddress(String addressId);

}
