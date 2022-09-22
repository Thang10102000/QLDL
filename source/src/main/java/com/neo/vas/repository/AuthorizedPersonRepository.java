package com.neo.vas.repository;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AuthorizedPerson;
import com.neo.vas.dto.AgencyInformationAuthorizedDTO;
import com.neo.vas.dto.AgencyInformationContactDTO;
import com.neo.vas.dto.AgencyInformationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Repository
public interface AuthorizedPersonRepository extends JpaRepository<AuthorizedPerson,Long> {
    AuthorizedPerson findById(long id);

    @Query("select au from AuthorizedPerson au where au.agencyIdPerson.id = ?1 ")
    List<AuthorizedPerson> getAuthorizedPerson(Long agencyId);

    @Query("select au from AuthorizedPerson au where (?1 is null or au.agencyIdPerson.id = ?1 )  order by au.createdDate desc")
    Page<AuthorizedPerson> searchAuthorized(String agencyName, Pageable pageable);

    @Query("select new com.neo.vas.dto.AgencyInformationDTO(au.id,au.fullName,au.birthday,au.position,au.email,au.telephone,au.phoneNumber) from AuthorizedPerson au where au.agencyIdPerson.id = ?1 and au.type = 0 ")
    AgencyInformationDTO getRepresent(Long agencyId);
    @Query("select new com.neo.vas.dto.AgencyInformationAuthorizedDTO(au.id,au.fullName,au.birthday,au.position,au.email,au.telephone,au.phoneNumber) from AuthorizedPerson au where au.agencyIdPerson.id = ?1 and au.type = 1 ")
    AgencyInformationAuthorizedDTO getAuthorized(Long agencyId);
    @Query("select new com.neo.vas.dto.AgencyInformationContactDTO(au.id,au.fullName,au.birthday,au.position,au.email,au.telephone,au.phoneNumber) from AuthorizedPerson au where au.agencyIdPerson.id = ?1 and au.type = 2 ")
    AgencyInformationContactDTO getContact(Long agencyId);
}
