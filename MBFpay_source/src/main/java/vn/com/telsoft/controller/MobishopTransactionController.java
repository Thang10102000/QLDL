/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.MobishopTransaction;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.MobishopTransactionModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author ThongNM
 */
@Named
@ViewScoped
public class MobishopTransactionController extends TSFuncTemplate implements Serializable {

    private List<MobishopTransaction> mlistApp;
    private MobishopTransaction mtmpApp;
    private List<MobishopTransaction> mselectedApp;
    private MobishopTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "mobishop_transaction_template.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "MOBISHOP_";
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> partnerCodeList;
    private List<ProfilePartner> bankList;
    private List<String> productCodeList;
    private List<String> productServiceCodeList;
    private List<String> payTypeNameList;

    private HashMap<String, String> sumResults;
    private String mPartnerCode;
    private String mBankCode;
    private Date createdTimeFrom;
    private Date createdTimeTo;
    private Date endTimeFrom;
    private Date endTimeTo;
    private String searchInput;
    private List<String> selectedPaymentAcceptors = new ArrayList<>();
    private List<String> selectedBankCodes = new ArrayList<>();
    private List<String> selectedOrderStatusCodes = new ArrayList<>();
    private List<String> selectedPayTypeNames = new ArrayList<>();
    private List<String> selectedProductServiceCodes = new ArrayList<>();
    private List<String> selectedProductCodes = new ArrayList<>();

    public MobishopTransactionController() throws Exception {
        mmodel = new MobishopTransactionModel();
        mlistApp = new ArrayList<>();
        mprofilePartnerModel = new ProfilePartnerModel();
        partnerCodeList = mprofilePartnerModel.getListPartnerCode();
        productCodeList = mmodel.getDistinctProductCode();
        productServiceCodeList = mmodel.getDistinctProductServiceCode();
        payTypeNameList = mmodel.getDistinctPayTypeName();

        bankList = mprofilePartnerModel.getListBank();
        createdTimeTo = new Date();
//        adjustedToDate = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        createdTimeFrom = calendar.getTime();
//        adjustedFromDate = calendar.getTime();

        searchInput = "";
        sumResults = new HashMap<>();
    }
    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new MobishopTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(MobishopTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (MobishopTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(MobishopTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (MobishopTransaction) SerializationUtils.clone(app);
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
            mtmpApp = new MobishopTransaction();

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

    public void handleDelete(MobishopTransaction ett) throws Exception {
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
            mlistApp = mmodel.getMobishopTransactions(this.selectedPaymentAcceptors, this.selectedBankCodes,
                    this.createdTimeFrom, this.createdTimeTo, this.searchInput, this.selectedOrderStatusCodes,
                    this.selectedPayTypeNames, this.selectedProductServiceCodes, this.selectedProductCodes,
                    this.endTimeFrom, this.endTimeTo, this.sumResults);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(MobishopTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (MobishopTransaction) SerializationUtils.clone(app);
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<MobishopTransaction> getMlistApp() {
        return mlistApp;
    }

    public MobishopTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(MobishopTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<MobishopTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<MobishopTransaction> mselectedApp) {
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

    public Set<String> getDistinctOrderStatusCode(final List<MobishopTransaction> transactionList){
        Set<String> distinctStatusCode = new HashSet<>();
        for(final MobishopTransaction Mobishop: transactionList){
            distinctStatusCode.add(Mobishop.getOrderStatusCode());
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

    public void setMlistApp(List<MobishopTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public MobishopTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(MobishopTransactionModel mmodel) {
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

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public List<String> getSelectedPayTypeNames() {
        return selectedPayTypeNames;
    }

    public void setSelectedPayTypeNames(List<String> selectedPayTypeNames) {
        this.selectedPayTypeNames = selectedPayTypeNames;
    }

    public List<String> getSelectedProductServiceCodes() {
        return selectedProductServiceCodes;
    }

    public void setSelectedProductServiceCodes(List<String> selectedProductServiceCodes) {
        this.selectedProductServiceCodes = selectedProductServiceCodes;
    }

    public List<String> getSelectedProductCodes() {
        return selectedProductCodes;
    }

    public void setSelectedProductCodes(List<String> selectedProductCodes) {
        this.selectedProductCodes = selectedProductCodes;
    }

    public List<String> getProductCodeList() {
        return productCodeList;
    }

    public void setProductCodeList(List<String> productCodeList) {
        this.productCodeList = productCodeList;
    }

    public List<String> getPayTypeNameList() {
        return payTypeNameList;
    }

    public void setPayTypeNameList(List<String> payTypeNameList) {
        this.payTypeNameList = payTypeNameList;
    }

    public List<String> getProductServiceCodeList() {
        return productServiceCodeList;
    }

    public void setProductServiceCodeList(List<String> productServiceCodeList) {
        this.productServiceCodeList = productServiceCodeList;
    }
}
