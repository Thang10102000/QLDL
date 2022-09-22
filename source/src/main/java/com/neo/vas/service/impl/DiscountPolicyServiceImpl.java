package com.neo.vas.service.impl;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.neo.vas.domain.*;
import com.neo.vas.repository.AgencyDiscountCommissionRepository;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.repository.LimitDiscountRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.service.DiscountPolicyService;


/**
 * project_name: demo
 * Created_by: thulv
 * time: 19/05/2021
 */
@Service
public class DiscountPolicyServiceImpl implements DiscountPolicyService {
    @Autowired
    private DiscountCommissionRepository dcRepo;
    @Autowired
    private AgencyRepository agencyRepository1;
    @Autowired
    private AgencyAreaServiceImpl agencyAreaServiceImpl;
    @Autowired
    private AgencyDiscountCommissionRepository agencyDiscountRepository;
    @Autowired
    private LimitDiscountRepository limitDiscountRepository;
    @Autowired
    private CommissionServiceImpl commissionService;

    @Override
    public List<DiscountCommission> getAll() {
        return dcRepo.findAll();
    }

    @Override
    public List<DiscountCommission> getAllDiscountPolicy() {
        return dcRepo.findByType(1);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DiscountCommission> searchDiscountPolicy(String policyName,String isDefault, String startDate, String endDate, int page, int size) {

        Pageable pageable = PageRequest.of(page,size);

//        search đầy đủ! startDate, endDate
        if ((policyName != null || isDefault != null) &&(!startDate.isEmpty() && !endDate.isEmpty())){
            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                return dcRepo.findByAll(policyName.toLowerCase(),isDefault,start,end,1,pageable);
            }catch (Exception e){
                System.err.println("search đầy đủ! startDate, endDate "+e);
                return null;
            }
        }
//        search thiếu ngày kết thúc
        if ((policyName != null || isDefault != null) && (!startDate.isEmpty() && endDate.isEmpty())){
            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                return dcRepo.findEndDateIsNull(policyName.toLowerCase(),isDefault,start,1,pageable);
            }catch (Exception e){
                System.err.println("search thiếu ngày kết thúc" + e);
                return null;
            }
        }
//        search thiếu ngày bắt đầu
        if ((policyName != null || isDefault != null) && (startDate.isEmpty() && !endDate.isEmpty())){
            try {
                Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                return dcRepo.findStartDateIsNull(policyName.toLowerCase(),isDefault,end,1,pageable);
            }catch (Exception e){
                System.err.println("search thiếu ngày bắt đầu "+ e);
                return null;
            }
        }
        //        search thiếu ngày bắt đầu
        if ((policyName != null || isDefault != null) && (startDate.isEmpty() && endDate.isEmpty())){
            try {
                return dcRepo.findStartEndIsNull(policyName.toLowerCase(),isDefault,1,pageable);
            }catch (Exception e){
                System.err.println("search thiếu ngày bắt đầu "+e);
                return null;
            }
        }
//        index search
        return dcRepo.getAll(1,pageable);
    }

    @Override
    @Transactional
    public boolean createDiscountPolicy(JSONObject data, Principal principal) {
        DiscountCommission discountPolicy = new DiscountCommission();
        Date date = new Date();
        try {
            if (!data.getString("policyName").isEmpty()) {
                discountPolicy.setPolicyName(data.getString("policyName").trim());
            }
            if (!data.getString("startDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate").trim());
                discountPolicy.setStartDate(startDate);
            }
            if (!data.getString("endDate").isEmpty()) {
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate").trim());
                discountPolicy.setEndDate(endDate);
            }
            if(!data.getString("checkDefault").isEmpty()){
                discountPolicy.setIsDefault(1);
            }else {
                discountPolicy.setIsDefault(0);
            }
            discountPolicy.setDescription(data.getString("description").trim());
            discountPolicy.setCreatedDate(date);
            discountPolicy.setCreatedBy(principal.getName());
            discountPolicy.setType(1);
            DiscountCommission dc = dcRepo.saveAndFlush(discountPolicy);
            DiscountCommission discountId = dcRepo.findById(dc.getId()).get();

//            save default minOrder, limitOrder, discountRate
            LimitDiscount limitDiscount = new LimitDiscount();
            limitDiscount.setDiscountCommission(discountId);
            if (!data.getString("discountRate").isEmpty()) {
                limitDiscount.setDiscountRate(Integer.parseInt(data.getString("discountRate").trim()));
            }
            if (!data.getString("minOrder").isEmpty()) {
                String minOrder = data.getString("minOrder").trim();
                minOrder = minOrder.replaceAll("[^a-zA-Z0-9]", "");
                limitDiscount.setMinOrder(Long.parseLong(minOrder));
            }
            if (!data.getString("limitOrder").isEmpty()) {
                String limitOrder = data.getString("limitOrder").trim();
                limitOrder = limitOrder.replaceAll("[^a-zA-Z0-9]", "");
                limitDiscount.setLimitOrder(Long.parseLong(limitOrder));
            }
            limitDiscountRepository.saveAndFlush(limitDiscount);

//           save minOrder, limitOrder, discountRate
            if (!data.getString("lengthArr").isEmpty()){
                for (int i = 1; i< data.getInt("lengthArr") ; i++){
                    LimitDiscount ld = new LimitDiscount();
                    ld.setDiscountCommission(discountId);
                    ld.setCreatedBy(principal.getName());
                    ld.setCreatedDate(new Date());
                    if (data.has("discountRate" + i) && !data.getString("discountRate" + i).trim().isEmpty()) {
                        ld.setDiscountRate(Integer.parseInt(data.getString("discountRate" + i).trim()));
                    }
                    if (data.has("minOrder" + i) && !data.getString("minOrder" + i).isEmpty()) {
                        String minOrder = data.getString("minOrder" + i).trim();
                        minOrder = minOrder.replaceAll("[^a-zA-Z0-9]", "");
                        ld.setMinOrder(Long.parseLong(minOrder));
                    }
                    if (data.has("limitOrder" + i) && !data.getString("limitOrder" + i).isEmpty()) {
                        String limitOrder = data.getString("limitOrder" + i).trim();
                        limitOrder = limitOrder.replaceAll("[^a-zA-Z0-9]", "");
                        ld.setLimitOrder(Long.parseLong(limitOrder));
                    }
                    limitDiscountRepository.saveAndFlush(ld);

                }
            }

//            save agency of discount policy
            if (!data.getJSONArray("agency").isEmpty()) {
                for (Object ag : data.getJSONArray("agency")) {
                    AgencyDiscountCommission agencyD = new AgencyDiscountCommission();
                    agencyD.setAgencyAD(agencyRepository1.getOne(Long.parseLong(ag.toString())));
                    agencyD.setDiscountPolicyAD(discountId);
                    agencyD.setType(1);
                    agencyDiscountRepository.saveAndFlush(agencyD);
                }
            } else {
                try {
                    for (Agency ag : agencyRepository1.getAgencyNotPolicy()) {
                        AgencyDiscountCommission newAgD = new AgencyDiscountCommission();
                        newAgD.setAgencyAD(ag);
                        newAgD.setDiscountPolicyAD(discountId);
                        agencyDiscountRepository.saveAndFlush(newAgD);
                        commissionService.historyAgencyDC(ag.getId(),discountId.getId(),1,principal.getName(),date);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


    @Override
    @Transactional
    public boolean updateDiscountPolicy(JSONObject data, Principal principal) {
        Date date = new Date();
        System.err.println("discount " + data);
        DiscountCommission discountPolicy = new DiscountCommission();
        try {
            if (!data.getString("startDate").isEmpty() && !data.getString("endDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
                DiscountCommission upDC = dcRepo.getPolicyIsDefault(startDate, endDate,1);
                if (upDC != null){
                    upDC.setIsDefault(0);
                    dcRepo.saveAndFlush(upDC);
                }
            }

            if (!data.getString("id").isEmpty()) {
                try {
                    discountPolicy = dcRepo.findById(Long.parseLong(data.getString("id")));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("policyName").isEmpty()) {
                discountPolicy.setPolicyName(data.getString("policyName").trim());
            }
            if (!data.getString("startDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate").trim());
                discountPolicy.setStartDate(startDate);

            }
            if (!data.getString("endDate").isEmpty()) {
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate").trim());
                discountPolicy.setEndDate(endDate);
            }
            if (data.has("isDefault")){
                discountPolicy.setIsDefault(1);
            }
            discountPolicy.setDescription(data.getString("description").trim());
            discountPolicy.setUpdateDate(date);
            discountPolicy.setUpdateBy(principal.getName());
            dcRepo.saveAndFlush(discountPolicy);

//            save edit limit order

            if (!data.getString("sizeLimit").isEmpty()){
                if (!data.getJSONArray("totalIdLimit").isEmpty()) {
                    for (Object ag : data.getJSONArray("totalIdLimit")) {
                        Long limitId = Long.parseLong(ag.toString());
                        limitDiscountRepository.deleteById(limitId);
                    }
                }
                for (int i = 1; i< data.getInt("sizeLimit") + 1; i++){
                    LimitDiscount limitDiscount = new LimitDiscount();
                    if (data.has("idLimit" + i) && !data.getString("idLimit" +i).isEmpty()){
                        limitDiscount = limitDiscountRepository.findById(Long.parseLong(data.getString("idLimit"+i))).get();
                    }
                    if (data.has("discountRate" + i) && !data.getString("discountRate" + i).isEmpty()) {
                        limitDiscount.setDiscountRate(Integer.parseInt(data.getString("discountRate" + i).trim()));
                    }
                    if (data.has("minOrder" + i) && !data.getString("minOrder" + i).isEmpty()) {
                        String minOrder = data.getString("minOrder" + i).trim();
                        minOrder = minOrder.replaceAll("[^a-zA-Z0-9]", "");
                        limitDiscount.setMinOrder(Long.parseLong(minOrder));
                    }
                    if (data.has("limitOrder" + i) && !data.getString("limitOrder" + i).isEmpty()) {
                        String limitOrder = data.getString("limitOrder" + i).trim();
                        limitOrder = limitOrder.replaceAll("[^a-zA-Z0-9]", "");
                        limitDiscount.setLimitOrder(Long.parseLong(limitOrder));
                    }
                    limitDiscount.setUpdateBy(principal.getName());
                    limitDiscount.setUpdateDate(new Date());
                    limitDiscountRepository.saveAndFlush(limitDiscount);
                }
            }

//           save minOrder, limitOrder, discountRate
            if (!data.getString("lengthArr").isEmpty() && data.getInt("lengthArr") > data.getInt("sizeLimit")){
                for (int i =  data.getInt("sizeLimit")+1 ; i< data.getInt("lengthArr") ; i++){
                    LimitDiscount ld = new LimitDiscount();
                    ld.setDiscountCommission(discountPolicy);
                    ld.setCreatedBy(principal.getName());
                    ld.setCreatedDate(new Date());
                    if (data.has("discountRate" + i) && !data.getString("discountRate" + i).isEmpty()) {
                        ld.setDiscountRate(Integer.parseInt(data.getString("discountRate" + i).trim()));
                    }
                    if (data.has("minOrder" + i) && !data.getString("minOrder" + i).isEmpty()) {
                        String minOrder = data.getString("minOrder" + i).trim();
                        minOrder = minOrder.replaceAll("[^a-zA-Z0-9]", "");
                        ld.setMinOrder(Long.parseLong(minOrder));
                    }
                    if (data.has("limitOrder" + i) && !data.getString("limitOrder" + i).isEmpty()) {
                        String limitOrder = data.getString("limitOrder" + i).trim();
                        limitOrder = limitOrder.replaceAll("[^a-zA-Z0-9]", "");
                        ld.setLimitOrder(Long.parseLong(limitOrder));
                    }
                    limitDiscountRepository.saveAndFlush(ld);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public DiscountCommission getDiscountPolicyById(Long id) {
        Optional<DiscountCommission> optDiscount = dcRepo.findById(id);
        DiscountCommission discountPolicy = null;
        if (optDiscount.isPresent()) {
            discountPolicy = optDiscount.get();
        } else {
            throw new RuntimeException("Không tìm thấy chính sách chiết khấu " + id);
        }
        return discountPolicy;
    }

    @Override
    public void deleteDiscountPolicyById(Long id) {
        List<AgencyDiscountCommission> deAD = agencyDiscountRepository.deleteAgencyDiscount(id);
        List<LimitDiscount> deLD = limitDiscountRepository.getLimitByDiscount(id);
        agencyDiscountRepository.deleteInBatch(deAD);
        limitDiscountRepository.deleteInBatch(deLD);
        this.dcRepo.deleteById(id);
    }

    @Override
    public List<DiscountCommission> getCommission() {
        return dcRepo.getCommission();
    }


    @Override
    public List<Agency> getListAgency(String id) {

        List<AgencyArea> lstt = new ArrayList<>();
        String[] lstArea = id.split(",");
        for (String st : lstArea) {
            try {
                System.out.println(st);
                Long idArea = Long.parseLong(st);
                AgencyArea agencyArea = agencyAreaServiceImpl.getAgencyAreaById(idArea);
                lstt.add(agencyArea);
            } catch (Exception e) {
                System.err.println(e);
                return null;
            }
        }
        List<Agency> lstAg = agencyRepository1.getAgencyByArea(lstt);
        System.out.println(lstAg);
        return lstAg;
    }

//    lấy danh sách đại lý chưa thuộc chính sách
    @Override
    public List<Agency> getListAgencyNoPolicy(String id) {

        List<AgencyArea> lstt = new ArrayList<>();
        String[] lstArea = id.split(",");
        for (String st : lstArea) {
            try {
                System.out.println(st);
                Long idArea = Long.parseLong(st);
                AgencyArea agencyArea = agencyAreaServiceImpl.getAgencyAreaById(idArea);
                lstt.add(agencyArea);
            } catch (Exception e) {
                System.err.println(e);
                return null;
            }
        }
        List<Agency> lstAg = agencyRepository1.getAgencyByAreaNotPolicy(lstt);
        System.out.println(lstAg);
        return lstAg;
    }

    @Override
    public DiscountCommission getPolicyIsDefault(Date startDate, Date endDate, int type) {
        return dcRepo.getPolicyIsDefault(startDate, endDate,type);
    }

    @Override
    public DiscountCommission findDiscountEffect(Date now) {
        return dcRepo.findDiscountEffect(now);
    }

    @Override
    public List<DiscountCommission> findCommissionEffect(Date now) {
        return dcRepo.findCommissionEffect(now);
    }

    @Override
    public List<Agency> getListAgencyAmkam(String id) {

        List<AgencyArea> lstt = new ArrayList<>();
        String[] lstArea = id.split(",");
        for (String st : lstArea) {
            try {
                System.out.println(st);
                Long idArea = Long.parseLong(st);
                AgencyArea agencyArea = agencyAreaServiceImpl.getAgencyAreaById(idArea);
                lstt.add(agencyArea);
            } catch (Exception e) {
                return null;
            }
        }
        List<Agency> lstAg = agencyRepository1.getAgencyAmkamByArea(lstt);
        System.out.println(lstAg);
        return lstAg;
    }

}
