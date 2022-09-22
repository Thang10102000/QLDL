package vn.com.telsoft.controller;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.MismatchedTransaction;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.IncorrectFeeTransactionModel;
import vn.com.telsoft.model.MismatchedTransactionModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

@Named
@ViewScoped
public class IncorrectFeeTransactionController extends TSFuncTemplate implements Serializable {
    private List<IncorrectFeeTransaction> mlistApp;
    private IncorrectFeeTransaction mtmpApp;
    private List<IncorrectFeeTransaction> mselectedApp;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "incorrect_fee_transaction_template.xlsx";
    private final String strPrefexFileName = "GDSLP_";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private StreamedContent file;
    private IncorrectFeeTransactionModel mmodel;
    private List<ProfilePartner> bankList;
    private ProfilePartnerModel mprofilePartnerModel;
    private Date fromDate;
    private Date toDate;
    private Date fromAdjustDate;
    private Date toAdjustDate;
    private Date inputDate;
    private String invoiceOrderID;
    private String userTitle;

    private String ticketName;
    private int refundAmount;
    private String refundStatus;

    private List<String> selectedBankCodes = new ArrayList<>();
    private List<String> selectedOrderStatusCodes = new ArrayList<>();
    private List<String> selectedProcessStatus = new ArrayList<>();
    private List<String> selectedConfirmStatus = new ArrayList<>();
    private List<String> selectedTicketStatus = new ArrayList<>();

    public IncorrectFeeTransactionController() throws Exception {
        mmodel = new IncorrectFeeTransactionModel();
        mprofilePartnerModel = new ProfilePartnerModel();
        bankList = mprofilePartnerModel.getListBank();
        ticketName = "";

        toDate = new Date();
        inputDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fromDate = calendar.getTime();
        userTitle = AdminUser.getUserLogged().getTitle() + "";
    }

