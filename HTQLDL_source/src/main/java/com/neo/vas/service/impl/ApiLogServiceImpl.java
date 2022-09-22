package com.neo.vas.service.impl;

import com.neo.vas.domain.LogAPI;
import com.neo.vas.repository.ApiLogRepository;
import com.neo.vas.service.ApiLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ApiLogServiceImpl implements ApiLogService {
    @Autowired
    private ApiLogRepository apiLogRepository;


    @Override
    public Page<LogAPI> searchApiLog(Long alId, Long module, String startDate, String endDate, String creator,Long referenceId, int page, int size) {
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("alId").descending());
            // Th1 có start va end date
            if (!startDate.isEmpty() && !endDate.isEmpty()){
//                if (!alId.isEmpty() || !module.isEmpty() || !creator.isEmpty()){
                    Date start = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(startDate.trim());
                    Date end =  new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(endDate.trim());
                    return apiLogRepository.findAllLogApi(alId, module, start, end, creator,referenceId, pageable);
//                }
            }
            // Th2 khong co end date
            if (!startDate.isEmpty() && endDate.isEmpty()){
//                if (alId != null || !module.isEmpty() || !creator.isEmpty()){
                    Date start = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(startDate.trim());
                    return apiLogRepository.findALlLogApiStart(alId, module, start, creator,referenceId, pageable);
//                }
            }
            // Th3 khong co start date
            if (startDate.isEmpty() && !endDate.isEmpty()){
//                if (alId != null || !module.isEmpty() || !creator.isEmpty()){
                    Date end =  new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(endDate.trim());
                    return apiLogRepository.findAllLogApoEnd(alId, module, end, creator,referenceId, pageable);

//                }
            }
            // Th4 khong co date
            if (startDate.isEmpty() && endDate.isEmpty()){
//                if (alId != null || module != null || !creator.isEmpty()){
                    return apiLogRepository.findAllLogApiNoDate(alId, module, creator,referenceId, pageable);
//                }
            }
            return apiLogRepository.findAll(pageable);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
