package com.neo.vas.service.impl;

import com.neo.vas.domain.AuthorizedPerson;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.AuthorizedPersonRepository;
import com.neo.vas.service.AuthorizedPersonService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Service
public class AuthorizedPersonServiceImpl implements AuthorizedPersonService {

    @Autowired
    private AuthorizedPersonRepository authorizedPersonRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    Date date = new Date();

    @Override
    public List<AuthorizedPerson> getAllAuthorizedPerson() {
        return authorizedPersonRepository.findAll();
    }

    @Override
    public Page<AuthorizedPerson> searchAuthor(String agencyName,int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if (agencyName != null){
            return authorizedPersonRepository.searchAuthorized(agencyName,pageable);
        }
        return authorizedPersonRepository.findAll(pageable);
    }

    @Override
    public boolean createAuthor(JSONObject data) {
        AuthorizedPerson authorizedPerson = new AuthorizedPerson();
        try {
            if (!data.getString("agencyFullName").isEmpty()) {
                authorizedPerson.setFullName(data.getString("agencyFullName").trim());
            }
            if (!data.getString("birthday").isEmpty()) {
                Date birthDay = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("birthday").trim());
                authorizedPerson.setBirthday(birthDay);
            }
            if (!data.getString("position").isEmpty()) {
                authorizedPerson.setPosition(data.getString("position").trim());
            }
            if (!data.getString("phoneNumber").isEmpty()) {
                authorizedPerson.setPhoneNumber(data.getString("phoneNumber").trim());
            }
            if (!data.getString("type").isEmpty()) {
                authorizedPerson.setType(data.getInt("type"));
            }
            if (!data.getString("agencyEmail").isEmpty()) {
                authorizedPerson.setEmail(data.getString("agencyEmail").trim());
            }
            if (!data.getString("agencyName1").isEmpty()) {
                authorizedPerson.setAgencyIdPerson(agencyRepository.getOne(Long.parseLong(data.getString("agencyName1"))));
            }
            authorizedPerson.setCreatedBy(data.getString("createdByAuthor"));
            authorizedPerson.setCreatedDate(date);
            authorizedPersonRepository.saveAndFlush(authorizedPerson);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean updateAuthor(JSONObject data) {
        AuthorizedPerson authorizedPerson = new AuthorizedPerson();
        try {
            if (!data.getString("id").isEmpty()) {
                try {
                    authorizedPerson = authorizedPersonRepository.findById(Long.parseLong(data.getString("id")));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
            if (!data.getString("fullName").isEmpty()) {
                authorizedPerson.setFullName(data.getString("fullName").trim());
            }
            if (!data.getString("birthday").isEmpty()) {
                Date birthDay = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("birthday").trim());
                authorizedPerson.setBirthday(birthDay);
            }
            if (!data.getString("position").isEmpty()) {
                authorizedPerson.setPosition(data.getString("position").trim());
            }
            if (!data.getString("phoneNumber").isEmpty()) {
                authorizedPerson.setPhoneNumber(data.getString("phoneNumber").trim());
            }
            if (!data.getString("type").isEmpty()) {
                authorizedPerson.setType(data.getInt("type"));
            }
            if (!data.getString("email").isEmpty()) {
                authorizedPerson.setEmail(data.getString("email").trim());
            }
            if (!data.getString("agencyIdPerson").isEmpty()) {
                authorizedPerson.setAgencyIdPerson(agencyRepository.getOne(Long.parseLong(data.getString("agencyIdPerson"))));
            }
            authorizedPersonRepository.saveAndFlush(authorizedPerson);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public AuthorizedPerson getAuthorPersonById(Long id) {
        Optional<AuthorizedPerson> optAuthorizedPerson = authorizedPersonRepository.findById(id);
        AuthorizedPerson authorizedPerson = null;
        if (optAuthorizedPerson.isPresent()) {
            authorizedPerson = optAuthorizedPerson.get();
        } else {
            throw new RuntimeException("Không tìm thấy " + id);
        }
        return authorizedPerson;
    }

    @Override
    public void deleteAuthorPerson(Long id) {
        this.authorizedPersonRepository.deleteById(id);
    }
}
