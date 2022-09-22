package com.neo.vas.repository;

import com.neo.vas.domain.AgencyOrderRequest;
import com.neo.vas.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.ServiceRequest;

import java.util.List;

public interface ServiceRequestRepository
		extends JpaRepository<ServiceRequest, Long>, JpaSpecificationExecutor<ServiceRequest> {

	@Query(value = "select PK_SERVICE_REQUEST_V2.get_price(:psPolicyCode,:piBrandId) from dual", nativeQuery = true)
	Long getPrice(@Param("psPolicyCode") String psPolicyCode, @Param("piBrandId") Long piBrandId);

	@Query(value = "select PK_SERVICE_REQUEST_V2.get_discount(:psPolicyCode,:piBrandId) from dual", nativeQuery = true)
	Long getDiscount(@Param("psPolicyCode") String psPolicyCode, @Param("piBrandId") Long piBrandId);

	@Query(value = "select PK_SERVICE_REQUEST_V2.get_price_discount(:psPolicyCode,:piBrandId) from dual", nativeQuery = true)
	String getPriceDiscount(@Param("psPolicyCode") String psPolicyCode, @Param("piBrandId") Long piBrandId);

	@Query(value = "select PK_SERVICE_REQUEST_V2.insert_attachment(:psUsername,:piSrID,:psFilePath,:psFileSize,:psFunction) from dual", nativeQuery = true)
	String insertFileInfo(@Param("psUsername") String psUsername, @Param("piSrID") Long piSrID,
						  @Param("psFilePath") String psFilePath, @Param("psFileSize") Long psFileSize, @Param("psFunction") String psFunction);

	@Query(value = "select PK_SERVICE_REQUEST_V2.get_order_sr(:piSrID, :piAmountCharge) from dual", nativeQuery = true)
	String getOrderSr(@Param("piSrID") Long piSrID, @Param("piAmountCharge") Long piAmountCharge);

	@Transactional
	@Procedure(procedureName = "PRO_API_LOG")
	void createApiLog(Long p_sf_id, String p_request, String p_response, String p_creator, String p_url, Long p_reference_id, int p_status);

	@Transactional
	@Procedure(procedureName = "PK_SERVICE_REQUEST_V2.update_ao")
	void chargeOrder(String psUserName, Long piTotalValue, Long piOrderId, Long piSrId);

	@Query("select c from Customer c where (c.agencyCode is null or c.agencyCode = ?1 ) order by c.name ")
	List<Customer> getCustomerByAgencyCode(String agencyCode);

	@Query("select ao from AgencyOrderRequest ao where ao.serviceRequest.srId = ?1 ")
	AgencyOrderRequest getAgencyOrderRequestBySrId(long srId);

	@Query("select sr from ServiceRequest sr where sr.customer in ( :listCustomerId ) and sr.status = 2 order by sr.customer.id ")
	List<ServiceRequest> getServiceRequestByCustomer(List<Customer> listCustomerId);

	@Query("select sr from ServiceRequest sr where sr.srId in ?1 order by sr.customer.id ")
	List<ServiceRequest> getServiceRequestByAorId(List<Long> idAo);

}
