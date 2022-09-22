/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import vn.com.telsoft.entity.MismatchedTransaction;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.MismatchedTransactionModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author ThongNM
 */
@Named
@ViewScoped
public class MismatchedTransactionController extends TSFuncTemplate implements Serializable {

    private List<MismatchedTransaction> mlistApp;
    private MismatchedTransaction mtmpApp;
    private List<MismatchedTransaction> mselectedApp;
    private MismatchedTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "template_mismatched_transaction.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "GDCL_";
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> partnerCodeList;
    private List<ProfilePartner> bankList;
    private HashMap<String, String> sumResults;
    private String mPartnerCode;
    private String mBankCode;
    private Date mdStartDate;
    private Date mdEndDate;
    private Date adjustedFromDate;
    private Date adjustedToDate;
    private Date inputDate;
    private String transactionID;
    private String processStatus;
    private String confirmStatus;
    private String refundStatus;
    private String userTitle;
    private String ticketStatus;
    private List<String> selectedPaymentAcceptors = new ArrayList<>();
    private List<String> selectedBankCodes = new ArrayList<>();
    private List<String> selectedProcessStatus = new ArrayList<>();
    private List<String> selectedConfirmStatus = new ArrayList<>();
    private List<String> selectedRefundStatus = new ArrayList<>();
    private List<String> selectedTicketStatus = new ArrayList<>();
    private List<String> selectedOrderStatusCodes = new ArrayList<>();
    private List<String> selectedFinalStatus = new ArrayList<>();

