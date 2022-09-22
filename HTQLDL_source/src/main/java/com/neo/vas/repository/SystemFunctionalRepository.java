/**
 *
 */
package com.neo.vas.repository;

import java.util.List;

import com.neo.vas.domain.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.SystemFunctional;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Repository
public interface SystemFunctionalRepository extends JpaRepository<SystemFunctional, Long>{
	@Query(value = "WITH cte ( sf_id, sf_name, font_awesome_icon_class, menu_father_id, status, "
			+ " url_controller, lvl, h_id, h_status, h_name, h_desc "
			+ " ) AS ( SELECT sf_id, sf_name, font_awesome_icon_class, menu_father_id, status, "
			+ " url_controller, 1 lvl, sf_id || '_' h_id, status || '_' || sf_id || '_' h_status, "
			+ " sf_name h_name, url_controller h_desc FROM system_functional WHERE menu_father_id = sf_id "
			+ " UNION ALL SELECT t.sf_id, t.sf_name, t.font_awesome_icon_class, t.menu_father_id, "
			+ " t.status, t.url_controller, lvl + 1, cte.h_id || t.sf_id || '_', cte.h_status "
			+ " || t.status || '_' || t.sf_id || '_', t.sf_name, cte.h_desc || t.url_controller "
			+ " FROM system_functional t INNER JOIN cte ON cte.sf_id = t.menu_father_id "
			+ " where t.sf_id not in(select sf_id from system_functional where menu_father_id = sf_id) "
			+ " ) SELECT sf_id, font_awesome_icon_class, h_name sf_name, status, h_desc url_controller, "
			+ " menu_father_id FROM cte ORDER BY cte.h_status", nativeQuery = true)
	List<SystemFunctional> selectFunction();

	@Query("select sf from SystemFunctional sf where lower(sf.urlController) = ?1 ")
	SystemFunctional getMenuCallApi(String url);
	@Query("select sf from SystemFunctional sf order by sf.sfName ")
	List<SystemFunctional> getAllSF();

	@Query("select sf from SystemFunctional sf where sf.sfId = ?1 ")
	SystemFunctional getSFById(long sfId);

	SystemFunctional findSystemFunctionalBySfId(Long sfId);

//	@Query("select sf from SystemFunctional sf where FN_CONVERT_TO_VN(upper(nvl(sf.sfId,'1'))) like %?1% and FN_CONVERT_TO_VN(upper(nvl(sf.sfName,'1'))) like %?2% and nvl(sf.urlController,'3') like %?3% "
//			+ " and FN_CONVERT_TO_VN(upper(nvl(sf.sfFather.sfName,'1'))) like %?4%" + " and sf.status != 2")
//	Page<SystemFunctional> findAllSF(String sfId, String sfName, String urlController, String sfFather, Pageable pageable);

	@Query("select sf from SystemFunctional sf where FN_CONVERT_TO_VN(upper(nvl(sf.sfId,'1'))) like %?1% and FN_CONVERT_TO_VN(upper(nvl(sf.sfName,'1'))) like %?2% and nvl(sf.urlController,'3') like %?3% "
			+ " and FN_CONVERT_TO_VN(upper(nvl(sf.sfFather.sfName,'1'))) like %?4%" +
			 " and FN_CONVERT_TO_VN(upper(nvl(sf.status,'1'))) like %?5%" +" and sf.status != 2")
	Page<SystemFunctional> findAllSF(String sfId, String sfName, String urlController, String sfFather, String sfStatus, Pageable pageable);

	@Query("SELECT sf FROM SystemFunctional sf where sf.status <> 2 ")
	Page<SystemFunctional> findAllSF(Pageable pageable);

	@Query(value = "SELECT sf.sfName FROM SystemFunctional AS sf where sf.status =?1")
	List<String> findSFNameByStatus(Integer status);

	@Query(value = "SELECT sf FROM SystemFunctional AS sf where sf.sfName =?1")
	SystemFunctional findSFBysfFatherName(String sfName);
}