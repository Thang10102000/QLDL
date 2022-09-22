package com.neo.vas.export;

import com.neo.vas.dto.DetailReportDTO;
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

public class ExportDetailReport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<DetailReportDTO> dtoList;

    public ExportDetailReport(List<DetailReportDTO> dtoList) {
        this.dtoList = dtoList;
        workbook = new XSSFWorkbook();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Detail Report");
        Row row = sheet.createRow(0);
        Row row1 = sheet.createRow(1);
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
        Cell customerNameCell = row.createCell(0);
        customerNameCell.setCellValue("Tên khách hàng");
        customerNameCell.setCellStyle(style);

        Cell customerIdCell = row.createCell(1);
        customerIdCell.setCellValue("Mã khách hàng");
        customerIdCell.setCellStyle(style);

        Cell serviceRequestIdCell = row.createCell(2);
        serviceRequestIdCell.setCellValue("Mã yêu cầu dịch vụ");
        serviceRequestIdCell.setCellStyle(style);

        Cell areaNameCell = row.createCell(3);
        areaNameCell.setCellValue("Công ty khu vực-Trung tâm");
        areaNameCell.setCellStyle(style);

        Cell agencyCodeCell = row.createCell(4);
        agencyCodeCell.setCellValue("Mã đại lý");
        agencyCodeCell.setCellStyle(style);

        Cell amCodeCell = row.createCell(5);
        amCodeCell.setCellValue("Mã nhân viên AM/KAM");
        amCodeCell.setCellStyle(style);

        Cell createdDateCell = row.createCell(6);
        createdDateCell.setCellValue("Thời gian khởi tạo");
        createdDateCell.setCellStyle(style);

        Cell approvedDateCell = row.createCell(7);
        approvedDateCell.setCellValue("Thời gian duyệt");
        approvedDateCell.setCellStyle(style);

        Cell creatorCell = row.createCell(8);
        creatorCell.setCellValue("Tài khoản người duyệt");
        creatorCell.setCellStyle(style);

        Cell brandGroupNameCell = row.createCell(9);
        brandGroupNameCell.setCellValue("Nhóm gói cước");
        brandGroupNameCell.setCellStyle(style);

        Cell brandNameCell = row.createCell(10);
        brandNameCell.setCellValue("Gói cước");
        brandNameCell.setCellStyle(style);

        Cell totalOrderValueCell = row.createCell(11);
        Cell totalOrderValueCell1 = row.createCell(12);
        Cell totalOrderValueCell2 = row.createCell(13);
        totalOrderValueCell.setCellValue("Tổng giá trị đặt hàng");
        totalOrderValueCell.setCellStyle(style);
        totalOrderValueCell1.setCellStyle(style);
        totalOrderValueCell2.setCellStyle(style);


        Cell activatedCell = row.createCell(14);
        Cell activatedCell1 = row.createCell(15);
        Cell activatedCell2 = row.createCell(16);
        Cell activatedCell3 = row.createCell(17);
        Cell activatedCell4 = row.createCell(18);
        Cell activatedCell5 = row.createCell(19);
        activatedCell.setCellValue("Đã kích hoạt trong kỳ báo cáo");
        activatedCell.setCellStyle(style);
        activatedCell1.setCellStyle(style);
        activatedCell2.setCellStyle(style);
        activatedCell3.setCellStyle(style);
        activatedCell4.setCellStyle(style);
        activatedCell5.setCellStyle(style);

        Cell UnactivatedCell = row.createCell(20);
        Cell UnactivatedCell1 = row.createCell(21);
        UnactivatedCell.setCellValue("Chưa kích hoạt");
        UnactivatedCell.setCellStyle(style);
        UnactivatedCell1.setCellStyle(style);
     //row 1
        Cell aCell = row1.createCell(0);
        aCell.setCellStyle(style);
        Cell bCell = row1.createCell(1);
        bCell.setCellStyle(style);
        Cell cCell = row1.createCell(2);
        cCell.setCellStyle(style);
        Cell dCell = row1.createCell(3);
        dCell.setCellStyle(style);
        Cell eCell = row1.createCell(4);
        eCell.setCellStyle(style);
        Cell fCell = row1.createCell(5);
        fCell.setCellStyle(style);
        Cell gCell = row1.createCell(6);
        gCell.setCellStyle(style);
        Cell hCell = row1.createCell(7);
        hCell.setCellStyle(style);
        Cell iCell = row1.createCell(8);
        iCell.setCellStyle(style);
        Cell jCell = row1.createCell(9);
        jCell.setCellStyle(style);
        Cell kCell = row1.createCell(10);
        kCell.setCellStyle(style);

        Cell brandIdCell = row1.createCell(11);
        brandIdCell.setCellStyle(style);
        brandIdCell.setCellValue("Gói cước");

        Cell priceCell = row1.createCell(12);
        priceCell.setCellStyle(style);
        priceCell.setCellValue("Đơn giá (VNĐ)");

        Cell quantityCell = row1.createCell(13);
        quantityCell.setCellStyle(style);
        quantityCell.setCellValue("Số lượng");

        Cell rowNumCell = row1.createCell(14);
        rowNumCell.setCellStyle(style);
        rowNumCell.setCellValue("Mã yêu cầu kích hoạt");

        Cell activatedDateCell = row1.createCell(15);
        activatedDateCell.setCellStyle(style);
        activatedDateCell.setCellValue("Thời gian kích hoạt");

        Cell activatedQuantityCell = row1.createCell(16);
        activatedQuantityCell.setCellStyle(style);
        activatedQuantityCell.setCellValue("Số lượng");

        Cell amountCell = row1.createCell(17);
        amountCell.setCellStyle(style);
        amountCell.setCellValue("Thành tiền (VNĐ)");

        Cell discountAgencyCell = row1.createCell(18);
        discountAgencyCell.setCellStyle(style);
        discountAgencyCell.setCellValue("Chiết khấu đại lý");

        Cell costAmCell = row1.createCell(19);
        costAmCell.setCellStyle(style);
        costAmCell.setCellValue("Chi phí Am/Kam");

        Cell quantityUnactiCell = row1.createCell(20);
        quantityUnactiCell.setCellStyle(style);
        quantityUnactiCell.setCellValue("Số lượng");

        Cell amountUnactiCell = row1.createCell(21);
        amountUnactiCell.setCellStyle(style);
        amountUnactiCell.setCellValue("Thành tiền (VNĐ)");

        sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("B1:B2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("C1:C2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("D1:D2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("E1:E2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("F1:F2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("G1:G2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("H1:H2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("I1:I2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("J1:J2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("K1:K2"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("L1:N1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("O1:T1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("U1:V1"));
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

        int totalPriceDiscount=0;
        int totalPriceBrand=0;
        int totalQuantityActive=0;
        int totalQuantityNotActive=0;
        int totalCostAmKam=0;

        for (DetailReportDTO reportDTO : dtoList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, reportDTO.getCustomerName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getCustomerId(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getServiceRequestId(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAreaName(), bodyStyle);
            if (!reportDTO.getAgencyCode().isEmpty()){
                createCell(row, columnCount++, reportDTO.getAgencyCode(), bodyStyle);
            }else {
                createCell(row, columnCount++, "N/A", bodyStyle);
            }
            if (!reportDTO.getAmCode().isEmpty()){
                createCell(row, columnCount++, reportDTO.getAmCode(), bodyStyle);
            }else {
                createCell(row, columnCount++, "N/A", bodyStyle);
            }
            createCell(row, columnCount++, reportDTO.getCreatedDate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getApprovedDate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getCreator(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getBrandGroupName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getBrandName(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getBrandId(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getPrice(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantity(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getRowNum(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAsrCreatedDate(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityActivations(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAmount(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getDiscountAgency(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getCostAmKam(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getQuantityUnactivated(), bodyStyle);
            createCell(row, columnCount++, reportDTO.getAmountUnactivated(), bodyStyle);

            totalPriceBrand += reportDTO.getPrice();
            totalQuantityActive += reportDTO.getAmount();
            totalPriceDiscount += reportDTO.getDiscountAgency();
            totalCostAmKam += reportDTO.getCostAmKam();
            totalQuantityNotActive += reportDTO.getAmountUnactivated();
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
        createCell(totalRow, 8, "", style);
        createCell(totalRow, 9, "", style);
        createCell(totalRow, 10, "", style);
        createCell(totalRow, 11, "", style);
        createCell(totalRow, 12, totalPriceBrand, style);
        createCell(totalRow, 13, "", style);
        createCell(totalRow, 14, "", style);
        createCell(totalRow, 15, "", style);
        createCell(totalRow, 16, "", style);
        createCell(totalRow, 17, totalQuantityActive, style);
        createCell(totalRow, 18, totalPriceDiscount, style);
        createCell(totalRow, 19, totalCostAmKam, style);
        createCell(totalRow, 20, "", style);
        createCell(totalRow, 21, totalQuantityNotActive, style);
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
