/**
 * 
 */
package com.neo.vas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author KhanhBQ
 *
 */
@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserService userService;

	@Autowired
	private CustomAuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}

	@Bean
	public CustomConcurrentSessionControlAuthenticationStrategy sessionControlStrategy() {
		return new CustomConcurrentSessionControlAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/js/**", "/style/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		 Default config for user has logged in
		http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("Quản lý tài khoản:Xem").and()
				.authorizeRequests().antMatchers("/dashboard").authenticated()
//				 Redirect to 403 page if user do not has role permission
				.and().authorizeRequests().and().exceptionHandling().accessDeniedPage("/403").and().exceptionHandling()
//				 Login & Logout cofig && Config for concurrent session
				.and().formLogin().loginPage("/login").loginProcessingUrl("/perform_login")
				.usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/dashboard")
				.failureHandler(authenticationFailureHandler).and().logout().logoutUrl("/perform_logout")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/login?message=logout");

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).invalidSessionUrl("/login")
				.sessionAuthenticationStrategy(sessionControlStrategy()).sessionFixation().newSession();

		http.sessionManagement().maximumSessions(-1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry())
				.expiredUrl("/login?message=max_session");
	}

}
