package vn.com.telsoft.controller;

import com.faplib.admin.security.AdminUser;
import com.faplib.applet.util.StringUtil;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import org.apache.axis.utils.Admin;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hsqldb.lib.HashMap;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import vn.com.telsoft.entity.FileUpload;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.MatchedTransaction;
import vn.com.telsoft.entity.MismatchedTransaction;
import vn.com.telsoft.model.MatchedTransactionModel;
import vn.com.telsoft.model.MismatchedTransactionModel;
import vn.com.telsoft.model.UploadAdjustedFileModel;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@ViewScoped
public class UploadAdjustedFileController extends TSFuncTemplate implements Serializable {
    private UploadAdjustedFileModel uploadAdjustedFileModel;
    private MismatchedTransactionModel mismatchedTransactionModel;
    private MatchedTransactionModel matchedTransactionModel;

    public UploadAdjustedFileController() throws Exception {
        uploadAdjustedFileModel = new UploadAdjustedFileModel();
        this.mismatchedTransactionModel = new MismatchedTransactionModel();
        this.matchedTransactionModel = new MatchedTransactionModel();
    }

    @Override
    public void handleOK() throws Exception {
        ClientMessage.log(ClientMessage.MESSAGE_TYPE.INF, "Upload file thành công!");
    }

    @Override
    public void handleDelete() throws Exception {

    }

    public void handleFileUpload(FileUploadEvent event) throws Exception {

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(event.getFile().getInputstream());
        Sheet mismatchedSheet = xssfWorkbook.getSheet("GDCL");
        Sheet matchedSheet = xssfWorkbook.getSheet("GDCK");
        Sheet ticketSheet = xssfWorkbook.getSheet("TICKET");
        Sheet updateIncorrectFeeTransactionSheet = xssfWorkbook.getSheet("GDSLP");

        String uploadMessage = "";

        // Handle matched transactions
        if (matchedSheet != null) {
            // If file uploaded by CV then update process status only
            if (AdminUser.getUserLogged().getTitle() == 0) {
                processMatchedTransaction(matchedSheet);
                uploadMessage = "Upload file Điều chỉnh giao dịch thành công!";
            } else if (AdminUser.getUserLogged().getTitle() == 1) { // If file uploaded by Leader then update confirm status only
                confirmMatchedTransaction(matchedSheet);
                uploadMessage = "Upload file Xác nhận điều chỉnh thành công!";
            }
        } else if (mismatchedSheet != null) {
            // If file uploaded by CV then update process status only
            if (AdminUser.getUserLogged().getTitle() == 0) {
                processMismatchedTransaction(mismatchedSheet);
                uploadMessage = "Upload file Điều chỉnh giao dịch thành công!";
            } else if (AdminUser.getUserLogged().getTitle() == 1) {// If file uploaded by Leader then update confirm status only
                confirmMismatchedTransaction(mismatchedSheet);
                uploadMessage = "Upload file Xác nhận điều chỉnh thành công!";
            }
        } else if (ticketSheet != null) { // process ticket sheet if exists
            updateTicket(ticketSheet);
            uploadMessage = "Upload file Cập nhật tên Ticket thành công!";
        } else if (updateIncorrectFeeTransactionSheet != null) {
            updateIncorrectFeeTransaction(updateIncorrectFeeTransactionSheet);
            uploadMessage = "Upload file Điều chỉnh giao dịch sai lệch phí thành công!";
        } else {
            uploadMessage = "Không tìm thấy Sheet phù hợp!";
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, uploadMessage);
            return;
        }

