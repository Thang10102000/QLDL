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
import vn.com.telsoft.entity.AdjustmentTransaction;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.AdjustmentTransactionModel;
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
public class AdjustmentTransactionController extends TSFuncTemplate implements Serializable {

    private List<AdjustmentTransaction> mlistApp;
    private AdjustmentTransaction mtmpApp;
    private List<AdjustmentTransaction> mselectedApp;
    private AdjustmentTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "template_adjustment_transaction.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "GDDC_";
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> partnerCodeList;
    private List<ProfilePartner> bankList;
    private HashMap<String, String> sumResults;
    private String mPartnerCode;
    private String mBankCode;
    private Date createdTimeFrom;
    private Date createdTimeTo;
    private Date endTimeFrom;
    private Date endTimeTo;
    private String invoiceOrderID;
    private List<String> selectedPaymentAcceptors = new ArrayList<>();
    private List<String> selectedBankCodes = new ArrayList<>();
    private List<String> selectedOrderStatusCodes = new ArrayList<>();

    public AdjustmentTransactionController() throws Exception {
        mmodel = new AdjustmentTransactionModel();
        mlistApp = new ArrayList<>();
        mprofilePartnerModel = new ProfilePartnerModel();
        partnerCodeList = mprofilePartnerModel.getListPartnerCode();
        bankList = mprofilePartnerModel.getListBank();
        createdTimeTo = new Date();
//        adjustedToDate = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        createdTimeFrom = calendar.getTime();
//        adjustedFromDate = calendar.getTime();

        invoiceOrderID = "";
        sumResults = new HashMap<>();
    }
    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new AdjustmentTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(AdjustmentTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (AdjustmentTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(AdjustmentTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (AdjustmentTransaction) SerializationUtils.clone(app);
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
            mtmpApp = new AdjustmentTransaction();

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

    public void handleDelete(AdjustmentTransaction ett) throws Exception {
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
            mlistApp = mmodel.getAdjustmentTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes,
                    this.createdTimeFrom, this.createdTimeTo, this.invoiceOrderID,
                    this.selectedOrderStatusCodes, this.endTimeFrom, this.endTimeTo, this.sumResults);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(AdjustmentTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (AdjustmentTransaction) SerializationUtils.clone(app);
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<AdjustmentTransaction> getMlistApp() {
        return mlistApp;
    }

    public AdjustmentTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(AdjustmentTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<AdjustmentTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<AdjustmentTransaction> mselectedApp) {
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

        if (createdTimeFrom != null & createdTimeTo != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(createdTimeFrom, "MM-dd") + "_to_" +
                            DateFormatUtils.format(createdTimeTo, "MM-dd") + ".xls";
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

    public Set<String> getDistinctOrderStatusCode(final List<AdjustmentTransaction> transactionList){
        Set<String> distinctStatusCode = new HashSet<>();
        for(final AdjustmentTransaction AdjustmentTransaction: transactionList){
            distinctStatusCode.add(AdjustmentTransaction.getOrderStatusCode());
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

    public List<String> getSelectedOrderStatusCodes() {
        return selectedOrderStatusCodes;
    }

    public void setSelectedOrderStatusCodes(List<String> selectedOrderStatusCodes) {
        this.selectedOrderStatusCodes = selectedOrderStatusCodes;
    }

    public void setMlistApp(List<AdjustmentTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public AdjustmentTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(AdjustmentTransactionModel mmodel) {
        this.mmodel = mmodel;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public String getStrRealPath() {
        return strRealPath;
    }

    public String getStrFileTemplate() {
        return strFileTemplate;
    }

    public String getStrFoderExport() {
        return strFoderExport;
    }

    public String getStrPrefexFileName() {
        return strPrefexFileName;
    }

    public ProfilePartnerModel getMprofilePartnerModel() {
        return mprofilePartnerModel;
    }

    public void setMprofilePartnerModel(ProfilePartnerModel mprofilePartnerModel) {
        this.mprofilePartnerModel = mprofilePartnerModel;
    }

    public HashMap<String, String> getSumResults() {
        return sumResults;
    }

    public void setSumResults(HashMap<String, String> sumResults) {
        this.sumResults = sumResults;
    }

    public Date getCreatedTimeFrom() {
        return createdTimeFrom;
    }

    public void setCreatedTimeFrom(Date createdTimeFrom) {
        this.createdTimeFrom = createdTimeFrom;
    }

    public Date getCreatedTimeTo() {
        return createdTimeTo;
    }

    public void setCreatedTimeTo(Date createdTimeTo) {
        this.createdTimeTo = createdTimeTo;
    }

    public Date getEndTimeFrom() {
        return endTimeFrom;
    }

    public void setEndTimeFrom(Date endTimeFrom) {
        this.endTimeFrom = endTimeFrom;
    }

    public Date getEndTimeTo() {
        return endTimeTo;
    }

    public void setEndTimeTo(Date endTimeTo) {
        this.endTimeTo = endTimeTo;
    }

    public String getInvoiceOrderID() {
        return invoiceOrderID;
    }

    public void setInvoiceOrderID(String invoiceOrderID) {
        this.invoiceOrderID = invoiceOrderID;
    }
}
