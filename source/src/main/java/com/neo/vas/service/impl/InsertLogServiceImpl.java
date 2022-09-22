package com.neo.vas.service.impl;

import com.neo.vas.config.DataSourceConnection;
import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.service.InsertLogService;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


/**
 * project_name: vasonline
 * Created_by: thulv
 * time: 06/07/2021
 */
@Service
public class InsertLogServiceImpl implements InsertLogService {

    @Autowired
    private SystemFunctionalRepository sfRepository;


    @Override
    public boolean insertLog(String username, String path, int privilegeId, String description){
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt = conn.prepareCall("{ ? = call PK_LOG.INSERT_LOG(?,?,?,?) }");

            List<SystemFunctional> lstSF = sfRepository.findAll();
            csmt.registerOutParameter(1, OracleTypes.VARCHAR);
            csmt.setString(2, username);
            for (SystemFunctional sf : lstSF) {
                if (path.equals(sf.getUrlController())) {
                    csmt.setLong(3, sf.getSfId());
                }
            }
            csmt.setInt(4, privilegeId);
            csmt.setString(5, description);
            csmt.execute();
            System.out.println("insert log success");
            return true;
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("insert log failed");
            return false;
        }finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
