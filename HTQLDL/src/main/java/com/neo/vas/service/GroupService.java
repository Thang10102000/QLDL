/**
 * 
 */
package com.neo.vas.service;

import java.security.Principal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.Groupss;

/**
 * @author KhanhBQ
 *
 */
public interface GroupService {
	List<Groupss> getAll();

	String saveGroup(JSONObject data, Principal principal);

	Page<Groupss> searchGroup(Groupss group, int page, int pageSize);

	String editGroup(JSONObject data, Principal principal);

	boolean deleteGroup(Groupss group);

	Page<Groupss> selectGroup(Groupss group, int page, int pageSize);

	Groupss findByGroupId(String groupId);

	List<Groupss> getGroupByLevel(Long levelID);
}
