/**
 * 
 */
package com.neo.vas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Users;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {
	Users findUsersByUsername(String username);

	@Query("select u from Users u left join GroupsUsers gu ON u.username = gu.usersGU.username "
			+ " where upper(concat(u.username)) like %?1% and FN_CONVERT_TO_VN(upper(concat(u.fullname))) like %?2% "
			+ " and FN_CONVERT_TO_VN(upper(nvl(u.email,'1'))) like %?3% and nvl(u.phone,'1') like %?4% and concat(u.levelUsers.levelId) like %?5% "
			+ " and nvl(concat(gu.groupIdGU.groupId),1) like %?6% and nvl(concat(u.areaId.areaId),1) like %?7% and nvl(concat(u.agencyId.id),1) like %?8% "
			+ " order by u.createDate desc ")
	Page<Users> searchUser(String username, String fullname, String email, String phone, String levelId, String gru,
			String areaId, String agencyId, Pageable pageable);

	@Query("select u from Users u where u.agencyId = ?1 ")
	Users getUsersByAgencyId(Long agencyId);
}