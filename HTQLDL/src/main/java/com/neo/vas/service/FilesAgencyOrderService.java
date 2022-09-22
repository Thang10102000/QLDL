package com.neo.vas.service;

import com.neo.vas.domain.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesAgencyOrderService {
    List<Files> getAllByOrderId(Long id);
    void deleteById(Long id);
    List<Files> getContractFileBySrId(long srId);
    List<Files> getPayFileBySrId(long srId);
    List<Files> getScanFileBySrId(long srId);
}
