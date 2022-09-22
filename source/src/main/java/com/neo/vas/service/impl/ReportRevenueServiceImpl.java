package com.neo.vas.service.impl;

import com.neo.vas.config.DataSourceConnection;
import com.neo.vas.domain.Agency;
import com.neo.vas.dto.ReportRevenueDTO;
import com.neo.vas.service.ReportRevenueService;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Service
public class ReportRevenueServiceImpl implements ReportRevenueService {

    @Override
    public List<Agency> getReport() {
        return null;
    }

    @Override
    public Page<ReportRevenueDTO> searchServiceRequest(ReportRevenueDTO dto, int page, int size) {
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt =  conn.prepareCall("{ ? = call PK_REPORT_AGENCY_REVENUE.GET_REPORT_REVENUE(?,?,?,?,?,?,?,?,?) }");
            csmt.registerOutParameter(1, OracleTypes.CURSOR);
            csmt.setString(2, dto.getBrandGroupId());
            csmt.setString(3, dto.getBrandId());
            csmt.setString(4, dto.getCostAmKam());
            csmt.setString(5, dto.getAreaId());
            csmt.setString(6, dto.getCreatedDate());
            csmt.setString(7, dto.getEndDate());
            csmt.setString(8, dto.getApprovedDate());
            csmt.setString(9, dto.getFromDate());
            csmt.setInt(10, dto.getAmountUnactivated());
            csmt.execute();
            ResultSet rs = (ResultSet)csmt.getObject(1);
            List<ReportRevenueDTO> dtoList = new ArrayList<>();
            while (rs.next()){
                ReportRevenueDTO reportDTO = new ReportRevenueDTO();
                reportDTO.setServiceName(rs.getString(1));
                reportDTO.setAgencyCode(rs.getString(2));
                reportDTO.setBrandGroupName(rs.getString(3));
                reportDTO.setBrandName(rs.getString(4));
                reportDTO.setQuantity(rs.getInt(5));
                reportDTO.setBrandNew(rs.getInt(6));
                reportDTO.setBrandEt(rs.getInt(7));
                reportDTO.setBrandOff(rs.getInt(8));
                reportDTO.setTotalMoney(rs.getLong(9));
                reportDTO.setCountBrand(rs.getInt(10));
                reportDTO.setDiscountRate(rs.getInt(11));
                dtoList.add(reportDTO);
            }
//			}
            Pageable pageable = PageRequest.of(page,size);
            return new PageImpl<>(dtoList,pageable,dtoList.size());
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return null;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {

                }
            }
        }

    }

    @Override
    public List<ReportRevenueDTO> searchServiceRequest(ReportRevenueDTO dto) {
        Connection conn = null;
        try {
            conn = DataSourceConnection.getConnection();
            CallableStatement csmt =  conn.prepareCall("{ ? = call PK_REPORT_AGENCY_REVENUE.GET_REPORT_REVENUE(?,?,?,?,?,?,?,?,?) }");
            csmt.registerOutParameter(1, OracleTypes.CURSOR);
            csmt.setString(2, dto.getBrandGroupId());
            csmt.setString(3, dto.getBrandId());
            csmt.setString(4, dto.getCostAmKam());
            csmt.setString(5, dto.getAreaId());
            csmt.setString(6, dto.getCreatedDate());
            csmt.setString(7, dto.getEndDate());
            csmt.setString(8, dto.getApprovedDate());
            csmt.setString(9, dto.getFromDate());
            csmt.setInt(10, dto.getAmountUnactivated());
            csmt.execute();
            ResultSet rs = (ResultSet)csmt.getObject(1);
            List<ReportRevenueDTO> dtoList = new ArrayList<>();
            while (rs.next()){
                ReportRevenueDTO reportDTO = new ReportRevenueDTO();
                reportDTO.setServiceName(rs.getString(1));
                reportDTO.setAgencyCode(rs.getString(2));
                reportDTO.setBrandGroupName(rs.getString(3));
                reportDTO.setBrandName(rs.getString(4));
                reportDTO.setQuantity(rs.getInt(5));
                reportDTO.setBrandNew(rs.getInt(6));
                reportDTO.setBrandEt(rs.getInt(7));
                reportDTO.setBrandOff(rs.getInt(8));
                reportDTO.setTotalMoney(rs.getLong(9));
                reportDTO.setCountBrand(rs.getInt(10));
                reportDTO.setDiscountRate(rs.getInt(11));
                dtoList.add(reportDTO);
            }
            return dtoList;
        } catch (Exception throwables) {
            throwables.printStackTrace();
            return null;
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {

                }
            }
        }

    }
}
