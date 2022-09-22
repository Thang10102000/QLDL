package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.AgencyServiceRequest;

public interface AsrRepository
		extends JpaRepository<AgencyServiceRequest, Long>, JpaSpecificationExecutor<AgencyServiceRequest> {

	@Query(value = "select PK_SERVICE_REQUEST.create_request(:userName,:description,:brandId,:customerAcc,:totalValue) from dual", nativeQuery = true)
	String createRequest(@Param("userName") String userName, @Param("description") String description,
			@Param("brandId") Long brandId, @Param("customerAcc") String customerAcc,
			@Param("totalValue") String totalValue);

	@Query(value = "select PK_SERVICE_REQUEST.edit_request(:userName,:requestId,:description,:brandId,:customerAcc,:totalValue) from dual", nativeQuery = true)
	String editRequest(@Param("userName") String userName, @Param("requestId") Long requestId,
			@Param("description") String description, @Param("brandId") Long brandId,
			@Param("customerAcc") String customerAcc, @Param("totalValue") String totalValue);

	@Query(value = "select PK_SERVICE_REQUEST.charge_order(:userName,:asrId,:totalValue,:agencyId,:piBrandId) from dual", nativeQuery = true)
	String processCharge(@Param("userName") String userName, @Param("asrId") Long asrId,
			@Param("totalValue") Long totalValue, @Param("agencyId") Long agencyId, @Param("piBrandId") Long piBrandId);

	@Query(value = "select PK_SERVICE_REQUEST.get_type_asr(:userName,:asrId,:totalValue,:agencyId,:piBrandId) from dual", nativeQuery = true)
	String getTypeAsr(@Param("userName") String userName, @Param("asrId") Long asrId,
			@Param("totalValue") Long totalValue, @Param("agencyId") Long agencyId, @Param("piBrandId") Long piBrandId);
	
	@Query(value = "select PK_SERVICE_REQUEST.accept_asr(:userName,:asrId,:totalValue,:agencyId,:piBrandId) from dual", nativeQuery = true)
	String acceptAsr(@Param("userName") String userName, @Param("asrId") Long asrId,
			@Param("totalValue") Long totalValue, @Param("agencyId") Long agencyId, @Param("piBrandId") Long piBrandId);

	@Transactional
	@Procedure(procedureName = "PK_SERVICE_REQUEST.update_ao")
	void chargeOrder(String psUserName, Long piTotalValue, Long piOrderId, Long piAsrId);

	@Transactional
	@Procedure(procedureName = "PK_SERVICE_REQUEST.update_asr")
	void updateAsr(String psUserName, Long piStatus, String psMessage, Long piAsrId);

	@Query(value = "select PK_SERVICE_REQUEST.get_total_value(:userName,:piBrandId) from dual", nativeQuery = true)
	Long getTotalValue(@Param("userName") String userName, @Param("piBrandId") Long piBrandId);
	
	@Transactional
	@Procedure(procedureName = "PK_SERVICE_REQUEST.insert_sms")
	void insertSms(Long piOrderId, Long piAgencyID, Long piAsrId, Long piTotalValue);

	@Transactional
	@Procedure(procedureName = "LOG_EVENT")
	void createEventLog(String p_module, String p_content);

}
