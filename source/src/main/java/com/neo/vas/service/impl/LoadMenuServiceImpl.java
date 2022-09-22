/**
 * 
 */
package com.neo.vas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.service.LoadMenuService;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Service
public class LoadMenuServiceImpl implements LoadMenuService{
	@Autowired
	private SystemFunctionalRepository sfRepository;

//	@Override
//	//@Cacheable(value = "listSf")
//	public List<SystemFunctional> loadMenuByUser() {
//		List<SystemFunctional> listSf = sfRepository.findAll();
//		return listSf;
//	}
	
	@Override
	public List<SystemFunctional> loadMenuByUser() {
		List<SystemFunctional> listSf = sfRepository.selectFunction();
		return listSf;
	}

}
