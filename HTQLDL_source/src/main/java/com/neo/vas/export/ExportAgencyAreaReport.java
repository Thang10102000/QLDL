package com.neo.vas.export;

import com.neo.vas.dto.AgencyAreaReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExportAgencyAreaReport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<AgencyAreaReportDTO> dtoList;

    public ExportAgencyAreaReport(List<AgencyAreaReportDTO> dtoList) {
        this.dtoList = dtoList;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Commission Report");
        Row row = sheet.createRow(0);
        Row row1 = sheet.createRow(1);
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = workbook.createFont();
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        sheet.setDefaultColumnWidth(22);

        createCell(row, 0, "Công ty khu vực", style);
        createCell(row, 1, "Tổng giá trị đặt hàng", style);
        createCell(row, 2, "Doanh thu gói đã kích hoạt", style);

        createCell(row, 3, "Đại lý chiết khấu", style);
        createCell(row, 4, "", style);
        createCell(row, 5, "", style);
        createCell(row, 6, "", style);
        createCell(row, 7, "", style);
        createCell(row, 8, "", style);
        createCell(row, 9, "", style);

        createCell(row, 10, "Nhân viên AM/KAM", style);
        createCell(row, 11, "", style);
        createCell(row, 12, "", style);
        createCell(row, 13, "", style);
        createCell(row, 14, "", style);
        createCell(row, 15, "", style);
        createCell(row, 16, "", style);
        createCell(row, 17, "", style);

        //row1
        createCell(row1, 0, "", style);
        createCell(row1, 1, "", style);
        createCell(row1, 2, "", style);

        createCell(row1, 3, "Số lượng đơn hàng mới", style);
        createCell(row1, 4, "Số lượng đặt hàng (đơn hàng mới)", style);
        createCell(row1, 5, "Số lượng gói cước đã kích hoạt trong kỳ", style);
        createCell(row1, 6, "Số lượng gói cước chưa kích hoạt trong kỳ", style);
        createCell(row1, 7, "Tổng giá trị đặt hàng", style);
        createCell(row1, 8, "Doanh thu gói đã kích hoạt", style);
        createCell(row1, 9, "Mức chiết khấu đại lý (%)", style);

        createCell(row1, 10, "Số lượng đơn đặt hàng mới", style);
        createCell(row1, 11, "Số lượng đặt hàng (đơn hàng mới)", style);
        createCell(row1, 12, "Số lượng gói cước đã kích hoạt trong kỳ", style);
        createCell(row1, 13, "Số lượng gói cước chưa kích hoạt trong kỳ", style);
        createCell(row1, 14, "Tổng giá trị đặt hàng", style);
        createCell(row1, 15, "Doanh thu gói đã kích hoạt", style);
        createCell(row1, 16, "Mức chi phí hoa hồng Am/Kam (%)", style);
        createCell(row1, 17, "Tổng chi phí hoa hồng dịch vụ", style);

        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("B1:B2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C1:C2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("D1:J1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("K1:R1"));
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
        int rowCount = 2;

        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = workbook.createFont();
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //Style body
        XSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        //style.setFont(titleFont);
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        int total1=0; int total2=0; int total3=0; int total4=0; int total5=0; int total6=0;
        int total12=0; int total11=0; int total10=0; int total9=0; int total8=0; int total7=0;
        int total13=0; int total14=0; int total15=0; int total16=0; int total17=0;

        for (AgencyAreaReportDTO reportDTO : dtoList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, reportDTO.getAreaName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_amount(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_revenue_brand_activated(), bodyStyle);
            //
            createCell(row, columnCount++, reportDTO.getSum_quantity_ck(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_new_ck(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_brand_activated(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_brand_unactivated(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_amount_ck(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_revenue_brand_acti_ck(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAgencyDiscount(), bodyStyle);
            //
            createCell(row, columnCount++, reportDTO.getSum_quantity_hh(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_new_hh(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_brand_acti(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_quantity_brand_unacti(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_amount_hh(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getSum_revenue_brand_acti_ck(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getCommission_rate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getTotal_cost_of_service_commission(), bodyStyle);

            total1 += reportDTO.getSum_amount();
            total2 += reportDTO.getSum_revenue_brand_activated();
            //
            total3 += reportDTO.getSum_quantity_ck();
            total4 += reportDTO.getSum_quantity_new_ck();
            total5 += reportDTO.getSum_quantity_brand_activated();
            total6 += reportDTO.getSum_quantity_brand_unactivated();
            total7 += reportDTO.getSum_amount_ck();
            total8 += reportDTO.getSum_revenue_brand_acti_ck();
            total9 += reportDTO.getAgencyDiscount();
            //
            total10 += reportDTO.getSum_quantity_hh();
            total11 += reportDTO.getSum_quantity_new_hh();
            total12 += reportDTO.getSum_quantity_brand_acti();
            total13 += reportDTO.getSum_quantity_brand_unacti();
            total14 += reportDTO.getSum_amount_hh();
            total15 += reportDTO.getSum_revenue_brand_acti_ck();
            total16 += reportDTO.getCommission_rate();
            total17 += reportDTO.getTotal_cost_of_service_commission();
        }

        Row totalRow = sheet.createRow(rowCount);
        createCell(totalRow, 0, "Tổng", style);
        createCell(totalRow, 1, total1, style);
        createCell(totalRow, 2, total2, style);
        createCell(totalRow, 3, total3, style);
        createCell(totalRow, 4, total4, style);
        createCell(totalRow, 5, total5, style);
        createCell(totalRow, 6, total6, style);
        createCell(totalRow, 7, total7, style);
        createCell(totalRow, 8, total8, style);
        createCell(totalRow, 9, total9, style);
        createCell(totalRow, 10, total10, style);
        createCell(totalRow, 11, total11, style);
        createCell(totalRow, 12, total12, style);
        createCell(totalRow, 13, total13, style);
        createCell(totalRow, 14, total14, style);
        createCell(totalRow, 15, total15, style);
        createCell(totalRow, 16, total16, style);
        createCell(totalRow, 17, total17, style);
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
