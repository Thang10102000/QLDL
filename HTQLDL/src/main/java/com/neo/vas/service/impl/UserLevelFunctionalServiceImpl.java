package com.neo.vas.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.domain.Users;
import com.neo.vas.domain.UsersLevelsFunctional;
import com.neo.vas.repository.PrivilegesRepository;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.repository.UsersLevelsFunctionalRepository;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.UserLevelFunctionalService;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Service
public class UserLevelFunctionalServiceImpl implements UserLevelFunctionalService {
	private final UsersLevelsFunctionalRepository usersLevelsFunctionalRepository;
	private final SystemFunctionalRepository systemFunctionalRepository;
	private final PrivilegesRepository privilegesRepository;
	private final UsersRepository usersRepository;

	public UserLevelFunctionalServiceImpl(UsersLevelsFunctionalRepository usersLevelsFunctionalRepository,
			SystemFunctionalRepository systemFunctionalRepository, PrivilegesRepository privilegesRepository,
			UsersRepository usersRepository) {
		this.usersLevelsFunctionalRepository = usersLevelsFunctionalRepository;
		this.systemFunctionalRepository = systemFunctionalRepository;
		this.privilegesRepository = privilegesRepository;
		this.usersRepository = usersRepository;
	}

	public long doCheckSF(String sf) {
		List<SystemFunctional> systemFunctional = systemFunctionalRepository.findAll();
		for (SystemFunctional item : systemFunctional) {
			if (item.getSfName().equals(sf)) {
				return item.getSfId();
			}
		}
		return 0;
	}

	public long doCheckPrivileges(String privileges) {
		switch (privileges) {
		case "Xem":
			return 1;
		case "Thêm":
			return 2;
		case "Sửa":
			return 3;
		case "Xoá":
			return 4;
		case "Thực thi":
			return 5;
		default:
			return 0;
		}
	}

	@Transactional
	public boolean setUpUsersLevelsFunctional(String item, String username) {
		String[] parts = item.split(":");
		UsersLevelsFunctional ulf = new UsersLevelsFunctional();
		Users user = usersRepository.getOne(username);
		if (doCheckSF(parts[0]) != 0) {
			ulf.setSystemFunctionalULF(systemFunctionalRepository.getOne(doCheckSF(parts[0])));
			if (doCheckPrivileges(parts[1]) != 0) {
				ulf.setPrivilegesULF(privilegesRepository.getOne(doCheckPrivileges(parts[1])));
			}
		}
		ulf.setUsersULF(user);
		return usersLevelsFunctionalRepository.saveAndFlush(ulf) != null;
	}

	@Override
	@Transactional
	public boolean savePrivilegesByUsername(ArrayList<String> privileges, String username) {
		List<UsersLevelsFunctional> listOld = usersLevelsFunctionalRepository.findByUsersname(username);
		usersLevelsFunctionalRepository.deleteAll(listOld);
		for (String item : privileges) {
			try {
				setUpUsersLevelsFunctional(item, username);
			} catch (Exception e) {
				System.err.println(e);
				return false;
			}
		}
		return true;
	}

	@Override
	public List<UsersLevelsFunctional> findByUsersname(String username) {
		return usersLevelsFunctionalRepository.findByUsersname(username);
	}

//	@Override
//	public void deleteULF(String username, long sfId) {
//		List<UsersLevelsFunctional> ulf =  usersLevelsFunctionalRepository.findSFByUsernameAndSFId(username,sfId);
//		for(UsersLevelsFunctional u: ulf){
//			this.systemFunctionalRepository.deleteById(u.getUlfId());
//		}
//	}

	@Override
	public void deleteULF(long ulfId) {
		this.systemFunctionalRepository.deleteById(ulfId);
		System.out.println("delelte " + ulfId);
	}

}
