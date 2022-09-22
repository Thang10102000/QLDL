/**
 * 
 */
package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.SmsTemplate;

import java.util.Optional;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, String>, JpaSpecificationExecutor<SmsTemplate>{

}
