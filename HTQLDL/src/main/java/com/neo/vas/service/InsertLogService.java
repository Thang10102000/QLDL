package com.neo.vas.service;

import java.security.Principal;
import java.sql.SQLException;

/**
 * project_name: vasonline
 * Created_by: thulv
 * time: 06/07/2021
 */
public interface InsertLogService {
    boolean insertLog(String username , String path, int privilegeId, String description) throws SQLException;
}
