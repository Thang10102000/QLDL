package com.neo.vas.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.neo.vas.config.DataSourceConnection;
import com.neo.vas.constant.ConstantSaveFile;
import com.neo.vas.constant.ConstantStatusOrder;
import com.neo.vas.domain.*;
import com.neo.vas.dto.AgencyOrderRequestDTO;
import com.neo.vas.dto.AgencyOrdersDTO;
import com.neo.vas.repository.*;
import com.neo.vas.service.specification.AgencySpecification;
import oracle.jdbc.internal.OracleTypes;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.neo.vas.service.AgencyOrdersService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 28/05/2021
 */
@Service
public class AgencyOrdersServiceImpl implements AgencyOrdersService {
    @Autowired
    private AgencyOrdersRepository agencyOrdersRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private AgencyDiscountCommissionRepository agencyDCRepository;
    @Autowired
    private AgencyOrderPaymentRepository agencyOrderPaymentRepository;
    @Autowired
    private DiscountCommissionRepository dcRepo;
    @Autowired
    private LimitDiscountRepository lmRepo;
    @Autowired
    private ServletContext context;
    @Autowired
    private DepositeRepository depositeRepository;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    @Autowired
    private AgencyOrderRequestRepository orderRequestRepository;
    @Autowired
    private ApiLogRepository apiLogRepository;
    @Autowired
    private SystemAttrRepository systemAttrRepository;


    Date date = new Date();
    public AgencyOrdersServiceImpl() throws SQLException {
    }

