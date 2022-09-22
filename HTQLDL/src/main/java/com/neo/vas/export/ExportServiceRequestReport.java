package com.neo.vas.export;

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

public class ExportServiceRequestReport {
    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private List<ServiceRequestReportDTO> listReport;


    public ExportServiceRequestReport(List<ServiceRequestReportDTO> listReport) {
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
        createCell(row, 1, "Mã yêu cầu", style);
        createCell(row, 2, "Mã khách hàng", style);
        createCell(row, 3, "Dịch vụ", style);
        createCell(row, 4, "Nhóm gói cước", style);
        createCell(row, 5, "Gói cước", style);
        createCell(row, 6, "Chính sách", style);
        createCell(row, 7, "Đơn giá", style);
        createCell(row, 8, "Đơn giá chiết khấu đại lý", style);
        createCell(row, 9, "Số lượng", style);
        createCell(row, 10, "Thành tiền (VNĐ)", style);
        createCell(row, 11, "Số lượng gói đã kích hoạt", style);
        createCell(row, 12, "Số lượng gói chưa kích hoạt", style);
        createCell(row, 13, "Số lượng gói yêu cầu kích hoạt", style);
        createCell(row, 14, "Thời gian bắt đầu kích hoạt", style);
        createCell(row, 15, "Doanh thu theo gói yêu cầu kích hoạt", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        int totalPriceDiscount=0;
        int totalPriceBrand=0;
        int totalQuantityActive=0;
        int totalQuantityNotActive=0;
        int totalRevenue=0;

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
        for (ServiceRequestReportDTO lst : listReport) {
            count ++;
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, count, bodyStyle);
            createCell(row, columnCount++, lst.getServiceRequestId(), bodyStyle);
            createCell(row, columnCount++, lst.getCustomerId(), bodyStyle);
            createCell(row, columnCount++, lst.getService(), bodyStyle);
            createCell(row, columnCount++, lst.getBrandGroup(), bodyStyle);
            createCell(row, columnCount++, lst.getBrand(), bodyStyle);
            createCell(row, columnCount++, lst.getPolicy(), bodyStyle);
            createCell(row, columnCount++, lst.getPrice(), bodyStyle);
            createCell(row, columnCount++, lst.getPriceDiscount(), bodyStyle);
            createCell(row, columnCount++, lst.getQuantity(), bodyStyle);
            createCell(row, columnCount++, lst.getPriceBrand(), bodyStyle);
            createCell(row, columnCount++, lst.getQuantityActive(), bodyStyle);
            createCell(row, columnCount++, lst.getQuantityNotActive(), bodyStyle);
            createCell(row, columnCount++, lst.getQuantityRequestActive(), bodyStyle);
            createCell(row, columnCount++, lst.getActiveDate(), bodyStyle);
            createCell(row, columnCount++, lst.getRevenue(), bodyStyle);

//            tính tổng
            totalPriceDiscount += lst.getPriceDiscount();
            totalPriceBrand += lst.getPriceBrand();
            totalQuantityActive += lst.getQuantityActive();
            totalQuantityNotActive += lst.getQuantityNotActive();
            totalRevenue += lst.getRevenue();
        }

//        tính tổng

        int totalIndex = rowCount + 1;
        Row totalRow = sheet.createRow(rowCount);
        sheet.addMergedRegion(CellRangeAddress.valueOf("A"+totalIndex+":H"+totalIndex));
        createCell(totalRow, 0, "Tổng", style);
        createCell(totalRow, 1, "", style);
        createCell(totalRow, 2, "", style);
        createCell(totalRow, 3, "", style);
        createCell(totalRow, 4, "", style);
        createCell(totalRow, 5, "", style);
        createCell(totalRow, 6, "", style);
        createCell(totalRow, 7, "", style);
        createCell(totalRow, 8, totalPriceDiscount, style);
        createCell(totalRow, 9, "", style);
        createCell(totalRow, 10, totalPriceBrand, style);
        createCell(totalRow, 11, totalQuantityActive, style);
        createCell(totalRow, 12, totalQuantityNotActive, style);
        createCell(totalRow, 13, "", style);
        createCell(totalRow, 14, "", style);
        createCell(totalRow, 15, totalRevenue, style);

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
