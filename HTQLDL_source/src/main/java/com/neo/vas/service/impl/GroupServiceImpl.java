package com.neo.vas.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.GroupsLevelsFunctional;
import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.GroupsFunctionalRepository;
import com.neo.vas.repository.GroupsRepository;
import com.neo.vas.repository.GroupsUsersRepository;
import com.neo.vas.repository.LevelsRepository;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.GroupService;
import com.neo.vas.service.specification.GroupSpecification;
import com.neo.vas.util.VNCharacterUtils;

/**
 * 
 */

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Service
public class GroupServiceImpl implements GroupService {
	private final GroupsRepository groupsRepository;
	private final GroupsUsersRepository groupsUsersRepository;
	private final UsersRepository usersRepository;
	private final LevelsRepository levelsRepository;
	private final GroupsFunctionalRepository glfRepository;

	public GroupServiceImpl(GroupsRepository groupsRepository, GroupsUsersRepository groupsUsersRepository,
			UsersRepository usersRepository, LevelsRepository levelsRepository,
			GroupsFunctionalRepository glfRepository) {
		this.groupsRepository = groupsRepository;
		this.groupsUsersRepository = groupsUsersRepository;
		this.usersRepository = usersRepository;
		this.levelsRepository = levelsRepository;
		this.glfRepository = glfRepository;
	}

	@Override
//	@Cacheable(value = "grList")
	public List<Groupss> getAll() {
		List<Groupss> grList = groupsRepository.findAll();
		return grList;
	}

	@Override
	@Transactional
	public String saveGroup(JSONObject data, Principal principal) {
		try {
			Groupss newGroup = new Groupss();
			if (!groupsRepository.existsById(data.getString("groupId")))
				newGroup.setGroupId(data.getString("groupId"));
			else
				return "EXISTING_GROUP";
			newGroup.setGroupName(data.getString("groupName"));
			if (data.has("group-father"))
				newGroup.setGroupFather(groupsRepository.findByGroupId(data.getString("group-father")));
			else
				newGroup.setGroupFather(null);
			newGroup.setCreateBy(principal.getName());
			newGroup.setCreateDate(new Date());
			newGroup.setLastModifyBy(principal.getName());
			newGroup.setLastModifyDate(new Date());
			if (data.has("groupLevel") && !data.getString("groupLevel").isEmpty())
				newGroup.setLevelGroups(levelsRepository.getOne(data.getLong("groupLevel")));
			else
				newGroup.setLevelGroups(null);
			groupsRepository.saveAndFlush(newGroup);
			if (data.has("group-member") && !data.getJSONArray("group-member").toString().equals("[\"\"]")) {
				int i = 0;
				for (Object username : data.getJSONArray("group-member")) {
					List<GroupsUsers> guList = groupsUsersRepository
							.findByUsersGU(usersRepository.findUsersByUsername(username.toString()));
					if (guList != null && !guList.isEmpty())
						System.out.println(username + " already in another group");
					else {
						try {
							GroupsUsers gu = new GroupsUsers();
							gu.setGroupIdGU(newGroup);
							gu.setUsersGU(usersRepository.findUsersByUsername(username.toString()));
							groupsUsersRepository.saveAndFlush(gu);
							i++;
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
				return String.valueOf(i);
			}
			return "TRUE";
		} catch (Exception e) {
			System.err.println(e);
			return "FALSE";
		}
	}

	@Override
	@Transactional
	public String editGroup(JSONObject data, Principal principal) {
		try {
			Groupss group = groupsRepository.getOne(data.getString("groupId"));
			if (!data.getString("groupName").isEmpty())
				group.setGroupName(data.getString("groupName"));
			if (data.has("groupFather"))
				group.setGroupFather(groupsRepository.findByGroupId(data.getString("groupFather")));
			else
				group.setGroupFather(null);
			group.setLastModifyBy(principal.getName());
			group.setLastModifyDate(new Date());
			if (data.has("groupLevel") && !data.getString("groupLevel").isEmpty())
				group.setLevelGroups(levelsRepository.getOne(data.getLong("groupLevel")));
			else
				group.setLevelGroups(null);
			groupsRepository.saveAndFlush(group);
			if (data.has("group-member") && !data.getJSONArray("group-member").toString().equals("[\"\"]")) {
				int i = 0;
				for (Object username : data.getJSONArray("group-member")) {
					Users user = usersRepository.getOne(username.toString());
					List<GroupsUsers> guList = groupsUsersRepository.findByUsersGU(user);
					if (guList != null && !guList.isEmpty())
						System.err.println(username + " already in another group");
					else {
						try {
							GroupsUsers gu = new GroupsUsers();
							gu.setGroupIdGU(group);
							gu.setUsersGU(usersRepository.findUsersByUsername(username.toString()));
							groupsUsersRepository.saveAndFlush(gu);
							i++;
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
				return String.valueOf(i);
			}
			return "TRUE";
		} catch (Exception e) {
			System.err.println(e);
			return "FALSE";
		}
	}

	@Override
//	@Cacheable(value = "pageGroups", key = "{#group.groupId,#group.groupName, #page, #pageSize}")
	@Transactional(readOnly = true)
	public Page<Groupss> searchGroup(Groupss group, int page, int pageSize) {
		Sort sortOrder = Sort.by("groupFather");
		Specification<Groupss> conditions = Specification
				.where(("" != group.getGroupId())
						? GroupSpecification.hasGroupId(VNCharacterUtils.removeAccent(group.getGroupId()))
						: GroupSpecification.hasGroupId(""))
				.and(null != group.getGroupName()
						? GroupSpecification.hasGroupName(VNCharacterUtils.removeAccent(group.getGroupName()))
						: null)
				.and(null != group.getGroupFather() ? GroupSpecification.hasGroupFather(group.getGroupFather()) : null);
		Page<Groupss> pageGroups = groupsRepository.findAll(conditions, PageRequest.of(page, pageSize, sortOrder));
		return pageGroups;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Groupss> selectGroup(Groupss group, int page, int pageSize) {
		Sort sortOrder = Sort.by("groupFather");
		Specification<Groupss> conditions = Specification
				.where((null != group.getLevelGroups()) ? GroupSpecification.equalGroupLevel(group.getLevelGroups())
						: GroupSpecification.equalGroupLevel(null));
		Page<Groupss> pageGroups = groupsRepository.findAll(conditions, PageRequest.of(page, pageSize, sortOrder));
		return pageGroups;
	}

	@Override
	public Groupss findByGroupId(String groupId) {
		return groupsRepository.findByGroupId(groupId);
	}

	@Override
	public List<Groupss> getGroupByLevel(Long levelID) {
		return groupsRepository.getGroupByLevel(levelID);
	}

	@Override
	@Transactional
	public boolean deleteGroup(Groupss group) {
		try {
			for (GroupsUsers gul : group.getGroupsGroups()) {
				groupsUsersRepository.delete(gul);
			}
			for (GroupsLevelsFunctional glf : group.getGroupsGLF()) {
				glfRepository.delete(glf);
			}
			groupsRepository.delete(group);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

}
