/**
 * 
 */
package com.neo.vas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.Levels;
import com.neo.vas.repository.LevelsRepository;
import com.neo.vas.service.LevelsService;

/**
 * @author KhanhBQ
 *
 */
@Service
public class LevelsServiceImpl implements LevelsService {
	@Autowired
	private LevelsRepository levelsRepository;

	@Override
	//@Cacheable(value = "lvList")
	public List<Levels> getAllLevels() {
		List<Levels> lvList = levelsRepository.findAll();
		return lvList;
	}
	
	@Override
	@Cacheable(value = "lvListNotInAmkam")
	public List<Levels> getLevelsNotInAmkam() {
		List<Levels> lvListNotInAmkam = levelsRepository.getAllNotInAmkam();
		return lvListNotInAmkam;
	}

	@Override
	public List<Levels> getMdsCtkv() {
		return levelsRepository.getMdsCtkv();
	}

	@Override
	public Levels findByLevelId(long id) {
		return levelsRepository.findByLevelId(id);
	}

}
