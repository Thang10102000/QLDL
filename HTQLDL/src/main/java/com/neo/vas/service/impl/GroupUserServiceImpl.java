/**
 * 
 */
package com.neo.vas.service.impl;

import com.neo.vas.domain.Groupss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.repository.GroupsUsersRepository;
import com.neo.vas.service.GroupUserService;
import com.neo.vas.service.specification.GroupUserSpecification;

/**
 * @author KhanhBQ
 *
 */
@Service
public class GroupUserServiceImpl implements GroupUserService {
	@Autowired
	private GroupsUsersRepository guRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GroupsUsers> searchGroupUsers(GroupsUsers groupuser, int page, int pageSize) {
		Specification<GroupsUsers> conditions = Specification
				.where((null != groupuser.getUsersGU()) ? GroupUserSpecification.hasUser(groupuser.getUsersGU()) : null)
				.and((null != groupuser.getGroupIdGU()) ? GroupUserSpecification.hasGroup(groupuser.getGroupIdGU())
						: null);
		Page<GroupsUsers> pages = guRepository.findAll(conditions, PageRequest.of(page, pageSize));
		return pages;
	}


	@Override
	public boolean deleteUserFromGroup(GroupsUsers gu) {
		if (null != gu) {
			guRepository.delete(gu);
			if (guRepository.existsById(gu.getGuId())) {
				return false;
			} else {
				return true;
			}
		} else {
			System.err.println(gu + "Không tồn tại");
			return false;
		}
	}

	@Override
	public GroupsUsers findByGuId(Long id) {
		return guRepository.findByGuId(id);
	}

}
