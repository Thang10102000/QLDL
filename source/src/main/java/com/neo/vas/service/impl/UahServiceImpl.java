package com.neo.vas.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.UsersActionHistory;
import com.neo.vas.repository.PrivilegesRepository;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.repository.UahRepository;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.UahService;
import com.neo.vas.service.specification.UahSpecification;

@Service
public class UahServiceImpl implements UahService {
	@Autowired
	private UahRepository uahRepository;
	@Autowired
	private PrivilegesRepository privilegesRepository;
	@Autowired
	private SystemFunctionalRepository sfRepository;
	@Autowired
	private UsersRepository userRepository;

	@Override
	public Page<UsersActionHistory> searchUahData(String username, String module, String privilegess, Date createdFrom,
			Date createdTo, int page, int size) {
		Specification<UsersActionHistory> conditions = Specification
				.where(!username.isEmpty() && username != null
						? UahSpecification.hasUsername(userRepository.getOne(username))
						: null)
				.and(!module.isEmpty() && module != null
						? UahSpecification.hasModule(sfRepository.getOne(Long.parseLong(module)))
						: null)
				.and(!privilegess.isEmpty() && privilegess != null
						? UahSpecification.hasPrivileges(privilegesRepository.getOne(Long.parseLong(privilegess)))
						: null)
				.and(createdFrom != null ? UahSpecification.hasCreatedDateFrom(createdFrom) : null)
				.and(createdTo != null ? UahSpecification.hasCreatedDateTo(createdTo) : null);
		Page<UsersActionHistory> pageUah = uahRepository.findAll(conditions, PageRequest.of(page, size, Sort.by("createdTime").descending()));
		return pageUah;
	}
}
