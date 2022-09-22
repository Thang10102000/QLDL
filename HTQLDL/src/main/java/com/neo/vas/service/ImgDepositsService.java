package com.neo.vas.service;

import com.neo.vas.domain.ImageDeposits;

import java.util.List;

public interface ImgDepositsService {

	void save(ImageDeposits img);
	List<ImageDeposits> getAllFileDeposit(Long depositId);
}