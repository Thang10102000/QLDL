package com.neo.vas.repository;

import com.neo.vas.domain.UsersLevelsFunctional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.UsersActionHistory;

import java.util.List;

@Repository
public interface UahRepository extends JpaRepository<UsersActionHistory, Long>, JpaSpecificationExecutor<UsersActionHistory> {
	@Query(value = "select * from users_action_hisory u where u.username = ?1", nativeQuery = true)
	List<UsersLevelsFunctional> findByUsersname(String username);

	@Query("select uah from UsersActionHistory uah where uah.usersUAH.username = ?1 and uah.systemFunctionalUAH.sfId = ?2")
	List<UsersActionHistory> findSFByUsernameAndSFId(String username, long sfId);
}
