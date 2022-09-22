/**
 * 
 */
package com.neo.vas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.UsersLevelsFunctional;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface UsersLevelsFunctionalRepository extends JpaRepository<UsersLevelsFunctional, Long> {
	@Query(value = "select * from users_levels_functional u where u.username = ?1", nativeQuery = true)
	List<UsersLevelsFunctional> findByUsersname(String username);

//	@Query(value = "select * from users_levels_functional u where u.usersULF.username =?1 and u.systemFunctionalULF.sfId = ?2", nativeQuery = true)
//	List<UsersLevelsFunctional> findSFByUsernameAndSFId(String username, long sfId);
	@Query("select ulf from UsersLevelsFunctional ulf where ulf.usersULF.username = ?1 and ulf.systemFunctionalULF.sfId = ?2")
	List<UsersLevelsFunctional> findSFByUsernameAndSFId(String username, long sfId);
}
