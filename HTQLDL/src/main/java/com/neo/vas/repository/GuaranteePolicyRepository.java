package com.neo.vas.repository;

import com.neo.vas.domain.GuaranteePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * @author hai
 *
 */
@Repository
public interface GuaranteePolicyRepository extends JpaRepository<GuaranteePolicy,Long> {
    @Query("select gp.limit from GuaranteePolicy gp")
    Integer getLimit();
}
