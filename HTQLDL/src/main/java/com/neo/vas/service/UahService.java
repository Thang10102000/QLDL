package com.neo.vas.service;

import java.util.Date;

import org.springframework.data.domain.Page;

import com.neo.vas.domain.UsersActionHistory;

public interface UahService {

	Page<UsersActionHistory> searchUahData(String username, String module, String privilegess, Date createdFrom,
			Date createdTo, int realPage, int size);

}
