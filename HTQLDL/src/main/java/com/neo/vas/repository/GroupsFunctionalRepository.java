/**
 * 
 */
package com.neo.vas.repository;

import com.neo.vas.domain.GroupsLevelsFunctional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface GroupsFunctionalRepository extends JpaRepository<GroupsLevelsFunctional, Long> {
	@Query(value = "select glf from GroupsLevelsFunctional glf where glf.groupsGLF.groupId in (select gu.groupIdGU.groupId from GroupsUsers gu where gu.usersGU.username = ?1)")
	List<GroupsLevelsFunctional> findByUsersname(String username);

	@Query("select glf from GroupsLevelsFunctional glf where glf.groupsGLF.groupId = ?1")
	List<GroupsLevelsFunctional> findByGroupsId(String groupId);

}
