/**
 * 
 */
package com.neo.vas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Levels;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface LevelsRepository extends JpaRepository<Levels, Long> {
	List<Levels> findByLevelName(String name);

	Levels findByLevelId(long id);
	
	@Query("select lv from Levels lv where lv.levelName != 'AM/KAM' ")
	List<Levels> getAllNotInAmkam();

	@Query("select lv from  Levels lv where lv.levelId=1 or lv.levelId=2")
	List<Levels> getMdsCtkv();
}
