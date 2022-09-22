package com.neo.vas.service.impl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.AgencyArea;
import com.neo.vas.domain.AuthorizedPerson;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.*;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.Agency;
import com.neo.vas.service.AgencyService;

/**
 * project_name: demo Created_by: thulv time: 21/05/2021
 */
@Service
public class AgencyServiceImpl implements AgencyService {
	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private AgencyAreaRepository agencyAreaRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private LevelsRepository levelsRepository;

	@Autowired
	private InsertLogService insertLogService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private AuthorizedPersonRepository authorizedPersonRepository;

	Date date = new Date();

	@Override
	public List<Agency> getAll() {
		return agencyRepository.findAll();
	}

	@Override
	public List<Agency> getAllAgency() {
		return agencyRepository.findAgencyByType(0);
	}

	@Override
	public List<Agency> getAllAmKam() {
		return agencyRepository.findAgencyByType(1);
	}

	@Override
	public Page<Agency> searchAgency(String agencyName, String areaId, String status, String type, int page, int size) {
		Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
		if (agencyName != null || areaId != null || status != null){
			return agencyRepository.findAllAgency(agencyName,areaId,status,type,pageable);
		}
		return agencyRepository.findAll(pageable);
	}

	@Override
	public Agency getAgencyById(Long id) {
		Optional<Agency> optAgency = agencyRepository.findById(id);
		Agency agency = new Agency();
		if (optAgency.isPresent()) {
			agency = optAgency.get();
		} else {
			throw new RuntimeException("Khong tim thay dai ly " + id);
		}
		return agency;
	}

	@Override
	public Agency getOne(Long id) {
		return agencyRepository.getOne(id);
	}

