package com.neo.vas.service;

import com.neo.vas.domain.SystemAttr;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface SystemAttrService {
    Page<SystemAttr> searchSystemAttr(String saId, String type,
                                      String name, String value, String description, int page, int size);
    boolean createSystemAttr(JSONObject data, Principal principal);
    boolean editSystemAttr(JSONObject data , Principal principal);
    void deleteById(Long id);
    SystemAttr getSaById(Long id);
    SystemAttr getSaByTypeName(String type, String name);
}