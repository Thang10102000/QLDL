/**
 * 
 */
package com.neo.vas.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.neo.vas.domain.Users;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.impl.SystemConfigServiceimpl;

/**
 * @author KhanhBQ
 *
 */
@Component
public class UserService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private SystemConfigServiceimpl scsImpl;

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String ip = getClientIP();
		if (loginAttemptService.isBlocked(ip)) {
			throw new InternalAuthenticationServiceException("blocked");
		}
		Users user = usersRepository.findUsersByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user, scsImpl);
	}

	private String getClientIP() {
		String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
