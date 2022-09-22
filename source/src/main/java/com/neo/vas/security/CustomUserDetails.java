/**
 * 
 */
package com.neo.vas.security;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.neo.vas.domain.GroupsLevelsFunctional;
import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Users;
import com.neo.vas.domain.UsersLevelsFunctional;
import com.neo.vas.service.impl.SystemConfigServiceimpl;

import lombok.Data;

/**
 * @author KhanhBQ
 *
 */
@Data
public class CustomUserDetails implements UserDetails {
	private Users users;
	private SystemConfigServiceimpl configServiceimpl;

	public CustomUserDetails(Users users, SystemConfigServiceimpl configServiceimpl) {
		this.users = users;
		this.configServiceimpl = configServiceimpl;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		Set<UsersLevelsFunctional> roleList1 = users.getUsersULF();
		Set<GroupsUsers> groupList = users.getGroupsUsers();
		Set<GroupsLevelsFunctional> roleList2 = new HashSet<>();
		for (GroupsUsers gu : groupList) {
			Groupss g = gu.getGroupIdGU();
			roleList2.addAll(g.getGroupsGLF());
		}
		for (UsersLevelsFunctional ulf : roleList1) {
			authorities.add(new SimpleGrantedAuthority(
					ulf.getSystemFunctionalULF().getSfName() + ":" + ulf.getPrivilegesULF().getPrivilegeName()));
		}
		for (GroupsLevelsFunctional glf : roleList2) {
			authorities.add(new SimpleGrantedAuthority(
					glf.getSystemFunctionalGLF().getSfName() + ":" + glf.getPrivilegesGLF().getPrivilegeName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return users.getPassword();
	}

	@Override
	public String getUsername() {
		return users.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		if (users.getPasswordNeverExpired() == 1) {
			return true;
		} else {
			Date today = new Date();
			long diffInMillies = Math.abs(today.getTime() - users.getLastLoginDate().getTime());
			long dateDiff = TimeUnit.MILLISECONDS.convert(configServiceimpl.getAllConfigs().get(0).getPwExpiredDate(),
					TimeUnit.DAYS);
			return diffInMillies < dateDiff;
		}
	}

	@Override
	public boolean isAccountNonLocked() {
		return users.getStatus() == 1;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return users.getAuthorized() == 1;
	}

	@Override
	public boolean equals(Object otherUser) {
	    if(otherUser == null) return false;
	    else if (!(otherUser instanceof UserDetails)) return false;
	    else return (otherUser.hashCode() == hashCode());
	}

	@Override
	public int hashCode() {
	    return users.getUsername().hashCode() ;
	}
	
}
