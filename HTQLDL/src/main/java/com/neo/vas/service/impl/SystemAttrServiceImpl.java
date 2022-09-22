package com.neo.vas.service.impl;

import com.neo.vas.domain.SystemAttr;
import com.neo.vas.repository.SystemAttrRepository;
import com.neo.vas.service.SystemAttrService;
import com.neo.vas.util.VNCharacterUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

@Service
public class SystemAttrServiceImpl implements SystemAttrService {
    @Autowired
    private SystemAttrRepository saRepository;

    @Override
    public Page<SystemAttr> searchSystemAttr(String saId, String type,
                                             String name, String value, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if ((saId != null && !saId.equals("")) || (type != null && !type.equals("")) || (name != null && !name.equals("")) || (value != null && !value.equals(""))) {
            return saRepository.findSystemAttr(saId, VNCharacterUtils.removeAccent(type.toUpperCase()),
                    VNCharacterUtils.removeAccent(name.toUpperCase()), VNCharacterUtils.removeAccent(value.toUpperCase()),
                    VNCharacterUtils.removeAccent(description.toUpperCase()), pageable);
        }
        return saRepository.findAll(pageable);
    }

    @Override
    public boolean createSystemAttr(JSONObject data, Principal principal) {
        try {
            SystemAttr systemAttr = new SystemAttr();
            if (!data.getString("typeAttr").isEmpty()) {
                systemAttr.setType(data.getString("typeAttr").trim());
            }
            if (!data.getString("nameAttr").isEmpty()) {
                systemAttr.setName(data.getString("nameAttr").trim());
            }
            if (!data.getString("valueAttr").isEmpty()) {
                systemAttr.setValue(data.getString("valueAttr").trim());
            }
            if (!data.getString("descriptionAttr").isEmpty()) {
                systemAttr.setDescription(data.getString("descriptionAttr").trim());
            }
            systemAttr.setModifier(principal.getName());
            systemAttr.setModified(new Date());
            return saRepository.saveAndFlush(systemAttr) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editSystemAttr(JSONObject data, Principal principal) {
        SystemAttr systemAttr;
        try {
            if (!data.getString("saId").isEmpty()) {
                try {
                    systemAttr = saRepository.getOne(Long.parseLong(data.getString("saId")));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("type").isEmpty()) {
                systemAttr.setType(data.getString("type").trim());
            }
            if (!data.getString("name").isEmpty()) {
                systemAttr.setName(data.getString("name").trim());
            }
            if (!data.getString("value").isEmpty()) {
                systemAttr.setValue(data.getString("value").trim());
            }
            if (!data.getString("description").isEmpty()) {
                systemAttr.setDescription(data.getString("description").trim());
            }
            systemAttr.setModifier(principal.getName());
            systemAttr.setModified(new Date());

            return saRepository.saveAndFlush(systemAttr) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteById(Long id) {
        this.saRepository.deleteById(id);
    }

    @Override
    public SystemAttr getSaById(Long id) {
        return saRepository.getOne(id);
    }

    @Override
    public SystemAttr getSaByTypeName(String type, String name) {
        return saRepository.findSystemAttrbyTypeName(type,name);
    }
}
