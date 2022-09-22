package com.neo.vas.service.impl;

import com.neo.vas.domain.*;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.service.SystemFunctionalService;
import com.neo.vas.util.VNCharacterUtils;
import oracle.net.aso.f;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class SystemFunctionServiceImpl implements SystemFunctionalService {
    @Autowired
    SystemFunctionalRepository systemFunctionalRepository;

    @Override
    public List<SystemFunctional> getAll() {
        List<SystemFunctional> sfList = systemFunctionalRepository.findAll();
        return sfList;
    }


    @Override
    @Transactional
    public boolean editSF(JSONObject data, Principal principal) {
        SystemFunctional sf = new SystemFunctional();
        try {
            if (!data.getString("sfId").isEmpty()) {
                try {
                    sf = systemFunctionalRepository.findSystemFunctionalBySfId(Long.parseLong(data.getString("sfId")));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("sfName").isEmpty() && !data.getString("sfFather").isEmpty()) {
                if(data.getString("sfName").equals(data.getString("sfFather")) && Integer.parseInt(data.getString("status").trim()) == 1) {
                    return false;
                }
                if(!data.getString("sfName").equals(data.getString("sfFather")) && Integer.parseInt(data.getString("status").trim()) == 0) {
                    return false;
                }
            }
            if (!data.getString("fontAwesomeIconClass").isEmpty()) {
                sf.setFontAwesomeIconClass(data.getString("fontAwesomeIconClass").trim());
            }
            if (!data.getString("sfName").isEmpty()) {
                sf.setSfName(data.getString("sfName").trim());
            }
            if (!data.getString("urlController").isEmpty()) {
                sf.setUrlController(data.getString("urlController"));
            }
            if (!data.getString("status").isEmpty()) {
                sf.setStatus(Integer.parseInt(data.getString("status").trim()));
            }
            if (!data.getString("sfFather").isEmpty()) {
                String father = data.getString("sfFather").trim();
                SystemFunctional sfunc = systemFunctionalRepository.findSFBysfFatherName(father);
                if(sfunc != null){
                    sf.setSfFather(sfunc);
                }
                else {
                    sf.setSfFather(sf);
                }
            }
            else {
                sf.setSfFather(sf);
            }
            systemFunctionalRepository.saveAndFlush(sf);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveNewSF(JSONObject data, Principal principal) {
        try {
            SystemFunctional sf = new SystemFunctional();
            if (!data.getString("fontAwesomeIconClass").isEmpty()) {
                sf.setFontAwesomeIconClass(data.getString("fontAwesomeIconClass").trim());
            }
            if (!data.getString("sfName").isEmpty()) {
                SystemFunctional sfunc = systemFunctionalRepository.findSFBysfFatherName(data.getString("sfName").trim());
                if(sfunc == null){
                    sf.setSfName(data.getString("sfName").trim());
                }
                else {
                    return false;
                }
            }
            if (!data.getString("urlController").isEmpty()) {
                sf.setUrlController(data.getString("urlController"));
            }
            if (!data.getString("status").isEmpty()) {
                sf.setStatus(Integer.parseInt(data.getString("status").trim()));
            }
            if (!data.getString("sfFather").isEmpty()) {
                SystemFunctional sfunc = systemFunctionalRepository.findSFBysfFatherName(data.getString("sfFather"));
                sf.setSfFather(sfunc);
            }
            else {
                sf.setSfFather(sf);
            }
            systemFunctionalRepository.saveAndFlush(sf);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public void deleteSF(long id) {
        this.systemFunctionalRepository.deleteById(id);
    }

    @Override
    public Page<SystemFunctional> searchSystemFunctional(String sfId, String sfName, String urlController, String sfFather, String sfStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("sfId").descending());
        if ((sfId != null && !sfId.equals("")) || (sfName != null && !sfName.equals("")) || (urlController != null && !urlController.equals("")) || (sfFather != null && !sfFather.equals("")) || (sfStatus != null && !sfStatus.equals(""))){
            return systemFunctionalRepository.findAllSF(sfId, VNCharacterUtils.removeAccent(sfName.toUpperCase()),
                    urlController, VNCharacterUtils.removeAccent(sfFather.toUpperCase()), sfStatus, pageable);
        }
//        return systemFunctionalRepository.findAll(pageable);
        return systemFunctionalRepository.findAllSF(pageable);
    }
}
