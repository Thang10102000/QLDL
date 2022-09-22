/**
 * 
 */
package com.neo.vas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author KhanhBQ
 *
 */
@Configuration
@EnableJpaRepositories("com.neo.vas.repository")
@EnableTransactionManagement
public class RepositoryConfiguration {
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(RepositoryConfiguration.class);
}
