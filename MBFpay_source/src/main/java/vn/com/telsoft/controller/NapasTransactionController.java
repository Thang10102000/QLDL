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
import vn.com.telsoft.entity.NapasTransaction;
import vn.com.telsoft.model.NapasTransactionModel;
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
public class NapasTransactionController extends TSFuncTemplate implements Serializable {

    private List<NapasTransaction> mlistApp;
    private NapasTransaction mtmpApp;
    private List<NapasTransaction> mselectedApp;
    private NapasTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "napas_transaction_template.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "NAPAS_";
    private Date transactionDateFrom;
    private Date transactionDateTo;
    private String searchInput;
    private List<String> merchantCodeList;
    private List<String> acqBankCodeList;
    private List<String> issuerBankCodeList;


    private List<String> selectedIssuerBankCodes = new ArrayList<>();
    private List<String> selectedACQBankCodes = new ArrayList<>();
    private List<String> selectedMerchantCodes = new ArrayList<>();

    public NapasTransactionController() throws Exception {
        mmodel = new NapasTransactionModel();
        mlistApp = new ArrayList<>();
        transactionDateTo = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        transactionDateFrom = calendar.getTime();

        searchInput = "";

        merchantCodeList = mmodel.getDistinctMerchantCode();
        acqBankCodeList = mmodel.getDistinctACQBankCode();
        issuerBankCodeList = mmodel.getDistinctIssuerBankCode();
    }
    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new NapasTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(NapasTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (NapasTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(NapasTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (NapasTransaction) SerializationUtils.clone(app);
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
            mtmpApp = new NapasTransaction();

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

    public void handleDelete(NapasTransaction ett) throws Exception {
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
            mlistApp = mmodel.getNapasTransactions(this.selectedMerchantCodes, this.selectedACQBankCodes,
                    this.selectedIssuerBankCodes, this.transactionDateFrom,
                    this.transactionDateTo, this.searchInput);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(NapasTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (NapasTransaction) SerializationUtils.clone(app);
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<NapasTransaction> getMlistApp() {
        return mlistApp;
    }

    public NapasTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(NapasTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<NapasTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<NapasTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = "";

        if (transactionDateFrom != null & transactionDateTo != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(transactionDateFrom, "MM-dd") + "_to_" +
                            DateFormatUtils.format(transactionDateTo, "MM-dd") + ".xls";
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

//    public Set<String> getDistinctOrderStatusCode(final List<Napas> transactionList){
//        Set<String> distinctStatusCode = new HashSet<>();
//        for(final Napas Napas: transactionList){
//            distinctStatusCode.add(Napas.getOrderStatusCode());
//        }
//        return distinctStatusCode;
//    }

    // =================================== GETTER & SETTER ====================================

    public void setMlistApp(List<NapasTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public NapasTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(NapasTransactionModel mmodel) {
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

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public Date getTransactionDateFrom() {
        return transactionDateFrom;
    }

    public void setTransactionDateFrom(Date transactionDateFrom) {
        this.transactionDateFrom = transactionDateFrom;
    }

    public Date getTransactionDateTo() {
        return transactionDateTo;
    }

    public void setTransactionDateTo(Date transactionDateTo) {
        this.transactionDateTo = transactionDateTo;
    }

    public List<String> getSelectedIssuerBankCodes() {
        return selectedIssuerBankCodes;
    }

    public void setSelectedIssuerBankCodes(List<String> selectedIssuerBankCodes) {
        this.selectedIssuerBankCodes = selectedIssuerBankCodes;
    }

    public List<String> getSelectedACQBankCodes() {
        return selectedACQBankCodes;
    }

    public void setSelectedACQBankCodes(List<String> selectedACQBankCodes) {
        this.selectedACQBankCodes = selectedACQBankCodes;
    }

    public List<String> getSelectedMerchantCodes() {
        return selectedMerchantCodes;
    }

    public void setSelectedMerchantCodes(List<String> selectedMerchantCodes) {
        this.selectedMerchantCodes = selectedMerchantCodes;
    }

    public List<String> getMerchantCodeList() {
        return merchantCodeList;
    }

    public void setMerchantCodeList(List<String> merchantCodeList) {
        this.merchantCodeList = merchantCodeList;
    }

    public List<String> getAcqBankCodeList() {
        return acqBankCodeList;
    }

    public void setAcqBankCodeList(List<String> acqBankCodeList) {
        this.acqBankCodeList = acqBankCodeList;
    }

    public List<String> getIssuerBankCodeList() {
        return issuerBankCodeList;
    }

    public void setIssuerBankCodeList(List<String> issuerBankCodeList) {
        this.issuerBankCodeList = issuerBankCodeList;
    }
}
