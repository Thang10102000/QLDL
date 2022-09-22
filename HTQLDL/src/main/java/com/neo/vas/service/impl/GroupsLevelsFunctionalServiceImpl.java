/**
 * 
 */
package com.neo.vas.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.neo.vas.domain.GroupsLevelsFunctional;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.repository.GroupsFunctionalRepository;
import com.neo.vas.repository.GroupsRepository;
import com.neo.vas.repository.PrivilegesRepository;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.service.GroupsLevelsFunctionalService;

/**
 * @author KhanhBQ
 *
 */
@Service
public class GroupsLevelsFunctionalServiceImpl implements GroupsLevelsFunctionalService{
	private final GroupsFunctionalRepository groupsFunctionalRepository;
	private final SystemFunctionalRepository systemFunctionalRepository;
	private final PrivilegesRepository privilegesRepository;
	private final GroupsRepository groupsRepository;

	public GroupsLevelsFunctionalServiceImpl(GroupsFunctionalRepository groupsFunctionalRepository,
			SystemFunctionalRepository systemFunctionalRepository, PrivilegesRepository privilegesRepository,
			GroupsRepository groupsRepository) {
		this.groupsFunctionalRepository = groupsFunctionalRepository;
		this.systemFunctionalRepository = systemFunctionalRepository;
		this.privilegesRepository = privilegesRepository;
		this.groupsRepository = groupsRepository;
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
	public boolean setUpUsersLevelsFunctional(String item, String groupId) {
		String[] parts = item.split(":");
		GroupsLevelsFunctional glf = new GroupsLevelsFunctional();
		Groupss group = groupsRepository.getOne(groupId);
		if (doCheckSF(parts[0]) != 0) {
			glf.setSystemFunctionalGLF(systemFunctionalRepository.getOne(doCheckSF(parts[0])));
			if (doCheckPrivileges(parts[1]) != 0) {
				glf.setPrivilegesGLF(privilegesRepository.getOne(doCheckPrivileges(parts[1])));
			}
		}
		glf.setGroupsGLF(group);
		return groupsFunctionalRepository.saveAndFlush(glf) != null;
	}

	@Override
	@Transactional
	public boolean savePrivilegesByGroupId(ArrayList<String> privileges, String groupId) {
		List<GroupsLevelsFunctional> listOld = groupsFunctionalRepository.findByGroupsId(groupId);
		groupsFunctionalRepository.deleteAll(listOld);
		for (String item : privileges) {
			try {
				setUpUsersLevelsFunctional(item, groupId);
			} catch (Exception e) {
				System.err.println(e);
				return false;
			}
		}
		return true;
	}

	@Override
	public List<GroupsLevelsFunctional> findByUsersname(String username) {
		return groupsFunctionalRepository.findByUsersname(username);
	}

	@Override
	public List<GroupsLevelsFunctional> findByGroupsId(String groupId) {
		return groupsFunctionalRepository.findByGroupsId(groupId);
	}
}
