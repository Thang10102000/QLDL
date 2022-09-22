package com.neo.vas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.ImageDeposits;
import com.neo.vas.repository.ImgDepositsRepository;
import com.neo.vas.service.ImgDepositsService;

import java.util.List;

@Service
public class ImgDepositsServiceImpl implements ImgDepositsService {

	@Autowired ImgDepositsRepository repo;
	
	@Override
	public void save(ImageDeposits img) {
		repo.save(img);
	}

	@Override
	public List<ImageDeposits> getAllFileDeposit(Long depositId) {
		return repo.getAllFileDeposit(depositId);
	}

}
