package com.neo.vas.repository;

import com.neo.vas.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    @Query("select cus from Customer cus where (?1 is null or lower(cus.name) like %?1%) and (?2 is null or cus.areaCId.areaId = ?2)" +
            " and (?3 is null or cus.schoolId.id  = ?3) and (?4 is null or cus.agencyCode = ?4 ) order by cus.createdDate desc")
    Page<Customer> searchCustomer(String name, String area, String school, String agencyCode, Pageable pageable);

    Customer findCustomerById(long id);
    @Query("select c from Customer c where c.areaCId.areaId = ?1 ")
    List<Customer> getCustomerByAreaId(Long areaId);
}
