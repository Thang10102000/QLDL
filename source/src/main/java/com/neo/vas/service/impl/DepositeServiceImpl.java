package com.neo.vas.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.neo.vas.constant.ConstantSaveFile;
import com.neo.vas.domain.AgencyContract;
import com.neo.vas.domain.ImageDeposits;
import com.neo.vas.repository.AgencyContractRepository;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.ImgDepositsRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.Deposits;
import com.neo.vas.repository.DepositeRepository;
import com.neo.vas.service.DepositeService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@Service
public class DepositeServiceImpl implements DepositeService {

	@Autowired
	private DepositeRepository repo;
	@Autowired
	private ServletContext context;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private ImgDepositsRepository imgDepositsRepository;
	@Autowired
	private AgencyContractRepository acRepository;


	Date date = new Date();

	@Override
	public Page<Deposits> getAll(String agencyName, String depositsAmount, String startDate, String endDate, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page,size);
		// If no date search
		if ((agencyName != null || depositsAmount != null || status != null) && (startDate.isEmpty() && endDate.isEmpty())) {
			System.out.println("No date search");
			return repo.findAllNoDate(agencyName, depositsAmount, status, pageable);
		}
		// If start date null
		if ((agencyName != null || depositsAmount != null || status != null) 
				&& (startDate.isEmpty() && !endDate.isEmpty())
				) {
			System.out.println("No start date search");
			Date endDateVar = null;
			try {
				endDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return repo.findAllNoStartDate(agencyName, depositsAmount, endDateVar, status, pageable);
		}
		// If end date null
		if ((agencyName != null || depositsAmount != null || status != null )
				&& (!startDate.isEmpty() && endDate.isEmpty())
				) {
			System.out.println("No end date search");
			Date startDateVar = null;
			try {
				startDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return repo.findAllNoEndDate(agencyName, depositsAmount, startDateVar, status, pageable);
		}
		// Both date input not null
		if ((agencyName != null || depositsAmount != null || status != null )
				&& (!startDate.isEmpty() && !endDate.isEmpty())
				) {
			System.out.println("Valid date search");
			Date startDateVar = null;
			Date endDateVar = null;
			try {
				startDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
				endDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return repo.findAll(agencyName, depositsAmount, startDateVar, endDateVar, status, pageable);
		}
		// index page search
		return repo.findAll(pageable);
	}

	@Override
	public void save(Deposits deposite) {
		repo.save(deposite);
	}

	@Override
	public boolean createDeposit(JSONObject data, Principal principal, MultipartFile[] file) {
		try {
			Deposits deposits = new Deposits();
			if (!data.getString("agencyId").isEmpty()){
				deposits.setAgencyD(agencyRepository.getOne(data.getLong("agencyId")));
			}
			if (!data.getString("startDate").isEmpty()){
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
				deposits.setStartDate(start);
			}
			if (!data.getString("endDate").isEmpty()){
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
				deposits.setEndDate(end);
			}
			if (!data.getString("depositsAmount").isEmpty()){
				String depositsAmount = data.getString("depositsAmount").trim();
				depositsAmount = depositsAmount.replaceAll("[^a-zA-Z0-9]", "");
				deposits.setDepositsAmount(depositsAmount);
			}
			if (!data.getString("contractId").isEmpty()){
				deposits.setContractD(acRepository.getOne(data.getLong("contractId")));
			}
			if (!data.getString("depositNo").isEmpty()){
				deposits.setDepositNo(data.getString("depositNo"));
			}
			if (!data.getString("receivedDate").isEmpty()){
				Date receivedDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("receivedDate"));
				deposits.setReceivedDate(receivedDate);
			}
			if (!data.getString("customerName").isEmpty()){
				deposits.setCustomerName(data.getString("customerName"));
			}
			deposits.setStatus(1);
			deposits.setCreateBy(principal.getName());
			deposits.setCreateDate(date);
			Deposits dp = repo.saveAndFlush(deposits);
			// Upload file, get string result return
			ArrayList<String> returnListStr = new ArrayList<>();
			try {
				returnListStr = uploadFile(file);
				// Get arr ImageDeposits
				int totalImgDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));

				for (int i = 0; i < totalImgDeposits; i++) {
					ImageDeposits img = new ImageDeposits();
					img.setImagePath(returnListStr.get(i));
					img.setCreatedDate(date);
					img.setDepositsID(dp);
					// Save ImageDeposits
						imgDepositsRepository.saveAndFlush(img);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}catch (Exception e){
			System.err.println(e);
			return false;
		}
	}

	@Override
	public boolean editDeposit(JSONObject data, Principal principal, MultipartFile[] file) {
		try {
			Deposits deposits = new Deposits();
			if (!data.getString("id").isEmpty()){
				deposits = repo.getOne(data.getLong("id"));
			}
			if (!data.getString("startDate").isEmpty()){
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
				deposits.setStartDate(start);
			}
			if (!data.getString("endDate").isEmpty()){
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
				deposits.setEndDate(end);
			}
			if (!data.getString("depositsAmount").isEmpty()){
				String depositsAmount = data.getString("depositsAmount").trim();
				depositsAmount = depositsAmount.replaceAll("[^a-zA-Z0-9]", "");
				deposits.setDepositsAmount(depositsAmount);
			}
			if (!data.getString("status").isEmpty()){
				deposits.setStatus(data.getInt("status"));
			}
			if (!data.getString("contractD").isEmpty()){
				deposits.setContractD(acRepository.getOne(data.getLong("contractD")));
			}
			if (!data.getString("depositNo").isEmpty()){
				deposits.setDepositNo(data.getString("depositNo"));
			}
			if (!data.getString("receivedDate").isEmpty()){
				Date receivedDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("receivedDate"));
				deposits.setReceivedDate(receivedDate);
			}
			if (!data.getString("customerName").isEmpty()){
				deposits.setCustomerName(data.getString("customerName"));
			}
			repo.saveAndFlush(deposits);
			// Upload file, get string result return
			ArrayList<String> returnListStr = new ArrayList<>();
			try {
				returnListStr = uploadFile(file);
				// Get arr ImageDeposits
				int totalImgDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));

				for (int i = 0; i < totalImgDeposits; i++) {
					ImageDeposits img = new ImageDeposits();
					img.setImagePath(returnListStr.get(i));
					img.setCreatedDate(date);
					img.setDepositsID(deposits);
					// Save ImageDeposits
					imgDepositsRepository.saveAndFlush(img);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Deposits get(Long id) {
		return repo.findById(id).get();
	}

	@Override
	public List<Deposits> exportFile(HttpServletResponse response, String agencyName, String depositsAmount, String startDate, String endDate, String status) {
		Date startDateVar = null;
		Date endDateVar = null;
		List<Deposits> depositsList = new ArrayList<>();
		try {
			if (!startDate.isEmpty()){
				startDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
			}
			if (!endDate.isEmpty()){
				endDateVar = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (startDateVar == null  && endDateVar == null){
			depositsList = repo.exportDepositNoDate(agencyName,depositsAmount,status);
		}
		if (startDateVar != null && endDateVar == null){
			depositsList = repo.exportDepositNoEndDate(agencyName,depositsAmount,startDateVar,status);
		}
		if (startDateVar == null && endDateVar != null){
			depositsList = repo.exportDepositNoStartDate(agencyName,depositsAmount,endDateVar,status);
		}
		if (startDateVar != null && endDateVar != null){
			depositsList = repo.exportDepositAllDate(agencyName, depositsAmount, startDateVar, endDateVar, status);
		}
		return depositsList;
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

	@Override
	public Deposits getDepositsExist(Long agencyId, Date startDate, Date endDate) {
		return repo.getDepositsExist(agencyId,startDate,endDate);
	}

	@Override
	public List<AgencyContract> getAgencyContractByAgencyId(Long agencyId) {
		return acRepository.agencyContractSearch(agencyId);
	}


//	upload multipart file

	public ArrayList<String> uploadFile(MultipartFile[] file) {
		// Upload file
		// Path to upload file.
		ArrayList<String> returnStr = new ArrayList<String>();
		String fullSavePath = context.getRealPath("") + ConstantSaveFile.SAVE_DIRECTORY + File.separator;
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
				returnStr.add("/" + ConstantSaveFile.SAVE_DIRECTORY + "/" + filename);
			} catch (IOException e) {
				e.printStackTrace();
				upFail++;
			}
		}
		String uploadResultStr = "[Ảnh chứng thư] Success upload: " + upSuccess + " file" + "," + "Fail upload: "
				+ upFail + " file";
		// index = upSuccess + 1
		returnStr.add(uploadResultStr);
		// total file path (imgFilePath) need inserted
		returnStr.add(String.valueOf(upSuccess));
		return returnStr;
	}
}
