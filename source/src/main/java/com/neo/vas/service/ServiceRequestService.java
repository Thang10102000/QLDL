package com.neo.vas.service;

import com.neo.vas.domain.ServiceRequest;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * author: YNN
 */

public interface ServiceRequestService {

    ServiceRequest getSrById(Long id);

    boolean deleteSr(long requestId);

    String saveNewSr(JSONObject data, Principal principal, MultipartFile[] contractFiles, MultipartFile[] payFiles,
                     MultipartFile[] scanFiles);

    String editSr(JSONObject data, Principal principal, MultipartFile[] contractFiles, MultipartFile[] payFiles,
                  MultipartFile[] scanFiles);

    boolean acceptSr(long requestId, Principal principal);

    Page<ServiceRequest> searchAsrs(String packages, String customer, String sr_id, String status, String policy,
                                    Date createFrom, Date createTo, Date approvedFrom, Date approvedTo,String agencyArea, int page, int pageSize);

    Long getPrice(Long brandId, String policyCode);

    Long getDiscount(Long brandId, String policyCode);

    String getPriceDiscount(Long brandId, String policyCode);

    boolean actionSr(JSONObject reqParamObj, Principal principal);

    String activateSr(JSONObject reqParamObj, Principal principal);

    List<ServiceRequest> getAllServiceRequest();

    boolean rechargeAsr(long sraId, Principal principal);

    boolean changeSr(long requestId, Principal principal);

}
