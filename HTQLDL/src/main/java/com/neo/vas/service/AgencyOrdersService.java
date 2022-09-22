package com.neo.vas.service;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import com.neo.vas.domain.*;
import com.neo.vas.dto.AgencyOrderRequestDTO;
import com.neo.vas.dto.AgencyOrdersDTO;
import org.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 28/05/2021
 */
public interface AgencyOrdersService {
    List<AgencyOrders> getAllAgencyOrder();
    Page<AgencyOrdersDTO> searchAgencyOrder(AgencyOrders agencyOrders, String agencyArea, int page, int size );
    boolean createAgencyOrder(JSONObject data, Principal principal, MultipartFile[] file ) throws SQLException;
    boolean updateAgencyOrder(JSONObject data, Principal principal, MultipartFile[] file);
    AgencyOrders findAgencyOrdersById(Long id);
    AgencyOrders getOne(Long id);
    void deleteById(Long id);
    boolean changeStatus(Long id,Long status,Principal principal);
    DiscountCommission discountRate(String id, long orderValue);
    boolean createPayment(JSONObject data, Principal principal);
    Page<AgencyOrderPayment> searchPayment(AgencyOrderPayment payment,int page, int size);
    Deposits getDepositAmount(long agencyId);
    List<AgencyOrderRequestDTO> getSrByAgencyCode(String agencyCode);
    List<AgencyOrderRequest> orderRequestDetail(Long orderId);
    List<AgencyOrderPayment> orderPaymentDetail(Long orderId);
    List<LogAPI> searchApiLogOrder(Long order);
}