        ClientMessage.log(ClientMessage.MESSAGE_TYPE.INF, uploadMessage);
    }

    public void updateTicket(Sheet ticketSheet) throws Exception {
        int rowNum = ticketSheet.getLastRowNum();

        for (int i = 6; i <= rowNum; i++) {
            Row row = ticketSheet.getRow(i);

            if (row == null) {
                break;
            }

            Cell sttCell = row.getCell(0);
            Cell ticketNameCell = row.getCell(1);
            Cell NVCodeCell = row.getCell(2);
            Cell invoiceOrderIDCell = row.getCell(3);


            if (ticketNameCell.toString().equals("")) {
                // If row is blank then stop
                break;
            } else if (sttCell.toString().equals("") && NVCodeCell.toString().equals("")) {
                // If row is not modified then skip
                continue;
            } else {
                invoiceOrderIDCell.setCellType(Cell.CELL_TYPE_STRING);
                ticketNameCell.setCellType(Cell.CELL_TYPE_STRING);

                String ticketName = ticketNameCell.toString();
                String invoiceOrderID = invoiceOrderIDCell.toString();

                MismatchedTransaction mismatchedTransaction =
                        uploadAdjustedFileModel.getMismatchedTransactionByID(invoiceOrderID);
                mismatchedTransaction.setTicketName(ticketName);
                uploadAdjustedFileModel.updateTicketName(mismatchedTransaction);
            }
        }
    }

    public void processMismatchedTransaction(Sheet mismatchedSheet) throws Exception {
        int rowNum = mismatchedSheet.getPhysicalNumberOfRows();
        // Process transactions
        for (int i = 6; i < rowNum; i++) { // Skip first row
            Row row = mismatchedSheet.getRow(i);
            Cell invoiceOrderIDCell = row.getCell(0);

            if (invoiceOrderIDCell == null) {
                // If row is blank then stop
                break;
            } else {
                Cell processStatusCell = row.getCell(51);
                processStatusCell.setCellType(Cell.CELL_TYPE_STRING);
                invoiceOrderIDCell.setCellType(Cell.CELL_TYPE_STRING);
                String processStatus = processStatusCell.getStringCellValue();

                // 1: Bo sung, 2: Huy bo
                if (processStatus.equals("1") || processStatus.equals("2") || processStatus.equals("4")) {

                    String invoiceOrderID = invoiceOrderIDCell.getStringCellValue();

                    MismatchedTransaction mismatchedTransaction =
                            uploadAdjustedFileModel.getMismatchedTransactionByID(invoiceOrderID);

                    // Process only if current confirm status is waiting for process
                    if (mismatchedTransaction.getConfirmStatus().equals("0") ||
                            mismatchedTransaction.getConfirmStatus().equals("2")) {

                        mismatchedTransaction.setProcessStatus(processStatus);
                        mismatchedTransaction.setUserProcess(AdminUser.getUserLogged().getUserName());
                        mismatchedTransaction.setInvoiceOrderID(invoiceOrderID);
                        mismatchedTransaction.setConfirmStatus("1");

                        // Update transaction data
                        mismatchedTransactionModel.processTransaction(mismatchedTransaction);
                    }
                }
            }
        }
    }

    public void processMatchedTransaction(Sheet matchedSheet) throws Exception {
        int rowNum = matchedSheet.getPhysicalNumberOfRows();

        // Process transactions
        for (int i = 6; i < rowNum; i++) { // Skip first row
            Row row = matchedSheet.getRow(i);
            Cell invoiceOrderIDCell = row.getCell(0);

            if (invoiceOrderIDCell == null) {
                // If row is blank then stop
                break;
            } else {
                Cell processStatusCell = row.getCell(51);
                processStatusCell.setCellType(Cell.CELL_TYPE_STRING);
                String processStatus = processStatusCell.getStringCellValue();

                // 3: Giam tru, Others: skip
                if (processStatus.equals("3")) {
                    String invoiceOrderID = invoiceOrderIDCell.getStringCellValue();
                    MatchedTransaction matchedTransaction =
                            uploadAdjustedFileModel.getMatchedTransactionByID(invoiceOrderID);

                    // 0: Waiting for process, 2: Waiting for re processing
                    if (matchedTransaction.getConfirmStatus().equals("0") ||
                            matchedTransaction.getConfirmStatus().equals("2")) {

                        matchedTransaction.setProcessStatus(processStatus);
                        matchedTransaction.setUserProcess(AdminUser.getUserLogged().getUserName());
                        matchedTransaction.setInvoiceOrderID(invoiceOrderID);
                        matchedTransaction.setConfirmStatus("1");

                        // Update transaction data
                        matchedTransactionModel.processTransaction(matchedTransaction);
                    }
                }
            }
        }
    }

    public void confirmMismatchedTransaction(Sheet mismatchedSheet) throws Exception {
        int rowNum = mismatchedSheet.getPhysicalNumberOfRows();

        // Confirm proceed transactions
        for (int i = 6; i < rowNum; i++) {
            Row row = mismatchedSheet.getRow(i);

            Cell invoiceOrderIDCell = row.getCell(0);

            if (invoiceOrderIDCell == null) {
                // If row is blank then stop
                break;
            } else {
                Cell confirmStatusCell = row.getCell(53);
                confirmStatusCell.setCellType(Cell.CELL_TYPE_STRING);
                invoiceOrderIDCell.setCellType(Cell.CELL_TYPE_STRING);
                String confirmStatus = confirmStatusCell.getStringCellValue();

                // 3: OK, 2: Not OK, Others: Skip
                if (confirmStatus.equals("3") || confirmStatus.equals("2")) {
                    MismatchedTransaction mismatchedTransaction =
                            uploadAdjustedFileModel.getMismatchedTransactionByID(invoiceOrderIDCell.getStringCellValue());

                    // only update is current confirm status is waiting
                    if (mismatchedTransaction.getConfirmStatus().equals("1")) {
                        mismatchedTransaction.setConfirmStatus(confirmStatus);
                        mismatchedTransaction.setUserConfirm(AdminUser.getUserLogged().getUserName());

                        // Update transaction data
                        mismatchedTransactionModel.confirmProcess(mismatchedTransaction);

                        // If process action is "Giam tru" then insert transaction into mismatched transaction table
                        if (mismatchedTransaction.getProcessStatus().equals("1")
                                && mismatchedTransaction.getConfirmStatus().equals("3")) {
                            mismatchedTransactionModel.handleAdd(mismatchedTransaction);
                        }
                    }
                }
            }
        }
    }

    public void confirmMatchedTransaction(Sheet matchedSheet) throws Exception {
        int rowNum = matchedSheet.getPhysicalNumberOfRows();
        // Confirm proceed transactions
        for (int i = 6; i < rowNum; i++) {
            Row row = matchedSheet.getRow(i);

            Cell invoiceOrderIDCell = row.getCell(0);

            if (invoiceOrderIDCell == null) {
                // If row is blank then stop
                break;
            } else {
                Cell confirmStatusCell = row.getCell(53);
                confirmStatusCell.setCellType(Cell.CELL_TYPE_STRING);
                invoiceOrderIDCell.setCellType(Cell.CELL_TYPE_STRING);
                String confirmStatus = confirmStatusCell.getStringCellValue();

                // 3: OK, 2: Not OK, Others: Skip
                if (confirmStatus.equals("3") || confirmStatus.equals("2")) {
                    MatchedTransaction matchedTransaction =
                            uploadAdjustedFileModel.getMatchedTransactionByID(row.getCell(0).toString());

                    // only update if current confirm status is waiting
                    if (matchedTransaction.getConfirmStatus().equals("1")) {
                        matchedTransaction.setConfirmStatus(confirmStatus);
                        matchedTransaction.setUserConfirm(AdminUser.getUserLogged().getUserName());

                        // Update transaction data
                        matchedTransactionModel.confirmProcess(matchedTransaction);

                        // If process action is "Giam tru" then insert transaction into mismatched transaction table
                        if (matchedTransaction.getProcessStatus().equals("3") && matchedTransaction.getConfirmStatus().equals("3")) {
                            matchedTransactionModel.handleDeduction(matchedTransaction);
                        }
                    }
                }

            }
        }
    }

    public void updateIncorrectFeeTransaction(Sheet updateIncorrectFeeTransactionSheet) throws Exception {
        // Process transactions
        for (int rowNum = 6; rowNum < updateIncorrectFeeTransactionSheet.getPhysicalNumberOfRows() - 1; rowNum++) {
            Row selectedRow = updateIncorrectFeeTransactionSheet.getRow(rowNum);

            Cell invoiceOrderIDCell = selectedRow.getCell(0);

            if (invoiceOrderIDCell == null) {
                // If row is blank then stop
                break;
            } else {

                invoiceOrderIDCell.setCellType(Cell.CELL_TYPE_STRING);
                IncorrectFeeTransaction incorrectFeeTransaction =
                        uploadAdjustedFileModel.getIncorrectFeeTransaction(invoiceOrderIDCell.getStringCellValue());

                Cell adjustDateCell = selectedRow.getCell(46);
                Cell refundStatusCell = selectedRow.getCell(47);
                Cell refundAmountCell = selectedRow.getCell(48);
                Cell confirmStatusCell = selectedRow.getCell(50);
                Cell ticketNameCell = selectedRow.getCell(52);

                refundStatusCell.setCellType(Cell.CELL_TYPE_STRING);
                confirmStatusCell.setCellType(Cell.CELL_TYPE_STRING);

                if (ticketNameCell != null) {
                    ticketNameCell.setCellType(Cell.CELL_TYPE_STRING);
                    incorrectFeeTransaction.setTicketName(ticketNameCell.getStringCellValue());
                } else {
                    incorrectFeeTransaction.setTicketName("");
                }

                if (adjustDateCell != null) {
                    incorrectFeeTransaction.setAdjustDate(adjustDateCell.getDateCellValue());
                } else {
                    incorrectFeeTransaction.setAdjustDate(null);
                }

                if (refundAmountCell != null) {
                    refundAmountCell.setCellType(Cell.CELL_TYPE_STRING);
                    incorrectFeeTransaction.setRefundAmount(
                            Integer.parseInt(refundAmountCell.getStringCellValue().replace(".0", "")));
                }

                if (AdminUser.getUserLogged().getTitle() == 0) {
                    String currentStatus = incorrectFeeTransaction.getRefundStatus();
                    String newStatus = refundStatusCell.getStringCellValue();
                    if (!currentStatus.equals(newStatus)) {
                        incorrectFeeTransaction.setRefundStatus(newStatus);
                        incorrectFeeTransaction.setUserProcess(AdminUser.getUserLogged().getUserName());
                        incorrectFeeTransaction.setConfirmStatus("1");
                    }
                } else if (AdminUser.getUserLogged().getTitle() == 1) {
                    String currentStatus = incorrectFeeTransaction.getConfirmStatus();
                    String newStatus = confirmStatusCell.getStringCellValue();
                    if (!currentStatus.equals(newStatus)) {
                        incorrectFeeTransaction.setConfirmStatus(newStatus);
                        incorrectFeeTransaction.setUserConfirm(AdminUser.getUserLogged().getUserName());
                    }
                }

                uploadAdjustedFileModel.updateIncorrectFeeTransaction(incorrectFeeTransaction);
            }
        }
    }
}
