package com.neo.vas.service;

import com.neo.vas.domain.AuthorizedPerson;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
public interface AuthorizedPersonService {
    List<AuthorizedPerson> getAllAuthorizedPerson();
    Page<AuthorizedPerson> searchAuthor(String agencyName, int page, int size);
    boolean createAuthor(JSONObject data);
    boolean updateAuthor(JSONObject data);
    AuthorizedPerson getAuthorPersonById(Long id);
    void deleteAuthorPerson(Long id);
}
