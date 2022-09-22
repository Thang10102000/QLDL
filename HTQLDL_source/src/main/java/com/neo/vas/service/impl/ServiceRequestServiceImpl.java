package com.neo.vas.service.impl;

import com.neo.vas.config.DataSourceConnection;
import com.neo.vas.constant.ConstantSaveFile;
import com.neo.vas.constant.ConstantStatusSR;
import com.neo.vas.domain.ServiceRequest;
import com.neo.vas.domain.ServiceRequestActivate;
import com.neo.vas.domain.SystemAttr;
import com.neo.vas.repository.*;
import com.neo.vas.service.ServiceRequestService;
import com.neo.vas.service.specification.ServiceRequestSpecification;
import okhttp3.*;
import oracle.jdbc.internal.OracleTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * author: YNN
 */
@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {
    public static final int STATUS_CHARGE_OK = 1;
    public static final int STATUS_CHARGE_NOK = 0;
    public static final int STATUS_API_OK = 1;
    public static final int STATUS_API_NOK = 0;
    public static final String URL_API = "http://10.54.20.79:8888/api/registerPackage";
    @Autowired
    private ServiceRequestRepository srRepository;
    @Autowired
    private CustomerRepository cusRepository;
    @Autowired
    private BrandRepository brRepository;
    @Autowired
    private SRARepository sraRepository;
    @Autowired
    private ServletContext context;
    @Autowired
    private SystemFunctionalRepository systemFunctional;
    @Autowired
    private SystemAttrRepository systemAttrRepository;

    @Override
    @Transactional
    public String saveNewSr(JSONObject data, Principal principal, MultipartFile[] contractFiles,
                            MultipartFile[] payFiles, MultipartFile[] scanFiles) {
        try {
            ServiceRequest newSr = new ServiceRequest();
            if (!data.getString("customer_info").isEmpty()) {
                try {
                    newSr.setCustomer(cusRepository.getOne(Long.parseLong(data.getString("customer_info"))));
                } catch (Exception e) {
                    System.err.println(e);
                    return "FALSE";
                }
            } else
                return "INVALID_PARAMS";
            if (!data.getString("pkg").isEmpty()) {
                try {
                    newSr.setBrandASR(brRepository.getOne(Long.parseLong(data.getString("pkg"))));
                } catch (Exception e) {
                    System.err.println(e);
                    return "FALSE";
                }
            } else
                return "INVALID_PARAMS";
            if (!data.getString("policy_c").isEmpty()) {
                newSr.setPolicy(data.getString("policy_c").trim());
            } else
                return "INVALID_PARAMS";
            if (!data.getString("price").isEmpty()) {
                newSr.setPrice(Long.parseLong(data.getString("price").trim()));
            } else
                return "INVALID_PARAMS";
            if (!data.getString("discount").isEmpty()) {
                newSr.setDiscountCost(Long.parseLong(data.getString("discount").trim()));
            }
            if (!data.getString("quantity").isEmpty()) {
                newSr.setQuantity(Long.parseLong(data.getString("quantity").trim()));
            } else
                return "INVALID_PARAMS";
            if (!data.getString("amount").isEmpty()) {
                newSr.setAmount(Integer.parseInt(data.getString("amount").trim()));
            } else
                return "INVALID_PARAMS";
            SystemAttr systemAttr = systemAttrRepository.findSystemAttrbyTypeName("SERVICE_REQUEST","APPROVE_SERVICE_REQUEST");
            if(systemAttr.getValue().equals("TRUE"))
                newSr.setStatus(ConstantStatusSR.APPROVED);
            else
                newSr.setStatus(ConstantStatusSR.NEWLY_CREATED);
            newSr.setCreated(new Date());
            newSr.setCreator(principal.getName());
            newSr.setRemainingQuantity(Integer.parseInt(data.getString("quantity").trim()));
            srRepository.saveAndFlush(newSr);
            //contractFiles
            if (contractFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(contractFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "CONTRACT_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            //payFiles
            if (payFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(payFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "PAY_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            //scanFiles
            if (scanFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(scanFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "SCAN_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            return "TRUE";
        } catch (Exception e) {
            System.err.println(e);
            return "FALSE";
        }
    }

    @Override
    @Transactional
    public String editSr(JSONObject data, Principal principal, MultipartFile[] contractFiles,
                         MultipartFile[] payFiles, MultipartFile[] scanFiles) {
        try {
            ServiceRequest newSr = getSrById(Long.parseLong(data.getString("srId")));
            if (!data.getString("customer.id").isEmpty()) {
                try {
                    newSr.setCustomer(cusRepository.getOne(Long.parseLong(data.getString("customer.id"))));
                } catch (Exception e) {
                    System.err.println(e);
                    return "FALSE";
                }
            } else
                return "INVALID_PARAMS";
            if (!data.getString("brandASR.id").isEmpty()) {
                try {
                    newSr.setBrandASR(brRepository.getOne(Long.parseLong(data.getString("brandASR.id"))));
                } catch (Exception e) {
                    System.err.println(e);
                    return "FALSE";
                }
            } else
                return "INVALID_PARAMS";
            if (!data.getString("policy").isEmpty()) {
                newSr.setPolicy(data.getString("policy").trim());
            } else
                return "INVALID_PARAMS";
            if (!data.getString("price").isEmpty()) {
                newSr.setPrice(Long.parseLong(data.getString("price").trim()));
            } else
                return "INVALID_PARAMS";
            if (!data.getString("discountCost").isEmpty()) {
                newSr.setDiscountCost(Long.parseLong(data.getString("discountCost").trim()));
            }
            if (!data.getString("quantity").isEmpty()) {
                newSr.setQuantity(Long.parseLong(data.getString("quantity").trim()));
            } else
                return "INVALID_PARAMS";
            if (!data.getString("amount").isEmpty()) {
                newSr.setAmount(Integer.parseInt(data.getString("amount").trim()));
            } else
                return "INVALID_PARAMS";
            newSr.setModified(new Date());
            newSr.setModifier(principal.getName());
            srRepository.saveAndFlush(newSr);
            //contractFiles
            if (contractFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(contractFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "CONTRACT_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            //payFiles
            if (payFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(payFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "PAY_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            //scanFiles
            if (scanFiles != null) {
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(scanFiles);
                int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
                for (int i = 0; i < totalFileDeposits; i++) {
                    try {
                        srRepository.insertFileInfo(principal.getName(), newSr.getSrId(), returnListStr.get(i), 0L, "SCAN_FILE_SR");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR_FILE";
                    }
                }
            }
            return "TRUE";
        } catch (Exception e) {
            System.err.println(e);
            return "FALSE";
        }
    }

    @Override
    @Transactional
    public boolean deleteSr(long requestId) {
        try {
            srRepository.deleteById(requestId);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public ServiceRequest getSrById(Long id) {
        Optional<ServiceRequest> optAsr = srRepository.findById(id);
        ServiceRequest sr = null;
        if (optAsr.isPresent()) {
            sr = optAsr.get();
        } else {
            throw new RuntimeException("AgencyServiceRequest " + id);
        }
        return sr;
    }

    @Override
    public boolean acceptSr(long requestId, Principal principal) {
        try {
            ServiceRequest newSr = getSrById(requestId);
            newSr.setStatus(ConstantStatusSR.PENDING);
            newSr.setModified(new Date());
            newSr.setModifier(principal.getName());
            srRepository.saveAndFlush(newSr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Page<ServiceRequest> searchAsrs(String packages, String customer, String sr_id, String status, String policy,
                                           Date createFrom, Date createTo, Date approvedFrom, Date approvedTo, String agencyArea,  int page, int pageSize) {
        Specification<ServiceRequest> conditions = Specification
                .where(!sr_id.isEmpty() && sr_id != null
                        ? ServiceRequestSpecification.hasRequestId(Long.parseLong(sr_id))
                        : null)
                .and(!status.isEmpty() && status != null
                        ? ServiceRequestSpecification.hasStatus(Integer.valueOf(status))
                        : null)
                .and(!policy.isEmpty() && policy != null ? ServiceRequestSpecification.hasPolicy(policy) : null)
                .and(!packages.isEmpty() && !packages.equals("null")
                        ? ServiceRequestSpecification.hasPackage(brRepository.getOne(Long.parseLong(packages)))
                        : null)
                .and(!customer.isEmpty() && !customer.equals("null")
                        ? ServiceRequestSpecification.hasCustomer(cusRepository.getOne(Long.parseLong(customer)))
                        : null)
                .and(!agencyArea.isEmpty() && !agencyArea.equals("null")
                        ? ServiceRequestSpecification.hasCustomers(cusRepository.getCustomerByAreaId(Long.parseLong(agencyArea)))
                        : null)
                .and(createFrom != null ? ServiceRequestSpecification.hasCreatedFrom(createFrom) : null)
                .and(createTo != null ? ServiceRequestSpecification.hasCreatedTo(createTo) : null)
                .and(approvedFrom != null ? ServiceRequestSpecification.hasApprovedFrom(approvedFrom) : null)
                .and(approvedTo != null ? ServiceRequestSpecification.hasApprovedTo(approvedTo) : null);
        Page<ServiceRequest> pageAsr = srRepository.findAll(conditions,
                PageRequest.of(page, pageSize, Sort.by("created").descending()));
        return pageAsr;
    }

    @Override
    public Long getPrice(Long brandId, String policyCode) {
        Long price = null;
        try {
            price = srRepository.getPrice(policyCode, brandId);
            return price;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    public Long getDiscount(Long brandId, String policyCode) {
        Long price = null;
        try {
            price = srRepository.getDiscount(policyCode, brandId);
            return price;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    @Override
    public String getPriceDiscount(Long brandId, String policyCode) {
        String json = null;
        try {
            json = srRepository.getPriceDiscount(policyCode, brandId);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    //  upload multi file
    public ArrayList<String> uploadFile(MultipartFile[] file) {
        // Upload file
        // Path to upload file.
        ArrayList<String> returnStr = new ArrayList<String>();
        String fullSavePath = context.getRealPath("") + ConstantSaveFile.SAVE_DIRECTORY_SRS + File.separator;
        String contextPath = context.getContextPath();
        // Create dir if dir not exist.
        File fileSaveDir = new File(fullSavePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        // save the file on the local file system (can be multi files)
        int upSuccess = 0;
        int upFail = 0;
        for (MultipartFile f : file) {
            String filename = f.getOriginalFilename();
            try {
                Path path = Paths.get(fullSavePath + filename);
                Files.copy(f.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                upSuccess++;
                returnStr.add(contextPath + "/" + ConstantSaveFile.SAVE_DIRECTORY_SRS + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                upFail++;
            }
        }
        String uploadResultStr = "File yêu cầu dịch vụ : " + upSuccess + " file" + "," + "Fail upload: "
                + upFail + " file";
        // index = upSuccess + 1
        returnStr.add(uploadResultStr);
        // total file path (imgFilePath) need inserted
        returnStr.add(String.valueOf(upSuccess));
        return returnStr;
    }

    public boolean changeSr(long requestId, Principal principal) {
        try {
            ServiceRequest newSr = getSrById(requestId);
            newSr.setStatus(ConstantStatusSR.CANCELLED);
            newSr.setModified(new Date());
            newSr.setModifier(principal.getName());
            srRepository.saveAndFlush(newSr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String activateSr(JSONObject data, Principal principal) {
        try {
            ServiceRequest sr = getSrById(Long.parseLong(data.getString("srId")));
            Integer quantityActive = Integer.parseInt(data.getString("quantity_active"));
            Long amountCharge = sr.getPrice() * quantityActive;
            String getOrderId = srRepository.getOrderSr(sr.getSrId(), amountCharge);

			if(getOrderId.equals("INVALID_ORDER"))
                return "INVALID_ORDER";
			if(getOrderId.equals("ORDER_NOT_COMPLETE"))
			    return "ORDER_NOT_COMPLETE";
            if(getOrderId.equals("ORDER_OUT_OF_MONEY"))
                return "ORDER_OUT_OF_MONEY";
            Date dateActive;
            if (data.getString("check_time").equals("1"))//Kich hoat ngay
                dateActive = new Date();
            else
                dateActive = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(data.getString("activatedDate"));
            String callApi = activePackage(sr, dateActive, quantityActive, principal);
            if (!callApi.equals("SUCCESS")){
                return callApi;
            }
            Integer sttChargeAct;
            Long orderId = Long.parseLong(getOrderId);
            try {
                srRepository.chargeOrder(principal.getName(), amountCharge, orderId, sr.getSrId());
                sttChargeAct = STATUS_CHARGE_OK;
            } catch (Exception ex) {
                sttChargeAct = STATUS_CHARGE_NOK;
                ex.printStackTrace();
            }
            sr.setStatus(ConstantStatusSR.ACTIVATED);
            sr.setModified(new Date());
            sr.setModifier(principal.getName());
            Integer remaining = sr.getRemainingQuantity() - quantityActive;
            sr.setRemainingQuantity(remaining);
            srRepository.saveAndFlush(sr);
            ServiceRequestActivate sra = new ServiceRequestActivate();
            sra.setServiceRequest(sr);
            sra.setQuantity(quantityActive);
            sra.setRemaining(remaining);
            sra.setStatus(sttChargeAct);
            sra.setActivated(dateActive);
            sra.setActivator(principal.getName());
            sra.setAmount(amountCharge);
            sra.setOrderId(orderId);
            Integer checkSl = sraRepository.checkQuantity(Long.parseLong(data.getString("srId")));
            sra.setOrderCode("ACTIVATED_" + data.getString("srId") + "_" + (checkSl + 1));
            sraRepository.saveAndFlush(sra);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FALSE";
        }
    }

    @Override
    public boolean actionSr(JSONObject data, Principal principal) {
        try {
            ServiceRequest newSr = getSrById(Long.parseLong(data.getString("srId")));
            if (!data.getString("description").isEmpty()) {
                newSr.setDescription(data.getString("description"));
            }
            newSr.setStatus(Long.parseLong(data.getString("status")));
            if (data.getString("status").equals(String.valueOf(ConstantStatusSR.APPROVED)))
                newSr.setApproved(new Date());
            else {
                newSr.setModified(new Date());
                newSr.setModifier(principal.getName());
            }
            srRepository.saveAndFlush(newSr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<ServiceRequest> getAllServiceRequest() {
        return srRepository.findAll();
    }


    public String activePackage(ServiceRequest srvRequest, Date dateActive, Integer quantity, Principal principal) throws IOException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateActive);
            cal.add(Calendar.DAY_OF_MONTH, srvRequest.getBrandASR().getActiveDay());
            Date endDateActive = cal.getTime();
            Long amount = srvRequest.getPrice() * quantity;
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            String requestUrl = URL_API + "?isdn=" + srvRequest.getCustomer().getPhone()
                    + "&packageCode=" + srvRequest.getBrandASR().getBrandId() + "&agencyCode=" + srvRequest.getCustomer().getAgencyCode()
                    + "&maxNumber=" + quantity + "&regDatetime=" + dateFormat.format(dateActive)
                    + "&staDatetime=" + dateFormat.format(dateActive) + "&endDatetime=" + dateFormat.format(endDateActive)
                    + "&charge_price=" + amount + "&username=kammobiedu&password=kammobiedu@321";
            RequestBody body = RequestBody.create(mediaType, "");
            System.err.println(requestUrl);
            Request request = new Request.Builder().url(requestUrl)
                    .method("POST", body).build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            if (res.length() > 1024)
                res = res.substring(0, 1024);
            System.out.println("-------------------Api active package SR_ID-" + srvRequest.getSrId() + ": " + res);
            JSONObject json = new JSONObject(res);
            if (json.getString("resultCode").equals("1")) {
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        res, principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_OK);
                return "SUCCESS";
            }else if (json.getString("resultCode").equals("-1")){
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        "Authentication api information error", principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_NOK);
                return "ERROR AUTHENTICATION";
            }
            else if (json.getString("resultCode").equals("0")){
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        "Transaction Duplicate", principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_NOK);
                return "TRANSACTION DUPLICATE";
            }
            else if (json.getString("resultCode").equals("500")){
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        "System error api", principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_NOK);
                return "ERROR SYSTEM";
            } else if(response.code() == 400){
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        "HTTP Status 400 – Bad Request", principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_NOK);
                return "BAD REQUEST";
            }
            else{
                srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/service-request").getSfId(), requestUrl,
                        res, principal.getName(), URL_API, srvRequest.getSrId(), STATUS_API_NOK);
                return "FAILED";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String response;
            if (ex.getCause() != null)
                response = ex.getCause().getMessage();
            else
                response = ex.getLocalizedMessage();
            if (response.length() > 1024)
                response = response.substring(0, 1024);
            System.out.println("-------------------Error call api register service ASR_ID-" + srvRequest.getSrId() + ": " + response);
            return "FAILED";
        }
    }

    @Override
    public boolean rechargeAsr(long sraId, Principal principal) {
        try {
            ServiceRequestActivate sra = sraRepository.getOne(sraId);
            srRepository.chargeOrder(principal.getName(), sra.getAmount(), sra.getOrderId(), sra.getServiceRequest().getSrId());
            sra.setStatus(STATUS_CHARGE_OK);
            sraRepository.saveAndFlush(sra);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

}
