/**
 * 
 */
package com.neo.vas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Users;

/**
 * @author KhanhBQ
 * @modifier YNN
 */
@Repository
public interface GroupsUsersRepository extends JpaRepository<GroupsUsers, Long>, JpaSpecificationExecutor<GroupsUsers> {
	GroupsUsers findByUsersGUAndGroupIdGU(Users users, Groupss group);

	GroupsUsers findByGuId(Long id);
	
	List<GroupsUsers> findByGroupIdGU (Groupss group);
	
	List<GroupsUsers> findByUsersGU (Users users);
}
