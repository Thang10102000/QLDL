package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.constant.ConstantStatusOrder;
import com.neo.vas.domain.*;
import com.neo.vas.dto.AgencyOrderRequestDTO;
import com.neo.vas.repository.*;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.AgencyOrderRequestService;
import com.neo.vas.service.AgencyOrdersService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.impl.AgencyOrderPaymentServiceImpl;
import com.neo.vas.service.impl.AgencyServiceImpl;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 28/05/2021
 */
@Controller
public class AgencyOrdersController {

    @Autowired
    private AgencyOrdersService agencyOrdersService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyAreaService agencyAreaService;
    @Autowired
    private OrdersStatusService ordersStatusService;
    @Autowired
    private AgencyOrderPaymentService agencyOrderPaymentService;
    @Autowired
    private FilesAgencyOrderService filesAgencyOrderService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private GuaranteePolicyService gpService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AgencyOrderRequestService agencyOrderRequestService;
    @Autowired
    private WardService wardService;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ProvinceRepository provinceRepository;

    @GetMapping("/agency-order")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    public ModelAndView getAllAgencyOrder(Model model, Principal principal, RedirectAttributes reDir) {
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
        }else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
            Long agencyId = user.getAgencyId();
            Agency agency = agencyService.getAgencyById(agencyId);
            if (agency != null){
                model.addAttribute("agency",agency);
                model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
            }else {
                model.addAttribute("agency",null);
                model.addAttribute("agencyArea",null);
            }
        }else {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        }
        reDir.addFlashAttribute("levelUser",user.getLevelUsers().getLevelId());
        model.addAttribute("orderStatus", ordersStatusService.getAll());
        ModelAndView modelAndView = new ModelAndView("AgencyOrders/agency_order");
        modelAndView.addObject("page", 1);
        modelAndView.addObject("size", 5);
        return modelAndView;
    }

    @GetMapping("/new-agency-order")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thêm')")
    public String newAgencyOrder(Model model, Principal principal) {
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
        }else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
            Long agencyId = user.getAgencyId();
            Agency agency = agencyService.getAgencyById(agencyId);
            if (agency != null){
                model.addAttribute("agency",agency);
                model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
            }else {
                model.addAttribute("agency",null);
                model.addAttribute("agencyArea",null);
            }
        }else {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        }
        model.addAttribute("orderStatus", ordersStatusService.getAll());
        model.addAttribute("guarantee", gpService.getLimit());
        return "AgencyOrders/new_agency_order";
    }

    @GetMapping("/edit-agency-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Sửa')")
    public ModelAndView editAgencyOrder(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("agency", agencyService.getAllAgency());
        model.addAttribute("orderStatus", ordersStatusService.getAll());
        model.addAttribute("guarantee", gpService.getLimit());
        model.addAttribute("files", filesAgencyOrderService.getAllByOrderId(id));
        AgencyOrders agencyOrders = agencyOrdersService.findAgencyOrdersById(id);
        List<ServiceRequest> requestList = agencyOrderRequestService.getServiceRequestByAoId(id);
        System.out.println(requestList);
        model.addAttribute("serviceRequest", requestList);
        return new ModelAndView("AgencyOrders/edit_agency_order", "agencyOrder", agencyOrders);
    }

    @GetMapping("/view-agency-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    public ModelAndView viewAgencyOrder(@PathVariable(value = "id") Long id, Model model) {

        model.addAttribute("agency", agencyService.getAllAgency());
        model.addAttribute("orderStatus", ordersStatusService.getAll());
        model.addAttribute("guarantee", gpService.getLimit());
        model.addAttribute("files", filesAgencyOrderService.getAllByOrderId(id));
        model.addAttribute("agencyOrderRequest",agencyOrdersService.orderRequestDetail(id));
        model.addAttribute("agencyOrderPayments",agencyOrdersService.orderPaymentDetail(id));
        AgencyOrders agencyOrders = agencyOrdersService.findAgencyOrdersById(id);
        return new ModelAndView("AgencyOrders/view_agency_order", "agencyOrder", agencyOrders);
    }

    @GetMapping("/agency-order-call-transaction/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView viewAgencyOrderTransaction(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("agencyOrderRequest",agencyOrdersService.orderRequestDetail(id));
        model.addAttribute("agencyOrderPayments",agencyOrdersService.orderPaymentDetail(id));
        AgencyOrders agencyOrders = agencyOrdersService.findAgencyOrdersById(id);
        if (agencyOrders.getAgencyAO().getType() == 0){
//            set code bằng null khi đơn hàng là đơn hàng của đại lý
            agencyOrders.getAgencyAO().setShopCode(agencyOrders.getAgencyAO().getAgencyCode());
            agencyOrders.getAgencyAO().setAgencyCode(null);
        }
        if (agencyOrders != null){
//            lấy thông tin khách hàng từ dơn hàng
            List<ServiceRequest> lstRequest = agencyOrderRequestService.getServiceRequestByAoId(agencyOrders.getOrderId());
            ServiceRequest serviceRequest = lstRequest.get(0);
            Agency agency = agencyService.getAgencyByCode(serviceRequest.getCustomer().getAgencyCode());
            model.addAttribute("address",serviceRequest.getCustomer().getAddress() + ", "+ wardService.findWardById(Long.parseLong(serviceRequest.getCustomer().getWards())).getWardName() +", "+
                    districtRepository.findDistrictById(Long.parseLong(serviceRequest.getCustomer().getDistrict())).getDistrictName() +", " +
                    provinceRepository.findProvinceById(Long.parseLong(serviceRequest.getCustomer().getProvince())).getProvince_name());
            model.addAttribute("lstRequest",serviceRequest);
            model.addAttribute("customerAgency",agency);
            model.addAttribute("payment",agencyOrderPaymentService.getPaymentByOrderId(agencyOrders.getOrderId()));
        }
        return new ModelAndView("AgencyOrders/export_invoice", "agencyOrder", agencyOrders);
    }

    @GetMapping("/delete-agency-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xoá')")
    public ModelAndView deleteAgencyOrder(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.deleteById(id);
            insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.DELETE,
                    principal.getName() + " delete agency order success");
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    nút chuyển trạng thái -> chờ duyệt
    @GetMapping("/agency-order-status/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Sửa')")
    public ModelAndView changeStatus(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.PENDING, principal);
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    nút chuyển trạng thái -> chờ thanh toán
    @GetMapping("/wait-pay-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Sửa')")
    public ModelAndView waitForPay(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.WAIT_PAY, principal);
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    nút chuyển trạng thái -> đã kích hoạt
    @GetMapping("/active-agency-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView changeStatusActive(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.ACTIVATED, principal);
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //nút chuyển trạng thái -> đã hủy
    @GetMapping("/cancel-agency-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView changeStatusCancel(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.CANCELLED, principal);
            System.out.println("check: "+this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.CANCELLED, principal));
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }


    //    nút chuyển trạng thái -> đã hoàn thành
    @GetMapping("/call-api-success/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView changeStatusSuccess(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.agencyOrdersService.changeStatus(id, ConstantStatusOrder.COMPLETED, principal);
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/agency-order");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    lấy tỉ lệ chiết khấu, hoa hồng đại lý
    @GetMapping("/discount-rate")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    @ResponseBody
    public DiscountCommission discountRate(Model model, @RequestParam String agencyAO, String orderValue) {
        try {
            Long orderV = null;
            if (orderValue.isEmpty()) {
                orderV = Long.parseLong(orderValue);
            } else {
                orderV = Long.parseLong("0");
            }
            DiscountCommission discount = agencyOrdersService.discountRate(agencyAO, orderV);
            model.addAttribute("discountRate", discount);
            return discount;
        } catch (Exception e) {
            return null;
        }
    }

    //    lấy giá trị bảo lãnh đặt cọc của đại lý
    @GetMapping("/get-deposit-amount")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    @ResponseBody
    public Deposits getDepositAmount(@RequestParam String agencyAO) {
        try {

            if (!agencyAO.isEmpty()) {
                return agencyOrdersService.getDepositAmount(Long.parseLong(agencyAO));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    //    Xác nhận thanh toán
    @GetMapping("/pay-confirm/{id}/{orderPay}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")

    public ModelAndView payConfirm(@PathVariable(value = "id") Long id, @PathVariable(value = "orderPay") Long orderPay, Model model) {
        System.out.println("Payyyyyyyyyyyyyyy");
        AgencyOrders agencyOrders = agencyOrdersService.findAgencyOrdersById(id);
        model.addAttribute("pay_confirm", agencyOrderPaymentService.getAll());
        if (agencyOrders.getCurrentValue() > 0){
            model.addAttribute("orderPay", agencyOrders.getCurrentValue());
        }else {
            model.addAttribute("orderPay", orderPay);
        }
        return new ModelAndView("AgencyOrders/pay_confirm", "payConfirm", agencyOrders);
    }

    //    Xác nhận thanh toán
    @GetMapping("/pay-confirm-update/{id}/{orderPay}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")

    public ModelAndView updatePayConfirm(@PathVariable(value = "id") Long id, @PathVariable(value = "orderPay") Long orderPay, Model model) {
        AgencyOrders agencyOrders = agencyOrdersService.findAgencyOrdersById(id);
        System.out.println("update Pay");
        model.addAttribute("pay_confirm", agencyOrderPaymentService.getAll());
//        AgencyOrderPayment orderPayment = agencyOrderPaymentService.getPaymentByOrderId(agencyOrders.getOrderId());
        if (agencyOrders.getCurrentValue() > 0){
            model.addAttribute("orderPay", agencyOrders.getCurrentValue());
        }else {
            model.addAttribute("orderPay", orderPay);
        }
        return new ModelAndView("AgencyOrders/pay_confirm_update", "payConfirm", agencyOrders);
    }

    //    tạo mới
    @PostMapping("/new-agency-order")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thêm')")
    public ModelAndView createAgencyOrder(@RequestParam Map<String, String> reParam, Principal principal,
                                          @RequestParam("files[]") MultipartFile[] file, RedirectAttributes reDir) {
        try {
            JSONObject data = new JSONObject(reParam);

            String[] strings = data.getString("serviceRequestVal").split(Pattern.quote(","));
            JSONArray jsonArrayAo = new JSONArray();
            if (strings[0] != "") {
                for (String s : strings) {
                    jsonArrayAo.put(s);
                }
            }
            data.put("serviceRequest", jsonArrayAo);
            System.out.println(data);

            boolean result = agencyOrdersService.createAgencyOrder(data, principal, file);
            if (result) {
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/agency-order");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/agency-order");
            }
        } catch (Exception e) {
            System.out.println(e);
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    Chỉnh sửa
    @PostMapping("/edit-agency-order")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Sửa')")
    public ModelAndView updateAgencyOrder(@RequestParam Map<String, String> reParam, Principal principal,
                                          @RequestParam("files[]") MultipartFile[] file, RedirectAttributes reDir) {
        try {
            JSONObject data = new JSONObject(reParam);

            String[] strings = data.getString("serviceRequestVal").split(Pattern.quote(","));
            JSONArray jsonArrayAo = new JSONArray();
            if (strings[0] != "") {
                for (String s : strings) {
                    jsonArrayAo.put(s);
                }
            }
            data.put("serviceRequest", jsonArrayAo);
            boolean result = agencyOrdersService.updateAgencyOrder(data, principal, file);
            if (result) {
                insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.EDIT,
                        principal.getName() + " edit payment confirm agency order success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/agency-order");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/agency-order");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    tạo mới xác nhận thanh toán
    @PostMapping("/payment-confirm")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView createPayment(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject json = new JSONObject(reParam);
            boolean result = agencyOrdersService.createPayment(json, principal);

            if (result) {
                this.agencyOrdersService.changeStatus(json.getLong("orderId"), ConstantStatusOrder.PAID, principal);
                insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.CREATE,
                        principal.getName() + " create payment confirm agency order success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/agency-order");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/agency-order");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    tiếp tục xác nhận thanh toán với số tiền còn nợ
    @PostMapping("/payment-confirm-update")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView updatePayment(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject json = new JSONObject(reParam);
            boolean result = agencyOrdersService.createPayment(json, principal);

            if (result) {
                insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.CREATE,
                        principal.getName() + " create payment confirm agency order success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/agency-order");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/agency-order");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/agency-order");
        }
    }

    //    Xoá files đơn hàng đại lý
    @GetMapping("/delete-file-order/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xoá')")
    public ResponseEntity<String> deleteFile(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir) throws SQLException {
        filesAgencyOrderService.deleteById(id);
        insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.DELETE,
                principal.getName() + " delete file agency order");
        reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
        return new ResponseEntity<>(ConstantNotify.SUCCESS, HttpStatus.OK);
    }

    //    get list yêu cầu dịch vụ
    @GetMapping(value = "/service-request-list")
    @ResponseBody
    public List<AgencyOrderRequestDTO> listServiceRequest(Model model, @RequestParam String agencyCode){
        List<AgencyOrderRequestDTO> requestList = agencyOrdersService.getSrByAgencyCode(agencyCode);
        model.addAttribute("listSr", requestList);
        return requestList;
    }

    // chi tiết đẩy hoá đơn sang bhtt
    @GetMapping("/list-order-active/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Thực thi')")
    public ModelAndView getListActivateSr(@PathVariable("requestId") long requestId, Model model, Principal principal) {

        return new ModelAndView("AgencyOrders/order_list_act", "listActive", agencyOrdersService.searchApiLogOrder(requestId));
    }
}
