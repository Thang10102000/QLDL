/**
 * 
 */
package com.neo.vas.service;

import java.util.List;

import com.neo.vas.domain.Levels;

/**
 * @author KhanhBQ
 *
 */
public interface LevelsService {
	List<Levels> getAllLevels();

	List<Levels> getLevelsNotInAmkam();

	List<Levels> getMdsCtkv();

	Levels findByLevelId(long id);
}
