package com.neo.vas.service.impl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.neo.vas.domain.*;
import com.neo.vas.repository.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.service.CommissionService;

/**
 * project_name: demo Created_by: thulv time: 17/05/2021
 */
@Service
public class CommissionServiceImpl implements CommissionService {

	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private BrandRepository brandRepository;
	@Autowired
	private ServicesImpl services;
	@Autowired
	private BrandGroupServiceImpl brandGroupServiceImpl;
	@Autowired
	private BrandGroupRepository brandGroupRepository;
	@Autowired
	private AgencyAreaServiceImpl agencyAreaServiceImpl;
	@Autowired
	private DiscountCommissionRepository dcRepo;
	@Autowired
	private AgencyDiscountCommissionRepository acRepo;
	@Autowired
	private BrandCommissionPolicyRepository bcRepo;
	@Autowired
	private AgencyDCHistoryRepository adcHistoryRepo;

	@Override
	public List<DiscountCommission> getALlCommission() {
		return dcRepo.findAll();
	}

    @Override
    public Page<DiscountCommission> searchCommissionData(String policyName, String isDefault, String startDate, String endDate,int type, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
//        search đầy đủ! startDate, endDate
        if ((policyName != null || isDefault != null) &&(!startDate.isEmpty() && !endDate.isEmpty())){
            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                return dcRepo.findByAll(policyName.toLowerCase(),isDefault,start,end,type,pageable);
            }catch (Exception e){
                System.err.println(e);
                return null;
            }
        }
//        search thiếu ngày kết thúc
        if ((policyName != null || isDefault != null) && (!startDate.isEmpty() && endDate.isEmpty())){
            try {
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                return dcRepo.findEndDateIsNull(policyName.toLowerCase(),isDefault,start,type,pageable);
            }catch (Exception e){
                System.err.println(e);
                return null;
            }
        }

//        search thiếu ngày bắt đầu
        if ((policyName != null || isDefault != null) && (startDate.isEmpty() && !endDate.isEmpty())){
            try {
                Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                return dcRepo.findStartDateIsNull(policyName.toLowerCase(),isDefault,end,type,pageable);
            }catch (Exception e){
                System.err.println(e);
                return null;
            }
        }

//        search thiếu date
        //        search thiếu ngày bắt đầu
        if ((policyName != null || isDefault != null) && (startDate.isEmpty() && endDate.isEmpty())){
            try {
                return dcRepo.findStartEndIsNull(policyName.toLowerCase(),isDefault,type,pageable);
            }catch (Exception e){
                System.err.println(e);
                return null;
            }
        }
//        index search
        return dcRepo.getAll(0,pageable);
    }

	@Override
	@Transactional
	public boolean createCommission(JSONObject data, Principal principal) {
		DiscountCommission dc = new DiscountCommission();
		Date date = new Date();
		try {
			if (!data.getString("policyName").isEmpty()) {
				dc.setPolicyName(data.getString("policyName").trim());
			}
			if (!data.getString("startDate").isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
				dc.setStartDate(startDate);
			}
			if (!data.getString("endDate").isEmpty()) {
				Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
				dc.setEndDate(endDate);
			}
			if (!data.getString("commissionRate").isEmpty()) {
				dc.setRate(data.getInt("commissionRate"));
			}
			if(!data.getString("checkDefault").isEmpty()){
				dc.setIsDefault(1);
			}else {
				dc.setIsDefault(0);
			}
			dc.setDescription(data.getString("description").trim());
			dc.setCreatedDate(date);
			dc.setCreatedBy(principal.getName());
			dc.setType(0);

			DiscountCommission newDC = dcRepo.saveAndFlush(dc);
			DiscountCommission cID = dcRepo.findById(newDC.getId()).get();
            if (!data.getJSONArray("am-kam").isEmpty() && !data.getJSONArray("agency").isEmpty()) {
                for (Object amKam : data.getJSONArray("am-kam")) {
					AgencyDiscountCommission newACP = new AgencyDiscountCommission();
                    newACP.setAgencyAD(agencyRepository.getOne(Long.parseLong(amKam.toString())));
                    newACP.setDiscountPolicyAD(cID);
					acRepo.saveAndFlush(newACP);
					//save table history
					historyAgencyDC(Long.parseLong(amKam.toString()),cID.getId(),0,principal.getName(),date);
                }
				for (Object agency : data.getJSONArray("agency")) {
					AgencyDiscountCommission newACP = new AgencyDiscountCommission();
					newACP.setAgencyAD(agencyRepository.getOne(Long.parseLong(agency.toString())));
					newACP.setDiscountPolicyAD(cID);
					acRepo.saveAndFlush(newACP);
					//save table history
					historyAgencyDC(Long.parseLong(agency.toString()),cID.getId(),0,principal.getName(),date);
				}


            }else {
                try {
                    for (Agency u : agencyRepository.getAgencyAmKamNotPolicy()) {
						AgencyDiscountCommission newACP = new AgencyDiscountCommission();
						newACP.setAgencyAD(u);
						newACP.setDiscountPolicyAD(cID);
						newACP.setType(0);
						acRepo.saveAndFlush(newACP);
//						save table history
						historyAgencyDC(u.getId(),cID.getId(),0,principal.getName(),date);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
            if (!data.getJSONArray("brand").isEmpty()) {
                for (Object br : data.getJSONArray("brand")) {
                	BrandCommissionPolicy brandC = new BrandCommissionPolicy();
                    brandC.setBrandBP(brandRepository.getOne(Long.parseLong(br.toString())));
                    brandC.setDiscountCommissionBC(cID);
                    bcRepo.saveAndFlush(brandC);
                }
            } else {
                try {
                    for (Brand brand : brandRepository.findAll()) {
						BrandCommissionPolicy brandC = new BrandCommissionPolicy();
						brandC.setBrandBP(brand);
						brandC.setDiscountCommissionBC(cID);
						bcRepo.saveAndFlush(brandC);
                    }
                } catch (Exception e) {
                    return false;
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
	public boolean updateCommission(JSONObject data, Principal principal) {
		try {
			DiscountCommission dc = new DiscountCommission();
			Date date = new Date();

			if (!data.getString("startDate").isEmpty() && !data.getString("endDate").isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
				Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
				DiscountCommission upDC = dcRepo.getPolicyIsDefault(startDate, endDate,0);
				if (upDC != null){
					upDC.setIsDefault(0);
					dcRepo.saveAndFlush(upDC);
				}
			}
			if (!data.getString("id").isEmpty()) {
				dc = dcRepo.findById(Long.parseLong(data.getString("id")));
			} else {
				return false;
			}
			if (!data.getString("policyName").isEmpty()) {
				dc.setPolicyName(data.getString("policyName").trim());
			}
			if (!data.getString("startDate").isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
				dc.setStartDate(startDate);
			}
			if (!data.getString("endDate").isEmpty()) {
				Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
				dc.setEndDate(endDate);
			}
			if (!data.getString("rate").isEmpty()) {
				dc.setRate(data.getInt("rate"));
			}
			if(!data.getString("checkDefault").isEmpty()){
				dc.setIsDefault(1);
			}
			dc.setDescription(data.getString("description").trim());
			dc.setUpdateDate(date);
			dc.setUpdateBy(principal.getName());
			dcRepo.saveAndFlush(dc);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}

	}

	@Override
	public DiscountCommission getCommissionById(Long id) {
		Optional<DiscountCommission> optcommisionPolicy = dcRepo.findById(id);
		DiscountCommission commisionPolicy = null;
		if (optcommisionPolicy.isPresent()) {
			commisionPolicy = optcommisionPolicy.get();
		} else {
			throw new RuntimeException("Không tìm thấy chính sách hoa hồng " + id);
		}
		return commisionPolicy;
	}

	@Override
	public List<DiscountCommission> getListDCByType(int type) {
		return dcRepo.findByType(type);
	}

	@Override
	public void deleteCommission(Long id) {
		//delete brand Commission
		List<BrandCommissionPolicy> lstBrCP = bcRepo.deleteBrandByCId(id);
		bcRepo.deleteInBatch(lstBrCP);

//		delete agency am/kam commission
		List<AgencyDiscountCommission> lstAgP = acRepo.deleteAgencyDiscount(id);
		acRepo.deleteInBatch(lstAgP);
		this.dcRepo.deleteById(id);
	}

	// get data brandGroup, service where id
	@Override
	public List<BrandGroup> listBrandG(String stringList) {
		List<Services> servicesList = new ArrayList<>();
		String[] listId = stringList.split(",");
		for (String st : listId) {
			try {
				Long id = Long.parseLong(st);
				Services sv = services.getServicesById(id);
				servicesList.add(sv);
			} catch (Exception e) {
				return null;
			}
		}
		List<BrandGroup> a = brandGroupRepository.listBrand(servicesList);
		return a;
	}

	@Override
	public List<Agency> listAmKam(String areaId) {
		List<AgencyArea> lstAmKam = new ArrayList<>();
		List<Agency> lstAgency = new ArrayList<>();
		String[] lstId = areaId.split(",");
		for (String id : lstId) {
			try {
				Long idArea = Long.parseLong(id);
				AgencyArea agencyArea = agencyAreaServiceImpl.getAgencyAreaById(idArea);
				lstAmKam.add(agencyArea);
			} catch (Exception e) {
				return lstAgency;
			}
		}
		lstAgency = agencyRepository.getAgencyByAgencyAreaList(lstAmKam);
		Comparator<Agency> agencyComparator = Comparator.comparing(Agency::getAgencyName);
		lstAgency.sort(agencyComparator);
		return lstAgency;
	}
	@Override
	public List<Brand> listBrand(String stringListId) {
		List<BrandGroup> lstBrandGroup = new ArrayList<>();
		String[] listBrandG = stringListId.split(",");
		for (String strId : listBrandG) {
			try {
				Long idBrandG = Long.parseLong(strId);
				BrandGroup brandG = brandGroupServiceImpl.getBrandGroupById(idBrandG);
				lstBrandGroup.add(brandG);
			} catch (Exception e) {
				return null;
			}
		}
		List<Brand> lstBG = brandRepository.findByBrandGroupBIn(lstBrandGroup);
		Comparator<Brand> brandComparator = Comparator.comparing(Brand::getBrandName);
		lstBG.sort(brandComparator);
		return lstBG;
	}

//	save history agency discount commission
	public void historyAgencyDC(long agencyId, long policyId, int type, String createdBy, Date createdDate){
		AgencyDCHistory agencyDCHistory = new AgencyDCHistory();
		agencyDCHistory.setAgencyId(agencyId);
		agencyDCHistory.setDiscountCommissionId(policyId);
		agencyDCHistory.setType(type);
		agencyDCHistory.setCreatedBy(createdBy);
		agencyDCHistory.setCreatedDate(createdDate);
		adcHistoryRepo.saveAndFlush(agencyDCHistory);
	}

}
