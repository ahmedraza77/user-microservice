package com.appsdeveloper.app.ws;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.appsdeveloper.app.ws.io.entity.AuthorityEntity;
import com.appsdeveloper.app.ws.io.entity.RoleEntity;
import com.appsdeveloper.app.ws.io.entity.UserEntity;
import com.appsdeveloper.app.ws.io.repository.AuthorityRepository;
import com.appsdeveloper.app.ws.io.repository.RoleRepository;
import com.appsdeveloper.app.ws.io.repository.UserRepository;
import com.appsdeveloper.app.ws.shared.utils.Utils;

@Component
public class InitialUsersSetup {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	Utils utils;

	@Autowired
	UserRepository userRepository;

	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("Application Ready Event....");

		AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
		AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
		AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

		RoleEntity userRole = createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
		RoleEntity adminRole = createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

		if (adminRole == null)
			return;

		UserEntity adminUser = new UserEntity();
		adminUser.setFirstName("Sergey");
		adminUser.setLastName("Kargopolov");
		adminUser.setEmail("admin@test.com");
		adminUser.setEmailVerificationStatus(true);
		adminUser.setUserId(utils.generateUserId(30));
		adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("12345678"));
		adminUser.setRoles(Arrays.asList(adminRole));

	}

	private AuthorityEntity createAuthority(String name) {
		AuthorityEntity authority = authorityRepository.findByName(name);

		if (authority == null) {
			authority = new AuthorityEntity(name);
			authorityRepository.save(authority);
		}
		return authority;
	}

	private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
		RoleEntity role = roleRepository.findByName(name);

		if (role == null) {
			role = new RoleEntity(name);
			role.setAuthorities(authorities);
			roleRepository.save(role);
		}
		return role;
	}

}
