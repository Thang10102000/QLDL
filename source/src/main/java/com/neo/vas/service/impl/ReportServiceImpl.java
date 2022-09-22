package com.neo.vas.service.impl;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.neo.vas.config.DataSourceConnection;
import com.neo.vas.dto.AgencyAreaReportDTO;
import com.neo.vas.dto.CommissionReportDTO;
import com.neo.vas.dto.DetailReportDTO;
import com.neo.vas.dto.ServiceRequestReportDTO;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.internal.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.Agency;
import com.neo.vas.repository.ReportRepository;
import com.neo.vas.service.ReportService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	public ReportServiceImpl() throws SQLException {
	}

	@Override
	public List<Agency> getReport() {
		// TODO Auto-generated method stub
		return reportRepository.getReport();
	}

	@Override
	@Transactional
	public List<DetailReportDTO> searchServiceRequest(DetailReportDTO dto){
		Connection conn = null;
		try {
			conn = DataSourceConnection.getConnection();
			CallableStatement csmt =  conn.prepareCall("{ ? = call PK_REPORT_DETAIL.REPORT_DETAIL(?,?,?,?,?,?,?,?,?) }");
			csmt.registerOutParameter(1, OracleTypes.CURSOR);
			csmt.setString(2, dto.getBrandGroupName());
			csmt.setString(3, dto.getBrandName());
			csmt.setString(4, dto.getAreaName());
			csmt.setString(5, dto.getAgencyCode());
			csmt.setString(6, dto.getCreatedDate());
			csmt.setString(7, dto.getEndDate());
			csmt.setString(8, dto.getApprovedDate());
			csmt.setString(9, dto.getDateEnd());
			csmt.setInt(10, dto.getReportMonth());
			csmt.execute();
			ResultSet rs = (ResultSet) csmt.getObject(1);
			List<DetailReportDTO> dtoList = new ArrayList<>();
			while (rs.next()){
				DetailReportDTO reportDTO = new DetailReportDTO();
				reportDTO.setCustomerName(rs.getString(1));
				reportDTO.setCustomerId(rs.getLong(2));
				reportDTO.setServiceRequestId(rs.getLong(3));
				reportDTO.setAreaName(rs.getString(4));
				if (rs.getInt(6) == 0){
					reportDTO.setAgencyCode(rs.getString(5));
				}else {
					reportDTO.setAgencyCode("");
				}
				if(rs.getInt(6) == 1){
					reportDTO.setAmCode(rs.getString(5));
				}else {
					reportDTO.setAmCode("");
				}
				reportDTO.setCreatedDate(rs.getString(7));
				reportDTO.setApprovedDate(rs.getString(8));
				reportDTO.setCreator(rs.getString(9));
				reportDTO.setBrandGroupName(rs.getString(10));
				reportDTO.setBrandName(rs.getString(11));
				reportDTO.setBrandId(rs.getLong(12));
				reportDTO.setPrice(rs.getInt(13));
				reportDTO.setQuantity(rs.getInt(14));
				reportDTO.setRowNum(rs.getInt(15));
				reportDTO.setAsrCreatedDate(rs.getString(16));
				reportDTO.setQuantityActivations(rs.getInt(17));
				reportDTO.setAmount(rs.getInt(18));
				reportDTO.setDiscountAgency(rs.getInt(19));
				reportDTO.setCostAmKam(rs.getInt(20));
				reportDTO.setQuantityUnactivated(rs.getInt(21));
				reportDTO.setAmountUnactivated(rs.getInt(22));
				dtoList.add(reportDTO);
			}

			return dtoList;
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}finally {
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
	}

	@Override
	@Transactional
	public List<CommissionReportDTO> searchCommissionReport(CommissionReportDTO dto){
		Connection conn = null;
		try {
			conn = DataSourceConnection.getConnection();
			CallableStatement csmt =  conn.prepareCall("{ ? = call PK_REPORT_COMMISSION.REPORT_COMMISSION(?,?,?,?,?,?,?,?,?) }");
			csmt.registerOutParameter(1, OracleTypes.CURSOR);
			csmt.setString(2, dto.getBrandGrName());
			csmt.setString(3, dto.getBrandName());
			csmt.setString(4, dto.getAreaName());
			csmt.setString(5, dto.getAgencyCode());
			csmt.setString(6, dto.getCreatedDate());
			csmt.setString(7, dto.getEndDate());
			csmt.setString(8, dto.getApprovedDate());
			csmt.setString(9, dto.getApprovalEndDate());
			csmt.setInt(10, dto.getReportMonth());
			csmt.execute();
			ResultSet rs = (ResultSet) csmt.getObject(1);
			List<CommissionReportDTO> dtoList = new ArrayList<>();
			while (rs.next()){
				CommissionReportDTO rsDTO = new CommissionReportDTO();
				rsDTO.setServiceName(rs.getString(1));
				rsDTO.setAgencyCode(rs.getString(2));
				rsDTO.setBrandGrName(rs.getString(3));
				rsDTO.setBrandName(rs.getString(4));
				rsDTO.setQuantityNewOrder(rs.getInt(5));
				rsDTO.setQuantityNewBrand(rs.getInt(6));
				rsDTO.setQuantityActivationsBrand(rs.getInt(7));
				rsDTO.setQuantityUnactivatedBrand(rs.getInt(8));
				rsDTO.setQuantityServiceRequestActivate(rs.getInt(9));
				rsDTO.setRevenueBrand(rs.getInt(10));
				rsDTO.setCommissionRate(rs.getInt(11));
				rsDTO.setTotalCost(rs.getInt(12));
				dtoList.add(rsDTO);
			}
			return dtoList;
		}catch (SQLException throwables){
			throwables.printStackTrace();
		}finally {
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<ServiceRequestReportDTO> searchServiceRequestReport(String serviceId,String brandGId,String brandId, String startDate, String endDate,
																	String serviceRequestId, String agencyCode, String customerId) {
		Connection conn = null;
		try {

			conn = DataSourceConnection.getConnection();
			CallableStatement csmt =  conn.prepareCall("{ ? = call PK_REPORT_SERVICE_REQUEST.REPORT_SERVICE_REQUEST(?,?,?,?,?,?,?,?) }");
			csmt.registerOutParameter(1, OracleTypes.CURSOR);

			csmt.setString(2, serviceId);
			csmt.setString(3, brandGId);
			csmt.setString(4, brandId);
			csmt.setString(5, startDate);
			csmt.setString(6, endDate);
			csmt.setString(7, customerId);
			csmt.setString(8, serviceRequestId);
			csmt.setString(9, agencyCode);
			csmt.execute();
			ResultSet rs = (ResultSet) csmt.getObject(1);
			List<ServiceRequestReportDTO> dtoList = new LinkedList<>();
			while (rs.next()){
				Date activeDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(rs.getString(14));
				ServiceRequestReportDTO rsDTO = new ServiceRequestReportDTO();
				rsDTO.setServiceRequestId(rs.getLong(1));
				rsDTO.setCustomerId(rs.getLong(2));
				rsDTO.setService(rs.getString(3));
				rsDTO.setBrandGroup(rs.getString(4));
				rsDTO.setBrand(rs.getString(5));
				rsDTO.setPolicy(rs.getString(6));
				rsDTO.setPrice(rs.getString(7));
				rsDTO.setPriceDiscount(rs.getInt(8));
				rsDTO.setQuantity(rs.getInt(9));
				rsDTO.setPriceBrand(rs.getInt(10));
				rsDTO.setQuantityActive(rs.getInt(11));
				rsDTO.setQuantityNotActive(rs.getInt(12));
				rsDTO.setQuantityRequestActive(rs.getInt(13));
				rsDTO.setActiveDate(rs.getString(14));
				if(activeDate.before(new Date())){
					rsDTO.setRevenue(rs.getInt(15));
				}else {
					rsDTO.setRevenue(0);
				}
				rsDTO.setCustomerName(rs.getString(16));

				dtoList.add(rsDTO);

			}
			return dtoList;
		}catch (SQLException | ParseException throwables){
			throwables.printStackTrace();
		}finally {
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException throwables) {

			}
		}
		return null;
	}

	@Override
	public List<AgencyAreaReportDTO> searchAgencyAreaReport(AgencyAreaReportDTO dto){
		Connection conn = null;
		try {
			conn = DataSourceConnection.getConnection();
			CallableStatement csmt = conn.prepareCall("{ ? = call PK_REPORT_AGENCY_AREA.REPORT_AGENCY_AREA(?,?,?,?,?,?,?,?) }");
			csmt.registerOutParameter(1, OracleTypes.CURSOR);
			csmt.setString(2, dto.getBrandGrName());
			csmt.setString(3, dto.getBrandName());
			csmt.setString(4, dto.getAreaName());
			csmt.setString(5, dto.getCreatedDate());
			csmt.setString(6, dto.getEndDate());
			csmt.setString(7, dto.getApprovedDate());
			csmt.setString(8, dto.getApprovedEndDate());
			csmt.setInt(9, dto.getReportMonth());
			csmt.execute();
			ResultSet rs = (ResultSet) csmt.getObject(1);
			List<AgencyAreaReportDTO> dtoList = new ArrayList<>();
			while (rs.next()){
				AgencyAreaReportDTO reportDTO = new AgencyAreaReportDTO();
				reportDTO.setAreaName(rs.getString(1));// đặt sai tên, đây là areaName
				reportDTO.setSum_amount(rs.getInt(2));
				reportDTO.setSum_revenue_brand_activated(rs.getInt(3));
				//
				reportDTO.setSum_quantity_ck(rs.getInt(4));
				reportDTO.setSum_quantity_new_ck(rs.getInt(5));
				reportDTO.setSum_quantity_brand_activated(rs.getInt(6));
				reportDTO.setSum_quantity_brand_unactivated(rs.getInt(7));
				reportDTO.setSum_amount_ck(rs.getInt(8));
				reportDTO.setSum_revenue_brand_acti_ck(rs.getInt(9));
				reportDTO.setAgencyDiscount(rs.getInt(10));
				//
				reportDTO.setSum_quantity_hh(rs.getInt(11));
				reportDTO.setSum_quantity_new_hh(rs.getInt(12));
				reportDTO.setSum_quantity_brand_acti(rs.getInt(13));
				reportDTO.setSum_quantity_brand_unacti(rs.getInt(14));
				reportDTO.setSum_amount_hh(rs.getInt(15));
				reportDTO.setSum_revenue_brand_acti_ck(rs.getInt(16));
				reportDTO.setCommission_rate(rs.getInt(17));
				reportDTO.setTotal_cost_of_service_commission(rs.getInt(18));
				dtoList.add(reportDTO);
			}
			return dtoList;
		}catch (SQLException throwables){
			throwables.printStackTrace();
		}finally {
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}return null;
	}

}
