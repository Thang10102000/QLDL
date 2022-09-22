/**
 * 
 */
package com.neo.vas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Groupss;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Repository
public interface GroupsRepository extends JpaRepository<Groupss, String>, JpaSpecificationExecutor<Groupss> {
	Groupss findByGroupId(String groupId);
	
	@Query("select gr from Groupss gr where gr.levelGroups.levelId = ?1 ")
	List<Groupss> getGroupByLevel(Long levelID);
}