    public MismatchedTransactionController() throws Exception {
        mmodel = new MismatchedTransactionModel();
        mlistApp = new ArrayList<>();
        mprofilePartnerModel = new ProfilePartnerModel();
        partnerCodeList = mprofilePartnerModel.getListPartnerCode();
        bankList = mprofilePartnerModel.getListBank();
        inputDate = new Date();
        mdEndDate = new Date();
//        adjustedToDate = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        mdStartDate = calendar.getTime();
//        adjustedFromDate = calendar.getTime();

        transactionID = "";
        userTitle = AdminUser.getUserLogged().getTitle() + "";
        ticketStatus = "";
        sumResults = new HashMap<>();
    }
    //////////////////////////////////////////////////////////////////////////////////
    public void onCellEdit(CellEditEvent event) {
        String headerText = event.getColumn().getHeaderText();
        try {
            switch (headerText){
                case "TicketName":
                    mmodel.updateTicketNameCell(event.getRowKey(), event.getNewValue().toString());
                    break;

                case "AdjustDate":
                    mmodel.updateAdjustDateCell(event.getRowKey(), inputDate);
                    break;

            }

            this.mlistApp = mmodel.getMismatchedTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes,
                    this.mdStartDate, this.mdEndDate, this.transactionID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedRefundStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.selectedFinalStatus,
                    this.adjustedFromDate, this.adjustedToDate, this.sumResults);
        } catch (Exception exception) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, exception.toString());
        }
    }

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new MismatchedTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(MismatchedTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (MismatchedTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(MismatchedTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (MismatchedTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleOK() throws Exception {
        if (isADD || isCOPY) {
            //Check permission
            if (!getPermission("I")) {
                return;
            }

//            mmodel.add(mtmpApp);
            mlistApp.add(0, mtmpApp);

            //Reset form
            mtmpApp = new MismatchedTransaction();

            //Message to client
            ClientMessage.logAdd();

        } else if (isEDIT) {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

//            mmodel.edit(mtmpApp);
            mlistApp.set(selectedIndex, mtmpApp);

            //Message to client
            ClientMessage.logUpdate();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleDelete() throws Exception {
        handleDelete(null);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void handleDelete(MismatchedTransaction ett) throws Exception {
        //Check permission
        if (!getPermission("D")) {
            return;
        }

        if (ett == null) {
//            mmodel.delete(mselectedApp);

        } else {
//            mmodel.delete(Collections.singletonList(ett));
        }


//        mlistApp = mmodel.getListApp();
        mselectedApp = null;

        //Message to client
        ClientMessage.logDelete();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void handleSearch() throws Exception {
        try {
            mlistApp = mmodel.getMismatchedTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes,
                    this.mdStartDate, this.mdEndDate, this.transactionID, this.selectedProcessStatus,
                    this.selectedConfirmStatus, this.selectedRefundStatus, this.selectedTicketStatus,
                    this.selectedOrderStatusCodes, this.selectedFinalStatus,
                    this.adjustedFromDate, this.adjustedToDate, this.sumResults);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(MismatchedTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (MismatchedTransaction) SerializationUtils.clone(app);
    }

    public void processTransaction(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            Date dtExecute = new Date();
            this.mtmpApp.setProcessStatus(input);
            this.mtmpApp.setAdjustDate(dtExecute);
            this.mtmpApp.setConfirmStatus("1");
            this.mtmpApp.setUserProcess(AdminUser.getUserLogged().getUserName());
            this.mmodel.processTransaction(this.mtmpApp); // update transaction in database
            this.mlistApp = this.mmodel.getMismatchedTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes, this.mdStartDate,
                    this.mdEndDate, this.transactionID, this.selectedProcessStatus, this.selectedConfirmStatus, this.selectedRefundStatus,
                    this.selectedTicketStatus, this.selectedOrderStatusCodes, this.selectedFinalStatus,
                    this.adjustedFromDate, this.adjustedToDate,
                    this.sumResults);

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

            Date dtExecute = new Date();

            if (input.equals("1")) {
                this.mtmpApp.setConfirmStatus("3");
            } else if (input.equals("2")) {
                this.mtmpApp.setConfirmStatus("2");
            }

            this.mtmpApp.setUserConfirm(AdminUser.getUserLogged().getUserName());
            this.mtmpApp.setAdjustDate(new Date());
            this.mmodel.confirmProcess(this.mtmpApp); // update transaction in database
            this.mlistApp = this.mmodel.getMismatchedTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes, this.mdStartDate,
                    this.mdEndDate, this.transactionID, this.selectedProcessStatus, this.selectedConfirmStatus, this.selectedRefundStatus,
                    this.selectedTicketStatus, this.selectedOrderStatusCodes, this.selectedFinalStatus,
                    this.adjustedFromDate, this.adjustedToDate,
                    this.sumResults);

            //Bo sung
            if (input.equals("1") && this.mtmpApp.getProcessStatus().equals("1")) {
                mmodel.handleAdd(mtmpApp);
            }

            //Message to client
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Xác nhận điều chỉnh thành công!");
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void updateRefundStatus(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            Date dtExecute = new Date();
            this.mtmpApp.setRefundStatus(input);
            this.mmodel.updateRefundStatus(this.mtmpApp); // update transaction in database
            this.mlistApp = this.mmodel.getMismatchedTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes, this.mdStartDate,
                    this.mdEndDate, this.transactionID, this.selectedProcessStatus, this.selectedConfirmStatus, this.selectedRefundStatus,
                    this.selectedTicketStatus, this.selectedOrderStatusCodes, this.selectedFinalStatus,
                    this.adjustedFromDate, this.adjustedToDate,
                    this.sumResults);

            //Message to client
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Cập nhật trạng thái hoàn tiền thành công!");
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }


    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<MismatchedTransaction> getMlistApp() {
        return mlistApp;
    }

    public MismatchedTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(MismatchedTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<MismatchedTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<MismatchedTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public String getTotalAmount() {
        return this.sumResults.get("total_amount");
    }

    public String getTotalQuantity() {
        return this.sumResults.get("total_quantity");
    }

    public String getTotalPaymentAmount() {
        return this.sumResults.get("total_payment_amount");
    }

    public String getTotalGrandAmount() {
        return this.sumResults.get("total_grand_amount");
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = "";

        if (mdStartDate != null & mdEndDate != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(mdStartDate, "MM-dd") + "_to_" +
                            DateFormatUtils.format(mdEndDate, "MM-dd") + ".xls";
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

    public Set<String> getDistinctOrderStatusCode(final List<MismatchedTransaction> transactionList){
        Set<String> distinctStatusCode = new HashSet<>();
        for(final MismatchedTransaction mismatchedTransaction: transactionList){
            distinctStatusCode.add(mismatchedTransaction.getOrderStatusCode());
        }
        return distinctStatusCode;
    }

    // =================================== GETTER & SETTER ====================================


    public List<ProfilePartner> getPartnerCodeList() {
        return partnerCodeList;
    }

    public void setPartnerCodeList(List<ProfilePartner> partnerCodeList) {
        this.partnerCodeList = partnerCodeList;
    }

    public List<ProfilePartner> getBankList() {
        return bankList;
    }

    public void setBankList(List<ProfilePartner> bankList) {
        this.bankList = bankList;
    }

    public String getmPartnerCode() {
        return mPartnerCode;
    }

    public void setmPartnerCode(String mPartnerCode) {
        this.mPartnerCode = mPartnerCode;
    }

    public String getmBankCode() {
        return mBankCode;
    }

    public void setmBankCode(String mBankCode) {
        this.mBankCode = mBankCode;
    }

    public Date getMdStartDate() {
        return mdStartDate;
    }

    public void setMdStartDate(Date mdStartDate) {
        this.mdStartDate = mdStartDate;
    }

    public Date getMdEndDate() {
        return mdEndDate;
    }

    public void setMdEndDate(Date mdEndDate) {
        this.mdEndDate = mdEndDate;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public List<String> getSelectedPaymentAcceptors() {
        return selectedPaymentAcceptors;
    }

    public void setSelectedPaymentAcceptors(List<String> selectedPaymentAcceptors) {
        this.selectedPaymentAcceptors = selectedPaymentAcceptors;
    }

    public List<String> getSelectedBankCodes() {
        return selectedBankCodes;
    }

    public void setSelectedBankCodes(List<String> selectedBankCodes) {
        this.selectedBankCodes = selectedBankCodes;
    }

    public List<String> getSelectedProcessStatus() {
        return selectedProcessStatus;
    }

    public void setSelectedProcessStatus(List<String> selectedProcessStatus) {
        this.selectedProcessStatus = selectedProcessStatus;
    }

    public List<String> getSelectedConfirmStatus() {
        return selectedConfirmStatus;
    }

    public void setSelectedConfirmStatus(List<String> selectedConfirmStatus) {
        this.selectedConfirmStatus = selectedConfirmStatus;
    }

    public List<String> getSelectedRefundStatus() {
        return selectedRefundStatus;
    }

    public void setSelectedRefundStatus(List<String> selectedRefundStatus) {
        this.selectedRefundStatus = selectedRefundStatus;
    }

    public List<String> getSelectedTicketStatus() {
        return selectedTicketStatus;
    }

    public void setSelectedTicketStatus(List<String> selectedTicketStatus) {
        this.selectedTicketStatus = selectedTicketStatus;
    }

    public List<String> getSelectedOrderStatusCodes() {
        return selectedOrderStatusCodes;
    }

    public void setSelectedOrderStatusCodes(List<String> selectedOrderStatusCodes) {
        this.selectedOrderStatusCodes = selectedOrderStatusCodes;
    }

    public Date getAdjustedFromDate() {
        return adjustedFromDate;
    }

    public void setAdjustedFromDate(Date adjustedFromDate) {
        this.adjustedFromDate = adjustedFromDate;
    }

    public Date getAdjustedToDate() {
        return adjustedToDate;
    }

    public void setAdjustedToDate(Date adjustedToDate) {
        this.adjustedToDate = adjustedToDate;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public List<String> getSelectedFinalStatus() {
        return selectedFinalStatus;
    }

    public void setSelectedFinalStatus(List<String> selectedFinalStatus) {
        this.selectedFinalStatus = selectedFinalStatus;
    }
}