    ///////////////////////////////////////////////////////////////////////
    public void handleSearch() throws Exception {
        try {
            mlistApp = mmodel.getIncorrectFeeTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.invoiceOrderID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.fromAdjustDate, this.toAdjustDate);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(IncorrectFeeTransaction incorrectFeeTransaction) throws Exception {
        selectedIndex = this.mlistApp.indexOf(incorrectFeeTransaction);
        this.mtmpApp = (IncorrectFeeTransaction) SerializationUtils.clone(incorrectFeeTransaction);
    }

    public void onCellEdit(CellEditEvent event) {
        String headerText = event.getColumn().getHeaderText();
        try {
            switch (headerText){
                case "TicketName":
                    mmodel.updateTicketName(event.getRowKey(), event.getNewValue().toString());
                    break;
                case "Refund/Arrears Amount":
                    mmodel.updateRefundAmount(event.getRowKey(), event.getNewValue().toString());
                    break;
                case "AdjustDate":
                    mmodel.updateAdjustDate(event.getRowKey(), inputDate);
                    break;
            }

            this.mlistApp = mmodel.getIncorrectFeeTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.invoiceOrderID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.fromAdjustDate, this.toAdjustDate);
        } catch (Exception exception) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, exception.toString());
        }
    }

    public void updateRefundStatus(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            this.mtmpApp.setRefundStatus(input);
            this.mtmpApp.setConfirmStatus("1");
            this.mtmpApp.setUserProcess(AdminUser.getUserLogged().getUserName());
            this.mmodel.updateRefundStatus(this.mtmpApp); // update transaction in database

            this.mlistApp = mmodel.getIncorrectFeeTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.invoiceOrderID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.fromAdjustDate, this.toAdjustDate);

            //Message to client
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Điều chỉnh giao dịch thành công!");
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void confirmProcess(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            if (input.equals("1")) {
                this.mtmpApp.setConfirmStatus("3");
            } else if (input.equals("2")) {
                this.mtmpApp.setConfirmStatus("2");
            }

            this.mtmpApp.setUserConfirm(AdminUser.getUserLogged().getUserName());
            this.mmodel.confirmProcess(this.mtmpApp); // update transaction in database
            this.mlistApp = mmodel.getIncorrectFeeTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.invoiceOrderID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.fromAdjustDate, this.toAdjustDate);

            //Message to client
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Xác nhận điều chỉnh thành công!");
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = "";

        if (fromDate != null & toDate != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(fromDate, "MM-dd") + "_to_" +
                            DateFormatUtils.format(toDate, "MM-dd") + ".xls";
        } else {
            fileName = strPrefexFileName + ".xls";
        }

        String destFileName = strFoderExport + File.separator + fileName;
        FileUtil.forceFolderExist(strFoderExport);
        Map beans = new HashMap();
        beans.put("app", mlistApp);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, destFileName);
        file = new DefaultStreamedContent(new FileInputStream(destFileName), "application/xls",
                fileName);
        return file;
    }

    public String getTotalAmount() {
        int total = 0;

        for (IncorrectFeeTransaction incorrectFeeTransaction : mlistApp) {
            total += incorrectFeeTransaction.getAmount() * incorrectFeeTransaction.getQuantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getTotalQuantity() {
        int total = 0;

        for (IncorrectFeeTransaction incorrectFeeTransaction : mlistApp) {
            total += incorrectFeeTransaction.getQuantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getTotalPaymentAmount() {
        int total = 0;

        for (IncorrectFeeTransaction incorrectFeeTransaction : mlistApp) {
            total += incorrectFeeTransaction.getPaymentAmount();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getTotalGrandAmount() {
        int total = 0;

        for (IncorrectFeeTransaction incorrectFeeTransaction : mlistApp) {
            total += incorrectFeeTransaction.getGrandAmount();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    @Override
    public void handleOK() throws Exception {

    }

    @Override
    public void handleDelete() throws Exception {

    }

    public List<ProfilePartner> getBankList() {
        return bankList;
    }

    public void setBankList(List<ProfilePartner> bankList) {
        this.bankList = bankList;
    }

    public ProfilePartnerModel getMprofilePartnerModel() {
        return mprofilePartnerModel;
    }

    public void setMprofilePartnerModel(ProfilePartnerModel mprofilePartnerModel) {
        this.mprofilePartnerModel = mprofilePartnerModel;
    }

    public List<String> getSelectedBankCodes() {
        return selectedBankCodes;
    }

    public void setSelectedBankCodes(List<String> selectedBankCodes) {
        this.selectedBankCodes = selectedBankCodes;
    }

    public List<String> getSelectedOrderStatusCodes() {
        return selectedOrderStatusCodes;
    }

    public void setSelectedOrderStatusCodes(List<String> selectedOrderStatusCodes) {
        this.selectedOrderStatusCodes = selectedOrderStatusCodes;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromAdjustDate() {
        return fromAdjustDate;
    }

    public void setFromAdjustDate(Date fromAdjustDate) {
        this.fromAdjustDate = fromAdjustDate;
    }

    public Date getToAdjustDate() {
        return toAdjustDate;
    }

    public void setToAdjustDate(Date toAdjustDate) {
        this.toAdjustDate = toAdjustDate;
    }

    public List<String> getSelectedProcessStatus() {
        return selectedProcessStatus;
    }

    public void setSelectedProcessStatus(List<String> selectedProcessStatus) {
        this.selectedProcessStatus = selectedProcessStatus;
    }

    public String getInvoiceOrderID() {
        return invoiceOrderID;
    }

    public void setInvoiceOrderID(String invoiceOrderID) {
        this.invoiceOrderID = invoiceOrderID;
    }

    public List<String> getSelectedConfirmStatus() {
        return selectedConfirmStatus;
    }

    public void setSelectedConfirmStatus(List<String> selectedConfirmStatus) {
        this.selectedConfirmStatus = selectedConfirmStatus;
    }

    public List<String> getSelectedTicketStatus() {
        return selectedTicketStatus;
    }

    public void setSelectedTicketStatus(List<String> selectedTicketStatus) {
        this.selectedTicketStatus = selectedTicketStatus;
    }

    public List<IncorrectFeeTransaction> getMlistApp() {
        return mlistApp;
    }

    public void setMlistApp(List<IncorrectFeeTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public IncorrectFeeTransaction getMtmpApp() {
        return mtmpApp;
    }

    public void setMtmpApp(IncorrectFeeTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<IncorrectFeeTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<IncorrectFeeTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public IncorrectFeeTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(IncorrectFeeTransactionModel mmodel) {
        this.mmodel = mmodel;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public int getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }
}
