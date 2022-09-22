package com.neo.vas.service.impl;

import com.neo.vas.domain.Files;
import com.neo.vas.repository.FilesAgencyOrderRepository;
import com.neo.vas.service.FilesAgencyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilesAgencyOrderServiceImpl implements FilesAgencyOrderService {
    @Autowired
    private FilesAgencyOrderRepository filesAgencyOrderRepository;

    @Override
    public List<Files> getAllByOrderId(Long id) {
        return filesAgencyOrderRepository.getFileByOrderId(id);
    }

    @Override
    public void deleteById(Long id) {
        this.filesAgencyOrderRepository.deleteById(id);
    }

    @Override
    public List<Files> getContractFileBySrId(long srId) {
        return filesAgencyOrderRepository.getContractFileBySrId(srId);
    }

    @Override
    public List<Files> getPayFileBySrId(long srId) {
        return filesAgencyOrderRepository.getPayFileBySrId(srId);
    }

    @Override
    public List<Files> getScanFileBySrId(long srId) {
        return filesAgencyOrderRepository.getScanFileBySrId(srId);
    }
}
