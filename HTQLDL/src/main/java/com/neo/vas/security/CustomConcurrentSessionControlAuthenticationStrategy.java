/**
 * 
 */
package com.neo.vas.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

import com.neo.vas.service.impl.SystemConfigServiceimpl;

/**
 * @author KhanhBQ
 *
 */
public class CustomConcurrentSessionControlAuthenticationStrategy
		extends ConcurrentSessionControlAuthenticationStrategy {

	public CustomConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
		super(sessionRegistry);
	}

	@Autowired
	private SessionRegistry sessionRegistry;

	@Autowired
	private SystemConfigServiceimpl scsImpl;

	@Override
	protected int getMaximumSessionsForThisUser(Authentication authentication) {
		System.err.println(
				"Max login for: " + authentication.getName() + " is " + scsImpl.getAllConfigs().get(0).getMaxLogin());
		return scsImpl.getAllConfigs().get(0).getMaxLogin();
	}

	@Override
	public void onAuthentication(Authentication authentication, HttpServletRequest request,
			HttpServletResponse response) {
		List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
		int sessionCount = sessions.size();
		int allowedSessions = getMaximumSessionsForThisUser(authentication);
		if (sessionCount < allowedSessions) {
			System.err.println(authentication.getName() + " :::: login");
			// They haven't got too many login sessions running at present
			return;
		}
		if (sessionCount == allowedSessions) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				// Only permit it though if this request is associated with one of the
				// already registered sessions
				for (SessionInformation si : sessions) {
					if (si.getSessionId().equals(session.getId())) {
						return;
					}
				}
			}
			// If the session is null, a new one will be created by the parent class,
			// exceeding the allowed number
		}
		allowableSessionsExceeded(sessions, allowedSessions, sessionRegistry);
	}

}
