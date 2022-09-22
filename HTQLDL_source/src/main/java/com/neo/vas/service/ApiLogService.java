package com.neo.vas.service;

import com.neo.vas.domain.LogAPI;
import org.springframework.data.domain.Page;


public interface ApiLogService {
    Page<LogAPI> searchApiLog(Long alId, Long module,
                              String startDate, String endDate, String creator, Long referenceId, int page, int size);
}
