/**
 * 
 */
package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Privilegess;

/**
 * @author KhanhBQ
 *
 */
@Repository
public interface PrivilegesRepository extends JpaRepository<Privilegess, Long>{

}