	@Override
	public boolean  createAgency(JSONObject data, Principal principal) {
		Agency agency = new Agency();
		Users users = new Users();

		try {
			if (!data.getString("agencyName").isEmpty()) {
				agency.setAgencyName(data.getString("agencyName").trim());
			}
			if (!data.getString("agencyArea").isEmpty()) {
				agency.setAreaId(agencyAreaRepository.getOne(Long.parseLong(data.getString("agencyArea"))));
			}
			if (!data.getString("agencyType").isEmpty()) {
				agency.setAgencyType(data.getInt("agencyType"));
			}
			if (!data.getString("status").isEmpty()) {
				agency.setStatus(data.getInt("status"));
			}
			if (!data.getString("businessLicense").isEmpty()) {
				agency.setBusinessLicense(data.getString("businessLicense").trim());
			}
			if (!data.getString("agencyCode").isEmpty()) {
				agency.setAgencyCode(data.getString("agencyCode").trim());
			}
			if (!data.getString("branchAgency").isEmpty()) {
				agency.setBranchAgency(data.getString("branchAgency").trim());
			}
			if (!data.getString("licenseDate").isEmpty()) {
				Date licenseDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("licenseDate").trim());
				agency.setLicenseDate(licenseDate);
			}
			if (!data.getString("licensePlace").isEmpty()) {
				agency.setLicensePlace(data.getString("licensePlace").trim());
			}
			if (!data.getString("taxCode").isEmpty()) {
				agency.setTaxCode(data.getString("taxCode").trim());
			}
			agency.setOfficeAddress(data.getString("officeAddress").trim());
			agency.setInvoiceAddress(data.getString("invoiceAddress").trim());
			agency.setPhoneNumber(data.getString("phoneNumber").trim());
			agency.setEmail(data.getString("email").trim());
			agency.setCreatedBy(principal.getName());
			agency.setType(0);
			agency.setCreatedDate(date);
//			setAgencyCode theo AgencyId
			Agency newAgency = agencyRepository.saveAndFlush(agency);
//			Agency agencyCode = new Agency();
//			agencyCode = agencyRepository.findById(newAgency.getId());
//			String code = "AGENCY" + data.getString("agencyArea") + "0000" + newAgency.getId();
//			agencyRepository.saveAndFlush(agencyCode);

//			create user level agency
//			users.setUsername(code);
//			users.setFullname(newAgency.getAgencyName());
//			if (data.getInt("status") == 1){
//				users.setStatus(0);
//			}else {
//				users.setStatus(1);
//			}
//			users.setAuthorized(1);
//			users.setCreateBy(principal.getName());
//			users.setCreateDate(date);
//			users.setEmail(data.getString("email"));
//			users.setAddress(newAgency.getOfficeAddress());
//			users.setPhone(newAgency.getPhoneNumber());
//			String password = bCryptPasswordEncoder.encode("123456");
//			users.setPassword(password);
//			users.setAgencyId(newAgency.getId());
//			users.setAreaId(agencyAreaRepository.getOne(Long.parseLong(data.getString("agencyArea"))));
//			users.setPasswordNeverExpired(0);
//			users.setLastLoginDate(date);
//			users.setLevelUsers(levelsRepository.getOne(Long.parseLong("4")));
//			usersRepository.saveAndFlush(users);
//			insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.CREATE,
//						principal.getName() + " create user success");

//			tao thong tin nguoi dai dien
			createAuthor(newAgency.getId(),data.getString("fullNameRepresent").trim(), data.getString("birthdayRepresent").trim(),data.getString("positionRepresent").trim(),
					data.getString("emailRepresent").trim(),data.getString("telephoneRepresent").trim(), data.getString("phoneNumberRepresent").trim(),0, principal);
			System.err.println("Tạo mới người đại diện thành công");
			//			tao thong tin nguoi lien he
			createAuthor(newAgency.getId(),data.getString("fullNameContact").trim(), data.getString("birthdayContact").trim(),data.getString("positionContact").trim(),
					data.getString("emailContact").trim(),data.getString("telephoneContact").trim(), data.getString("phoneNumberContact").trim(),2, principal);
			System.err.println("Tạo mới người liên hệ thành công");

			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	@Override
	public boolean updateAgency(JSONObject data, Principal principal) {
		Agency agency = new Agency();
		try {
			if (!data.getString("id").isEmpty()) {
				try {
					agency = agencyRepository.findById(Long.parseLong(data.getString("id")));
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
			if (!data.getString("agencyName").isEmpty()) {
				agency.setAgencyName(data.getString("agencyName").trim());
			}
			if (!data.getString("agencyType").isEmpty()) {
				agency.setAgencyType(data.getInt("agencyType"));
			}
			if (!data.getString("status").isEmpty()) {
				agency.setStatus(data.getInt("status"));
			}
			if (!data.getString("businessLicense").isEmpty()) {
				agency.setBusinessLicense(data.getString("businessLicense").trim());
			}
			if (!data.getString("licenseDate").isEmpty()) {
				Date licenseDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("licenseDate").trim());
				agency.setLicenseDate(licenseDate);
			}
			if (!data.getString("licensePlace").isEmpty()) {
				agency.setLicensePlace(data.getString("licensePlace").trim());
			}
			if (!data.getString("taxCode").isEmpty()) {
				agency.setTaxCode(data.getString("taxCode").trim());
			}
			if (!data.getString("branchAgency").isEmpty()) {
				agency.setBranchAgency(data.getString("branchAgency").trim());
			}
			if (!data.getString("agencyCode").isEmpty()) {
				agency.setAgencyCode(data.getString("agencyCode").trim());
			}
			agency.setOfficeAddress(data.getString("officeAddress").trim());
			agency.setInvoiceAddress(data.getString("invoiceAddress").trim());
			agency.setPhoneNumber(data.getString("phoneNumber").trim());
			agency.setEmail(data.getString("email").trim());
			agency.setType(0);
			agency.setUpdateBy(principal.getName());
			agency.setUpdateDate(date);

			agencyRepository.saveAndFlush(agency);

//			update status user agency
//			Users users = new Users();
//			users = usersRepository.findUsersByUsername(agency.getAgencyCode());
//			if (users != null){
//				if (data.getInt("status") == 1){
//					users.setStatus(0);
//				}else {
//					users.setStatus(1);
//				}
//				usersRepository.saveAndFlush(users);
//			}


			//			tao thong tin nguoi dai dien
			editAuthor(data.getLong("idRepresent"),agency.getId(),data.getString("fullNameRepresent").trim(), data.getString("birthdayRepresent"),data.getString("positionRepresent").trim(),
					data.getString("emailRepresent").trim(),data.getString("telephoneRepresent").trim(), data.getString("phoneNumberRepresent").trim(),0, principal);
			System.err.println("Edit người đại diện thành công");
			//			tao thong tin nguoi lien he
			editAuthor(data.getLong("idContact"),agency.getId(),data.getString("fullNameContact").trim(), data.getString("birthdayContact"),data.getString("positionContact").trim(),
					data.getString("emailContact").trim(),data.getString("telephoneContact").trim(), data.getString("phoneNumberContact").trim(),2, principal);
			System.err.println("Edit người liên hệ thành công");

			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public void deleteAgency(Long id) {
		List<AuthorizedPerson> lstAuth = authorizedPersonRepository.getAuthorizedPerson(id);
		this.authorizedPersonRepository.deleteInBatch(lstAuth);
		this.agencyRepository.deleteById(id);
	}

	@Override
	public List<Agency> getListAgencyByArea(Long areaId) {
		return agencyRepository.getAllAgencyAreaId(areaId);
	}

	@Override
	public List<Agency> getLIstAgencyByType(int type) {
		return agencyRepository.findAgencyByType(type);
	}

	@Override
	public List<Agency> getAgencyByArea(Long areaId) {
		return  agencyRepository.getAgencyByArea(areaId);
	}

	@Override
	public List<Agency> getListAgencyById(Long id) {
		return agencyRepository.getAgencyByArea(id);
	}

	@Override
	public List<Agency> getAgencyByArea(List<AgencyArea> idArea) {
		return  agencyRepository.getAgencyByArea(idArea);
	}

	@Override
	public Agency getAgencyByCode(String agencyCode) {
		try {
			return agencyRepository.findAgencyByAgencyCode(agencyCode);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


	public boolean createAuthor(Long agencyId, String fullName, String birthDay, String position, String email, String telephone, String phoneNumber, int type , Principal principal) {
		AuthorizedPerson authorizedPerson = new AuthorizedPerson();
		try {
			if(!fullName.isEmpty()){
				authorizedPerson.setFullName(fullName.trim());
			}
			if (!birthDay.isEmpty()) {
				Date birthDay1 = new SimpleDateFormat("dd/MM/yyyy").parse(birthDay.trim());
				authorizedPerson.setBirthday(birthDay1);
			}
			authorizedPerson.setPosition(position.trim());
			if (!phoneNumber.isEmpty()){
				authorizedPerson.setPhoneNumber(phoneNumber.trim());
			}
			if (!telephone.isEmpty()){
				authorizedPerson.setTelephone(telephone.trim());
			}
			if (!email.isEmpty()){
				authorizedPerson.setEmail(email.trim());
			}
			authorizedPerson.setType(type);
			authorizedPerson.setAgencyIdPerson(agencyRepository.getOne(agencyId));
			authorizedPerson.setCreatedBy(principal.getName());
			authorizedPerson.setCreatedDate(date);
			authorizedPersonRepository.saveAndFlush(authorizedPerson);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	public boolean editAuthor(long id, Long agencyId, String fullName, String birthDay, String position, String email, String telephone, String phoneNumber, int type , Principal principal) {
		AuthorizedPerson authorizedPerson = new AuthorizedPerson();
		try {
				try {
					authorizedPerson = authorizedPersonRepository.findById(id);
				} catch (Exception e) {
					System.out.println(e);
					return false;
				}
			if(!fullName.isEmpty()){
				authorizedPerson.setFullName(fullName.trim());
			}
			if (!birthDay.isEmpty()) {
				Date birthDay1 = new SimpleDateFormat("dd/MM/yyyy").parse(birthDay.trim());
				authorizedPerson.setBirthday(birthDay1);
			}
			authorizedPerson.setPosition(position.trim());
			if (!phoneNumber.isEmpty()){
				authorizedPerson.setPhoneNumber(phoneNumber.trim());
			}
			if (!telephone.isEmpty()){
				authorizedPerson.setTelephone(telephone.trim());
			}
			if (!email.isEmpty()){
				authorizedPerson.setEmail(email.trim());
			}
			authorizedPerson.setType(type);
			authorizedPerson.setAgencyIdPerson(agencyRepository.getOne(agencyId));
			authorizedPerson.setCreatedBy(principal.getName());
			authorizedPerson.setCreatedDate(date);
			authorizedPersonRepository.saveAndFlush(authorizedPerson);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

}
