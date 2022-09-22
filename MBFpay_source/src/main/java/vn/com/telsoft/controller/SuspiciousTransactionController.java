package vn.com.telsoft.controller;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.axis.utils.Admin;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SuspiciousTransaction;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SuspiciousTransactionModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class SuspiciousTransactionController extends TSFuncTemplate implements Serializable {

    private List<SuspiciousTransaction> mlistApp;
    private SuspiciousTransaction mtmpApp;
    private List<SuspiciousTransaction> mselectedApp;
    private SuspiciousTransactionModel mmodel;
    private List<ProfilePartner> bankList;
    private ProfilePartnerModel profilePartnerModel;
    private String searchInput;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "suspicious_transaction_template.xlsx";
    private final String strPrefexFileName = "GDNV_";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private StreamedContent file;
    private String userTitle;

    private Date fromDate;
    private Date toDate;
    private Date inputDate;

    private List<String> selectedBankCodes = new ArrayList<>();

    public SuspiciousTransactionController() throws Exception {
        mmodel = new SuspiciousTransactionModel();
        profilePartnerModel = new ProfilePartnerModel();
        bankList = profilePartnerModel.getListBank();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        fromDate = calendar.getTime();
        toDate = new Date();
        searchInput = "";
        userTitle = AdminUser.getUserLogged().getTitle() + "";
        inputDate = new Date();
    }

    @Override
    public void handleOK() throws Exception {

    }

    @Override
    public void handleDelete() throws Exception {

    }

    public void handleSearch() throws Exception {
        try {
            this.mlistApp = mmodel.getSuspiciousTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.searchInput);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(SuspiciousTransaction suspiciousTransaction) throws Exception {
        selectedIndex = this.mlistApp.indexOf(suspiciousTransaction);
        this.mtmpApp = (SuspiciousTransaction) SerializationUtils.clone(suspiciousTransaction);
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

    public void processTransaction(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            this.mtmpApp.setProcessStatus(input);
            this.mtmpApp.setConfirmStatus("1");
            this.mtmpApp.setUserProcess(AdminUser.getUserLogged().getUserName());
            this.mmodel.processTransaction(this.mtmpApp); // update transaction in database

            this.mlistApp = mmodel.getSuspiciousTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.searchInput);

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
            this.mlistApp = mmodel.getSuspiciousTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.searchInput);

            //Message to client
            ClientMessage.log(ClientMessage.MESSAGE_TYPE.UPDATE, "Xác nhận điều chỉnh thành công!");
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void onCellEdit(CellEditEvent event) {
        String headerText = event.getColumn().getHeaderText();
        try {
            switch (headerText){
                case "TicketName":
                    mmodel.updateTicketName(event.getRowKey(), event.getNewValue().toString());
                    break;
                case "AdjustDate":
                    mmodel.updateAdjustDate(event.getRowKey(), inputDate);
                    break;
            }

            this.mlistApp = mmodel.getSuspiciousTransactions(this.selectedBankCodes,
                    this.fromDate, this.toDate, this.searchInput);
        } catch (Exception exception) {
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, exception.toString());
        }
    }
    ////////////////////////////////////////////////////////
    // GETTER & SETTER

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
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

    public List<ProfilePartner> getBankList() {
        return bankList;
    }

    public void setBankList(List<ProfilePartner> bankList) {
        this.bankList = bankList;
    }

    public ProfilePartnerModel getProfilePartnerModel() {
        return profilePartnerModel;
    }

    public void setProfilePartnerModel(ProfilePartnerModel profilePartnerModel) {
        this.profilePartnerModel = profilePartnerModel;
    }

    public List<SuspiciousTransaction> getMlistApp() {
        return mlistApp;
    }

    public void setMlistApp(List<SuspiciousTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public SuspiciousTransaction getMtmpApp() {
        return mtmpApp;
    }

    public void setMtmpApp(SuspiciousTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SuspiciousTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<SuspiciousTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public SuspiciousTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(SuspiciousTransactionModel mmodel) {
        this.mmodel = mmodel;
    }

    public List<String> getSelectedBankCodes() {
        return selectedBankCodes;
    }

    public void setSelectedBankCodes(List<String> selectedBankCodes) {
        this.selectedBankCodes = selectedBankCodes;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }
}