    @Override
    public List<AgencyOrders> getAllAgencyOrder() {
        return agencyOrdersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyOrdersDTO> searchAgencyOrder(AgencyOrders agencyOrders, String agencyArea, int page, int size) {
        try {
            Specification<AgencyOrders> conditions = Specification
                    .where((null != agencyOrders.getAgencyAO()) ? AgencySpecification.hasAgency(agencyOrders.getAgencyAO()) : null)
                    .and((0 != agencyOrders.getOrderId()) ? AgencySpecification.hasAgencyOrder(agencyOrders.getOrderId()) : null)
                    .and((null != agencyOrders.getStartDate()) ? AgencySpecification.hasStartDate(agencyOrders.getStartDate()) : null)
                    .and((null != agencyOrders.getEndDate()) ? AgencySpecification.hasEndDate(agencyOrders.getEndDate()) : null)
                    .and((null != agencyOrders.getOrderStatusAO()) ? AgencySpecification.hasAgencyOStatus(agencyOrders.getOrderStatusAO()) : null)
                    .and((null != agencyOrders.getStartDate() && null != agencyOrders.getEndDate())
                            ? AgencySpecification.hasStartEnd(agencyOrders.getStartDate(), agencyOrders.getEndDate()) : null)
                    .and((1000 != agencyOrders.getPaymentMethod()) ? AgencySpecification.hasPaymentMethodOrders(agencyOrders.getPaymentMethod()) : null)
                    .and(!agencyArea.isEmpty() && !agencyArea.equals("null")
                            ? AgencySpecification.hasAgencys(agencyRepository.getAllAgencyAreaId(Long.parseLong(agencyArea)))
                            : null);
            List<AgencyOrderPayment> lst = agencyOrderPaymentRepository.findAll();
            Page<AgencyOrders> agencyOrdersPage = agencyOrdersRepository.findAll(conditions, PageRequest.of(page, size, Sort.by("createdDate").descending()));
            List<AgencyOrdersDTO> lstDTO = new ArrayList<>();
            for (AgencyOrders agencyOrders1 : agencyOrdersPage){
                AgencyOrdersDTO agencyOrdersDTO = new AgencyOrdersDTO();
                long total = 0;
                agencyOrdersDTO.setAgencyOrders(agencyOrders1);
                for (AgencyOrderPayment ap : lst){
                    if (agencyOrders1.getOrderId() == ap.getAgencyOrdersAOP().getOrderId()){
                        total += ap.getAmount();
                        agencyOrdersDTO.setRemainingAmount(total);
                    }
//                    else {
//                        agencyOrdersDTO.setRemainingAmount(0);
//                    }
                }
                lstDTO.add(agencyOrdersDTO);
            }
            Page<AgencyOrdersDTO> ordersDTOPage = new PageImpl<>(lstDTO,PageRequest.of(page, size, Sort.by("createdDate").descending()),lstDTO.size());
            return ordersDTOPage;
        } catch (Exception e) {
            System.err.println("---------" + e + "-------");
            return null;
        }
    }

    @Override
    @Transactional
    public boolean createAgencyOrder(JSONObject data, Principal principal, MultipartFile[] file){
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt =  conn.prepareCall("{ ? = call PK_AGENCY_ORDER.CREATE_ORDER(?,?,?,?,?,?,?,?,?) }");
            csmt.registerOutParameter(1, OracleTypes.VARCHAR);
            csmt.setString(2,principal.getName());
            csmt.setLong(3,data.getLong("agencyAO"));

            String orderPay = data.getString("orderPay").trim();
            orderPay = orderPay.replaceAll("[^a-zA-Z0-9]", "");
            csmt.setLong(4,Long.parseLong(orderPay));

            String orderValue = data.getString("orderValue").trim();
            orderValue = orderValue.replaceAll("[^a-zA-Z0-9]", "");
            csmt.setLong(5,Long.parseLong(orderValue));

            csmt.setInt(6, data.getInt("paymentMethod"));
            SystemAttr systemAttr = systemAttrRepository.findSystemAttrbyTypeName("AGENCY_ORDERS","SKIP_PAYMENT_ORDER");
            if(systemAttr.getValue().equals("TRUE"))
                csmt.setLong(7, ConstantStatusOrder.PAID);
            else
                csmt.setLong(7, ConstantStatusOrder.NEW_CREATED);
            csmt.setString(8,data.getString("startDate").trim());
            csmt.setString(9,data.getString("endDate").trim());
            csmt.setString(10,data.getString("description").trim());
            csmt.execute();
            String orderId = csmt.getString(1);
            if (!data.getJSONArray("serviceRequest").isEmpty()){
                for (Object ao : data.getJSONArray("serviceRequest")){
                    if (!orderId.isEmpty()) {
                        AgencyOrderRequest orderRequest = new AgencyOrderRequest();
                        orderRequest.setAgencyOrders(agencyOrdersRepository.findByOrderId(Long.parseLong(orderId)));
                        orderRequest.setServiceRequest(serviceRequestRepository.getOne(Long.parseLong(ao.toString())));
                        orderRequestRepository.saveAndFlush(orderRequest);
                    }
                }
            }
//          insert file
            CallableStatement insertFile = conn.prepareCall("{ ? = call PK_AGENCY_ORDER.INSERT_ATTACHMENT(?,?,?,?)}");
            ArrayList<String> returnListStr = new ArrayList<>();
            returnListStr = uploadFile(file);
            int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
            for (int i = 0; i < totalFileDeposits; i++) {
                try {
                    insertFile.registerOutParameter(1,OracleTypes.VARCHAR);
                    insertFile.setString(2,principal.getName());
                    if(Long.parseLong(orderId) > 0){
                        insertFile.setLong(3,Long.parseLong(orderId));
                    }else {
                        return false;
                    }

                    insertFile.setString(4,returnListStr.get(i));
                    insertFile.setLong(5,0);
                    insertFile.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("============>" + e);
            return false;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional
    public boolean updateAgencyOrder(JSONObject data, Principal principal, MultipartFile[] file) {
        AgencyOrders agencyOrders = new AgencyOrders();
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            if (!data.getString("orderId").isEmpty()) {
                agencyOrders = agencyOrdersRepository.findByOrderId(data.getLong("orderId"));
            }
            if (!data.getString("paymentMethod").isEmpty()) {
                agencyOrders.setPaymentMethod(data.getInt("paymentMethod"));
            }
            if (!data.getString("orderValue").isEmpty()) {
                String orderValue = data.getString("orderValue");
                orderValue = orderValue.replaceAll("[^a-zA-Z0-9]", "");
                agencyOrders.setOrderValue(Long.parseLong(orderValue));
            }
            if (!data.getString("orderPay").isEmpty()) {
                String orderPay = data.getString("orderPay");
                orderPay = orderPay.replaceAll("[^a-zA-Z0-9]", "");
                agencyOrders.setOrderPay(Long.parseLong(orderPay));
            }
            if (!data.getString("startDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
                agencyOrders.setStartDate(startDate);
            }
            if (!data.getString("endDate").isEmpty()) {
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
                agencyOrders.setEndDate(endDate);
            }
            if (!data.getJSONArray("serviceRequest").isEmpty()){
                // tìm agencyOrderService theo ServiceRequest id
                AgencyOrderRequest agencyOrderRequest = new AgencyOrderRequest();
                for (Object ao : data.getJSONArray("serviceRequest")){
                    if (!ao.toString().isEmpty()) {
                        // chỉnh sửa agencyOrder theo JSONAray rồi lưu
                        List<Long> longList = orderRequestRepository.getListIdAgencyOrderRequest(Long.parseLong(ao.toString()));
                        for (Long id : longList){
                            agencyOrderRequest = orderRequestRepository.getById(id,agencyOrders.getOrderId());
                            agencyOrderRequest.setServiceRequest(serviceRequestRepository.getOne(Long.parseLong(ao.toString())));
                            agencyOrderRequest.setAgencyOrders(agencyOrdersRepository.findByOrderId(id));
                            orderRequestRepository.saveAndFlush(agencyOrderRequest);
                        }
                    }
                }
            }
            agencyOrders.setDescription(data.getString("description"));
            agencyOrders.setUpdatedBy(principal.getName());
            agencyOrders.setUpdatedDate(date);
            agencyOrdersRepository.saveAndFlush(agencyOrders);
            ArrayList<String> returnListStr = new ArrayList<>();
            returnListStr = uploadFile(file);
            CallableStatement insertFile = conn.prepareCall("{ ? = call PK_AGENCY_ORDER.INSERT_ATTACHMENT(?,?,?,?)}");
            int totalFileDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));
            for (int i = 0; i < totalFileDeposits; i++) {
                try {
                    insertFile.registerOutParameter(1,OracleTypes.VARCHAR);
                    insertFile.setString(2,principal.getName());
                    if(agencyOrders.getOrderId() > 0){
                        insertFile.setLong(3,agencyOrders.getOrderId());
                    }else {
                        return false;
                    }
                    insertFile.setString(4,returnListStr.get(i));
                    insertFile.setLong(5,0);
                    insertFile.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public AgencyOrders findAgencyOrdersById(Long id) {
        Optional<AgencyOrders> optAgencyOrders = agencyOrdersRepository.findById(id);
        AgencyOrders agencyOrders = null;
        if (optAgencyOrders.isPresent()) {
            agencyOrders = optAgencyOrders.get();
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng " + id);
        }
        return agencyOrders;
    }

    @Override
    public AgencyOrders getOne(Long id) {
        return agencyOrdersRepository.getOne(id);
    }

    @Override
    public void deleteById(Long id) {
        List<AgencyOrderRequest> lst =  orderRequestRepository.getARByOrderId(id);
        List<AgencyOrderPayment> lstP = agencyOrderPaymentRepository.listAgencyOderPayment(id);
        this.orderRequestRepository.deleteInBatch(lst);
        this.agencyOrderPaymentRepository.deleteInBatch(lstP);
        this.agencyOrdersRepository.deleteById(id);
    }
//status 0: Mới tạo, 1:Chờ duyệt, 2:Đã duyệt, 3:Đã kích hoạt, 4:Hết hạn, 5:Chờ thanh toán, 6: Đã thanh toán, 7: Xuất hóa đơn, 8: Hoàn thành, 9:Hủy
    @Override
    public boolean changeStatus(Long id,Long status, Principal principal) {
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt = conn.prepareCall("{ ? = call PK_AGENCY_ORDER.UPDATE_ORDER_STATUS(?,?,?)}");
            csmt.registerOutParameter(1, OracleTypes.VARCHAR);
            csmt.setString(2,principal.getName());
            csmt.setLong(3,id);
            csmt.setLong(4,status);
            csmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public DiscountCommission discountRate(String id,long orderValue) {
        try {
            DiscountCommission discountCommission = new DiscountCommission();
            AgencyDiscountCommission agencyDC = agencyDCRepository.getAgencyDiscount(Long.parseLong(id));
            if (agencyDC != null){
                if (agencyDC.getType() == 0){
                    DiscountCommission dc = dcRepo.findById(agencyDC.getAgencyAD().getId());
                    return dc;
                  //  rate =  dc.getRate();
                }
                if (agencyDC.getType() == 1){
                    List<LimitDiscount> agencyDiscount = lmRepo.getLimitByDiscount(agencyDC.getDiscountPolicyAD().getId());
                    if (orderValue != 0){
                        for (LimitDiscount ld : agencyDiscount){
                            if (ld.getMinOrder() <= orderValue && ld.getLimitOrder() >= orderValue){
                                discountCommission.setRate(ld.getDiscountRate());
                                discountCommission.setType(1);
                            }
                        }
                    }else {
                        discountCommission.setRate(0);
                        discountCommission.setType(1);
                    }
                }
            }
            return discountCommission;
        }catch (Exception e){
            System.err.println("Loi lay ti le chinh sach "+e);
            return null;
        }
    }

//    AGENCY ORDER PAYMENT
    @Override
    public boolean createPayment(JSONObject data, Principal principal) {
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt = conn.prepareCall("{? = call PK_AGENCY_ORDER.PAYMENT_CONFIRM(?,?,?,?,?,?,?)}");
            csmt.registerOutParameter(1,OracleTypes.VARCHAR);
            csmt.setString(2,principal.getName());
            if (!data.getString("orderId").isEmpty()) {
                csmt.setLong(3,data.getLong("orderId"));
            }
            if (!data.getString("amount").isEmpty()) {
                String amount = data.getString("amount");
                amount = amount.replaceAll("[^a-zA-Z0-9]", "");
                csmt.setLong(4,Long.parseLong(amount));
            }
            if (!data.getString("paymentMethod").isEmpty()) {
                csmt.setString(5,data.getString("paymentMethod"));
            }

            if (!data.getString("receiverName").isEmpty()) {
                csmt.setString(6,data.getString("receiverName"));
            }
            if (!data.getString("receiverDate").isEmpty()) {
                csmt.setString(7,data.getString("receiverDate"));
            }
            csmt.setString(8,data.getString("description"));
            csmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public Page<AgencyOrderPayment> searchPayment(AgencyOrderPayment payment, int page, int size) {
        try {
            Specification conditions = Specification
                    .where(null != payment.getAgencyOrdersAOP() ? AgencySpecification.hasAgencyOrderId(payment.getAgencyOrdersAOP()) : null)
                    .and(!payment.getPaymentMethod().isEmpty() ? AgencySpecification.hasPaymentMethod(payment.getPaymentMethod()) : null)
                    .and(0 != payment.getAmount() ? AgencySpecification.hasAmount(payment.getAmount()) : null)
                    .and(!payment.getReceiverName().isEmpty() ? AgencySpecification.hasReceiverName(payment.getReceiverName()) : null)
                    .and(null != payment.getReceiverTime() ? AgencySpecification.hasReceiverTime(payment.getReceiverTime()) : null);
            Page<AgencyOrderPayment> paymentsPage = agencyOrderPaymentRepository.findAll(conditions, PageRequest.of(page, size));
            return paymentsPage;
        } catch (Exception e) {
            System.err.println("---------" + e + "-------");
            return null;
        }
    }

//    lấy giá trị bảo lãnh còn hiệu lực của đại lý
    @Override
    public Deposits getDepositAmount(long agencyId) {
        try {
            Deposits deposits = new Deposits();
            List<Deposits> lstDeposit = depositeRepository.getDepositByAgencyId(agencyId);
            if(lstDeposit != null){
                for (Deposits d : lstDeposit){
                    if (d.getStartDate().before(date) && d.getEndDate().after(date)){
                        deposits = d;
                    }
                }
            }else {
                deposits.setDepositsAmount("0");
            }
            return deposits;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public List<AgencyOrderRequestDTO> getSrByAgencyCode(String agencyCode) {
        try {
            Agency agency = new Agency();
            List<AgencyOrderRequestDTO> lstAORequest = new ArrayList<>();
            if (agencyCode != null){
                agency = agencyRepository.findById(Long.parseLong(agencyCode));
            }
            List<Customer> listIdCustomer = serviceRequestRepository.getCustomerByAgencyCode(agency.getAgencyCode());
            List<ServiceRequest> lstServiceRequest =  serviceRequestRepository.getServiceRequestByCustomer(listIdCustomer);
            if(listIdCustomer != null && !listIdCustomer.isEmpty()){
                for (ServiceRequest sr : lstServiceRequest){
                    AgencyOrderRequestDTO agencyOrderRequestDTO = new AgencyOrderRequestDTO();
                    agencyOrderRequestDTO = agencyOrderRequestDTO.setValueDTO(sr);
                    AgencyOrderRequest agencyOrderRequest = serviceRequestRepository.getAgencyOrderRequestBySrId(sr.getSrId());
//                    set type : 0 là chưa đưuọc sử dụng, 1 là đã được sử dụng
                    if (agencyOrderRequest == null){
                        agencyOrderRequestDTO.setType(0);
                    }else {
                        agencyOrderRequestDTO.setType(1);
                    }
                    lstAORequest.add(agencyOrderRequestDTO);
                }
                return lstAORequest;
            }else {
                return null;
            }

        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    @Override
    public List<AgencyOrderRequest> orderRequestDetail(Long orderId) {
        List<AgencyOrderRequest> lst = orderRequestRepository.getARByOrderId(orderId);
        for (AgencyOrderRequest e : lst){
            System.out.println(e.toString());
        }
        System.err.println(orderRequestRepository.getARByOrderId(orderId));
        return orderRequestRepository.getARByOrderId(orderId);
    }

    @Override
    public List<AgencyOrderPayment> orderPaymentDetail(Long orderId) {
        return agencyOrderPaymentRepository.listAgencyOderPayment(orderId);
    }

    @Override
    public List<LogAPI> searchApiLogOrder(Long order) {
        return apiLogRepository.searchApiLogOrder(order);
    }

//    @Override
//    public AgencyOrdersDetailDTO orderDetail(long orderId) {
//        try {
//            AgencyOrders agencyOrders = agencyOrdersRepository.findById(orderId).get();
//            List<AgencyOrderPayment> lstOrderP = agencyOrdersRepository.listAgencyOderPayment(orderId);
//            List<AgencyOrderRequest> lstOrderR = agencyOrdersRepository.listAgencyOderRequest(orderId);
//            AgencyOrdersDetailDTO agencyOrdersDetailDTO = new AgencyOrdersDetailDTO();
//            agencyOrdersDetailDTO.setAgencyOrders(agencyOrders);
//            agencyOrdersDetailDTO.setAgencyOrderRequest(lstOrderR);
//            agencyOrdersDetailDTO.setAgencyOrderPayments(lstOrderP);
//            return agencyOrdersDetailDTO;
//        }catch (Exception e){
//            System.err.println(e);
//            return null;
//        }
//    }

    //    upload multi file
    public ArrayList<String> uploadFile(MultipartFile[] file) {
        // Upload file
        // Path to upload file.
        ArrayList<String> returnStr = new ArrayList<String>();
        String fullSavePath = context.getRealPath("") + ConstantSaveFile.SAVE_DIRECTORY_ORDER + File.separator;
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
                returnStr.add(contextPath + "/" + ConstantSaveFile.SAVE_DIRECTORY_ORDER + "/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                upFail++;
            }
        }
        String uploadResultStr = "File đơn hàng đại lý : " + upSuccess + " file" + "," + "Fail upload: "
                + upFail + " file";
        // index = upSuccess + 1
        returnStr.add(uploadResultStr);
        // total file path (imgFilePath) need inserted
        returnStr.add(String.valueOf(upSuccess));
        return returnStr;
    }
}
