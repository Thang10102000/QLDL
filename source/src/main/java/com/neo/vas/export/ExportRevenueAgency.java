package com.neo.vas.export;

import com.neo.vas.dto.ReportRevenueDTO;
import com.neo.vas.dto.ServiceRequestReportDTO;
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

public class ExportRevenueAgency {
    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private List<ReportRevenueDTO> listReport;


    public ExportRevenueAgency(List<ReportRevenueDTO> listReport) {
        this.listReport = listReport;
        wb = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = wb.createSheet("Report");

        Row row = sheet.createRow(0);

        XSSFFont titleFont =  wb.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = wb.createFont();
        //titleFontHead.setBold(true);
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style=wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        sheet.setDefaultColumnWidth(18);

        createCell(row, 0, "STT", style);
        createCell(row, 1, "Dịch vụ", style);
        createCell(row, 2, "Mã Đại lý", style);
        createCell(row, 3, "Tên nhóm gói cước", style);
        createCell(row, 4, "Gói cước", style);
        createCell(row, 5, "Số lượng đặt hàng mới", style);
        createCell(row, 6, "Số lượng gói cước đặt trong một kì", style);
        createCell(row, 7, "Số lượng gói cước đã kích hoạt trong kì", style);
        createCell(row, 8, "Số lượng gói cước chưa kích hoạt trong kì", style);
        createCell(row, 9, "Tổng giá trị yêu cầu dịch vụ", style);
        createCell(row, 10, "Doanh thu gói đã kích hoạt", style);
        createCell(row, 11, "Mức chiết khấu (%)", style);

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
        int totalMoney=0;
        int totalService=0;
        int totalDicountRate=0;

        XSSFFont titleFont =  wb.createFont();
        titleFont.setBold(true);
        XSSFFont titleFontHead = wb.createFont();
        titleFontHead.setFontHeightInPoints((short) 20);

        XSSFCellStyle style=wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFont(titleFont);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //Style body
        XSSFCellStyle bodyStyle=wb.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        //style.setFont(titleFont);
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        int count = 0;
        for (ReportRevenueDTO lst : listReport) {
            count ++;
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, count, bodyStyle);
            createCell(row, columnCount++, lst.getServiceName(), bodyStyle);
            createCell(row, columnCount++, lst.getAgencyCode(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandGroupName(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandName(), bodyStyle);
            createCell(row, columnCount++, lst.getQuantity(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandNew(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandEt(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandOff(), bodyStyle);
            createCell(row, columnCount++, lst.getTotalMoney(), bodyStyle);
            createCell(row, columnCount++, lst.getCountBrand(), bodyStyle);
            createCell(row, columnCount++, lst.getDiscountRate(), bodyStyle);

//            tính tổng
            totalMoney += lst.getTotalMoney();
            totalService += lst.getCountBrand();
            totalDicountRate += lst.getDiscountRate();
        }

//        tính tổng

        int totalIndex = rowCount + 1;
        Row totalRow = sheet.createRow(rowCount);
//        sheet.addMergedRegion(CellRangeAddress.valueOf("A"+totalIndex+":F"+totalIndex));
        createCell(totalRow, 0, "Tổng", style);
        createCell(totalRow, 1, "", style);
        createCell(totalRow, 2, "", style);
        createCell(totalRow, 3, "", style);
        createCell(totalRow, 4, "", style);
        createCell(totalRow, 5, "", style);
        createCell(totalRow, 6, "", style);
        createCell(totalRow, 7, "", style);
        createCell(totalRow, 8, "", style);
        createCell(totalRow, 9, totalMoney, style);
        createCell(totalRow, 10, totalService, style);
        createCell(totalRow, 11, totalDicountRate, style);

    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        wb.close();

        outputStream.close();

    }
}
