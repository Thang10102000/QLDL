package com.neo.vas.service;

import com.neo.vas.domain.SystemFunctional;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface SystemFunctionalService {
    List<SystemFunctional> getAll();

    //Page<SystemFunctional> searchSystemFunctional(String sfName, String urlController, String sfFather, int page, int size);

    boolean editSF(JSONObject data, Principal principal);

    boolean saveNewSF(JSONObject data, Principal principal);

    void deleteSF(long sfId);
    Page<SystemFunctional> searchSystemFunctional(String sfId,String sfName, String urlController, String sfFather, String sfStatus, int page, int size);
}
