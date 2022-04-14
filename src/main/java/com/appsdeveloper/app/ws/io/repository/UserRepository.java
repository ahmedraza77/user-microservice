package com.appsdeveloper.app.ws.io.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.appsdeveloper.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String id);
	
	/*
	 * Using Native SQL Queries
	 * 
	 * */ 
	
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", 
			countQuery="select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", 
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmail(Pageable pageRequest);
	
	// With Positional Parameter
	@Query(value="select * from Users u where u.first_name = ?1",nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	// With Named Parameter
	@Query(value="select * from Users u where u.last_name = :lastName",nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);
	
	/*
	 * Advance LIKE Expression
	 * 
	 * */ 
	
	@Query(value="select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%",nativeQuery=true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);
	
	/*
	 * Select and return only specific columns
	 * 
	 * */ 
	
	@Query(value="select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%",nativeQuery=true)
	List<Object[]> findFirstAndLastNameByKeyword(@Param("keyword") String keyword);
	
	/*
	 * Update SQL Query Example
	 * 
	 * */
	
	@Transactional
	@Modifying
	@Query(value="update users u set u.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where u.user_id=:userId", nativeQuery=true)
	void updateEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, 
			@Param("userId") String userId);
	
	/*
	 * Using JPQL - Java Persistence Query Language
	 * Platform Independent
	 * 
	 * */
	@Query("select user from UserEntity user where user.userId =:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId =:userId")
	List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);
	
	@Transactional
	@Modifying
    @Query("UPDATE UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId = :userId")
	void updateUserEntityEmailVerificationStatus(
			@Param("emailVerificationStatus") boolean emailVerificationStatus, 
			@Param("userId") String userId);

}
