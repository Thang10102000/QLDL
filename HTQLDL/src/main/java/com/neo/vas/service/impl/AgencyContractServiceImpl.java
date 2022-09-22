package com.neo.vas.service.impl;

import com.neo.vas.constant.ConstantSaveFile;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Deposits;
import com.neo.vas.domain.ImageDeposits;
import com.neo.vas.dto.AgencyContractDTO;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.ImgDepositsRepository;
import com.neo.vas.service.specification.AgencyContractSpecification;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.AgencyContract;
import com.neo.vas.repository.AgencyContractRepository;
import com.neo.vas.service.AgencyContractService;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * project_name: demo Created_by: datnd time: 21/05/2021
 */
@Service
public class AgencyContractServiceImpl implements AgencyContractService {

    @Autowired
    private AgencyContractRepository repo;
    @Autowired
    private ServletContext context;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private ImgDepositsRepository imgDepositsRepository;

    Date date = new Date();

    @Override
    public Page<AgencyContract> getAll(String pageNumber, String sortField, String sortDir, String contractNo, String agencyName
            , String status, String serviceType, String recordNum) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("desc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber) - 1,
                Integer.parseInt(recordNum), sort);
        if (contractNo != null || agencyName != null
                || status != null || serviceType != null) {
            return repo.findAll(contractNo, agencyName, status, serviceType, pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public boolean createAgencyContract(JSONObject data, Principal principal, MultipartFile[] file) {

            AgencyContract agencyContract = new AgencyContract();
            try {
                if (!data.getString("contractNo").isEmpty()) {
                    agencyContract.setContractNo(data.getString("contractNo").trim());
                }
                if (!data.getString("serviceType").isEmpty()) {
                    agencyContract.setServiceType(data.getString("serviceType").trim());
                }
                if (!data.getString("signDate").isEmpty()) {
                    Date signDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("signDate").trim());
                    agencyContract.setSignDate(signDate);
                }
                if (!data.getString("startDate").isEmpty()) {
                    Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate").trim());
                    agencyContract.setStartDate(startDate);
                }
                if (!data.getString("endDate").isEmpty()) {
                    Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate").trim());
                    agencyContract.setEndDate(endDate);
                }
                if (!data.getString("guaranteeValue").isEmpty()) {
                    String guaranteeValue = data.getString("guaranteeValue").trim();
                    guaranteeValue = guaranteeValue.replaceAll("[^a-zA-Z0-9]", "");
                    agencyContract.setGuaranteeValue(Long.parseLong(guaranteeValue));
                }
//                if (!data.getString("status").isEmpty()) {
                    agencyContract.setStatus(0);
//                }
                if (!data.getString("liquidationDate").isEmpty()) {
                    agencyContract.setLiquidationDate(data.getInt("liquidationDate"));
                }
                agencyContract.setCreateBy(principal.getName());
                agencyContract.setCreateDate(date);
                if (!data.getString("agencyId").isEmpty()) {
                    agencyContract.setAgencyAC(agencyRepository.getOne(Long.parseLong(data.getString("agencyId").trim())));
                }
                AgencyContract ac = repo.saveAndFlush(agencyContract);
                // Upload file, get string result return
                ArrayList<String> returnListStr = new ArrayList<>();
                returnListStr = uploadFile(file);
                // Get arr ImageDeposits
                int totalImgDeposits = Integer.parseInt(returnListStr.get(returnListStr.size() - 1));

                for (int i = 0; i < totalImgDeposits; i++) {
                    ImageDeposits img = new ImageDeposits();
                    img.setImagePath(returnListStr.get(i));
                    img.setCreatedDate(date);
                    img.setAgencyContractID(ac);
                    // Save ImageDeposits
                    try {
                        imgDepositsRepository.saveAndFlush(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }catch (Exception e){
                System.err.println(e);
                return false;
            }
    }

    @Override
    public void save(AgencyContract agencyContract) {
        repo.save(agencyContract);
    }

    @Override
    public AgencyContract get(Long id) {
        return repo.findById(id).get();
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Page<AgencyContract> getAll(String contractNo, String agencyId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if (contractNo != null || agencyId != null || status != null) {
            return repo.agencyContractSearch(contractNo, agencyId, status, pageable);
        }
        return repo.agencyContractSearch(contractNo, agencyId, status, pageable);
    }

    @Override
    public Page<AgencyContract> searchAgencyContract(String contractNo, String agencyId, String status, String startDate,String endDate, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
//            TH1
            if (contractNo != null || agencyId != null || status != null && (startDate.isEmpty() && endDate.isEmpty())){
                return repo.searchAgencyContractStartEndNull(contractNo,agencyId,status,pageable);
            }

//            TH2
            if (contractNo != null || agencyId != null || status != null && (!startDate.isEmpty() && endDate.isEmpty())){
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.trim());
                return repo.searchAgencyContractEndDateNull(contractNo,agencyId,status,start,pageable);
            }
//            TH3
            if (contractNo != null || agencyId != null || status != null && (startDate.isEmpty() && !endDate.isEmpty())){
                Date end =  new SimpleDateFormat("dd/MM/yyyy").parse(endDate.trim());
                return repo.searchAgencyContractStartDateNul(contractNo,agencyId,status,end,pageable);
            }
//            TH4
            if (contractNo != null || agencyId != null || status != null && (!startDate.isEmpty() && !endDate.isEmpty())){
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.trim());
                Date end =  new SimpleDateFormat("dd/MM/yyyy").parse(endDate.trim());
                return repo.searchAgencyContract(contractNo,agencyId,status,start,end,pageable);
            }
            return repo.findAll(pageable);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

//    export file

    @Override
    public List<AgencyContract> exportFile(HttpServletResponse response, AgencyContract agencyContract){
        try {
            Specification<AgencyContract> conditions = Specification
                    .where(!agencyContract.getContractNo().isEmpty() ?
                            AgencyContractSpecification.hasContractNo(agencyContract.getContractNo()) : null)
                    .and(null != agencyContract.getAgencyAC() ?
                            AgencyContractSpecification.hasAgencyName(agencyContract.getAgencyAC()) : null)
                    .and(100 != agencyContract.getStatus() ?
                            AgencyContractSpecification.hasStatus(agencyContract.getStatus()) : null)
                    .and(null != agencyContract.getStartDate() ?
                            AgencyContractSpecification.hasStartDate(agencyContract.getStartDate()) : null)
                    .and(null != agencyContract.getEndDate() ?
                            AgencyContractSpecification.hasEndDate(agencyContract.getEndDate()) : null)
                    .and((null != agencyContract.getEndDate() && null != agencyContract.getStartDate()) ?
                            AgencyContractSpecification.hasStartEnd(agencyContract.getStartDate(), agencyContract.getEndDate()) : null);
            List<AgencyContract> lstAgencyContracts = repo.findAll(conditions);
            return lstAgencyContracts;
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    @Override
    public boolean editAgengyContract(JSONObject data, Principal principal, MultipartFile[] file) {

        AgencyContract agencyContract = new AgencyContract();
        try {
            if(!data.getString("id").isEmpty())
            {
                agencyContract = repo.getOne(data.getLong("id"));
            }
            if (!data.getString("serviceType").isEmpty()) {
                agencyContract.setServiceType(data.getString("serviceType").trim());
            }
            if (!data.getString("signDate").isEmpty()) {
                Date signDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("signDate").trim());
                agencyContract.setSignDate(signDate);
            }
            if (!data.getString("startDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate").trim());
                agencyContract.setStartDate(startDate);
            }
            if (!data.getString("endDate").isEmpty()) {
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate").trim());
                agencyContract.setEndDate(endDate);
            }
            if (!data.getString("guaranteeValue").isEmpty()) {
                String guaranteeValue = data.getString("guaranteeValue").trim();
                guaranteeValue = guaranteeValue.replaceAll("[^a-zA-Z0-9]", "");
                agencyContract.setGuaranteeValue(Long.parseLong(guaranteeValue));
            }
            if (!data.getString("status").isEmpty()) {
                agencyContract.setStatus(data.getInt("status"));
            }
            if (!data.getString("liquidationDate").isEmpty()) {
                agencyContract.setLiquidationDate(data.getInt("liquidationDate"));
            }
            agencyContract.setUpdateBy(principal.getName());
            agencyContract.setUpdateDate(new Date());

             repo.saveAndFlush(agencyContract);
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
                    img.setAgencyContractID(agencyContract);
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
//    @Override
//    public Page<AgencyContractDTO> searchAgencyContractArea(String contractNo, Long id, Integer status, Date startDate, Date endDate, int page, int size) {
//        Pageable pageable = PageRequest.of(page,size);
//            return repo.searchAgencyContract(contractNo,id,status,startDate,endDate, pageable);
//    }
}
