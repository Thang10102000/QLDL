/**
 * 
 */
package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.SystemConfig;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long>{

}
