package com.neo.vas.export;

import com.neo.vas.dto.CommissionReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExportCommissionReport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<CommissionReportDTO> dtoList;

    public ExportCommissionReport(List<CommissionReportDTO> dtoList) {
        this.dtoList = dtoList;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Commission Report");
        Row row = sheet.createRow(0);
        XSSFFont titleFont =  workbook.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = workbook.createFont();
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style=workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        sheet.setDefaultColumnWidth(22);
        // row
        createCell(row, 0, "Dịch vụ", style);
        createCell(row, 1, "Mã Am/Kam", style);
        createCell(row, 2, "Tên nhóm gói cước", style);
        createCell(row, 3, "Gói cước", style);
        createCell(row, 4, "Số lượng đặt hàng (đơn hàng mới)", style);
        createCell(row, 5, "Số lượng gói cước yêu cầu mới trong kỳ", style);
        createCell(row, 6, "Số lượng gói cước đã kích hoạt trong kỳ", style);
        createCell(row, 7, "Số lượng gói cước chưa kích hoạt trong kỳ", style);
        createCell(row, 8, "Số lượng đặt hàng (đơn hàng mới)", style);
        createCell(row, 9, "Doanh thu gói đã kích hoạt", style);
        createCell(row, 10, "Mức chi phí hoa hồng Am/Kam", style);
        createCell(row, 11, "Tổng chi phí hoa hồng dịch vụ", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        XSSFFont titleFont =  workbook.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = workbook.createFont();
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style=workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //Style body
        XSSFCellStyle bodyStyle=workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        //style.setFont(titleFont);
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        int totalNewOrder = 0;
        int totalRevenue = 0;
        int totalCommissionRate = 0;
        int totaltotalCost = 0;

        for (CommissionReportDTO reportDTO : dtoList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, reportDTO.getServiceName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAgencyCode(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getBrandGrName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getBrandName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityNewOrder(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityNewBrand(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityActivationsBrand(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityUnactivatedBrand(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityServiceRequestActivate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getRevenueBrand(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getCommissionRate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getTotalCost(), bodyStyle);

            totalNewOrder += reportDTO.getQuantityServiceRequestActivate();
            totalRevenue += reportDTO.getRevenueBrand();
            totalCommissionRate += reportDTO.getCommissionRate();
            totaltotalCost += reportDTO.getTotalCost();
        }

        Row totalRow = sheet.createRow(rowCount);
        createCell(totalRow, 0, "Tổng", style);
        createCell(totalRow, 1, "", style);
        createCell(totalRow, 2, "", style);
        createCell(totalRow, 3, "", style);
        createCell(totalRow, 4, "", style);
        createCell(totalRow, 5, "", style);
        createCell(totalRow, 6, "", style);
        createCell(totalRow, 7, "", style);
        createCell(totalRow, 8, totalNewOrder, style);
        createCell(totalRow, 9, totalRevenue, style);
        createCell(totalRow, 10, totalCommissionRate, style);
        createCell(totalRow, 11, totaltotalCost, style);
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
