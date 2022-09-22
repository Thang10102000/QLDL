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
import vn.com.telsoft.entity.CenterCodeBundleTransaction;
import vn.com.telsoft.model.CenterCodeBundleTransactionModel;
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
public class CenterCodeBundleTransactionController extends TSFuncTemplate implements Serializable {

    private List<CenterCodeBundleTransaction> mlistApp;
    private CenterCodeBundleTransaction mtmpApp;
    private List<CenterCodeBundleTransaction> mselectedApp;
    private CenterCodeBundleTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "ccb_transaction_template.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "CENTER_CODE_";
    private Date requestTimeFrom;
    private Date requestTimeTo;
    private String searchInput;

    private List<String> selectedProductNames = new ArrayList<>();

    public CenterCodeBundleTransactionController() throws Exception {
        mmodel = new CenterCodeBundleTransactionModel();
        mlistApp = new ArrayList<>();
        requestTimeTo = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        requestTimeFrom = calendar.getTime();

        searchInput = "";
    }
    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new CenterCodeBundleTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(CenterCodeBundleTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (CenterCodeBundleTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(CenterCodeBundleTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (CenterCodeBundleTransaction) SerializationUtils.clone(app);
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
            mtmpApp = new CenterCodeBundleTransaction();

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

    public void handleDelete(CenterCodeBundleTransaction ett) throws Exception {
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
            mlistApp = mmodel.getCenterCodeBundleTransactions( this.requestTimeFrom, this.requestTimeTo,
                    this.selectedProductNames, this.searchInput);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(CenterCodeBundleTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (CenterCodeBundleTransaction) SerializationUtils.clone(app);
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<CenterCodeBundleTransaction> getMlistApp() {
        return mlistApp;
    }

    public CenterCodeBundleTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(CenterCodeBundleTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<CenterCodeBundleTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<CenterCodeBundleTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = "";

        if (requestTimeFrom != null & requestTimeTo != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(requestTimeFrom, "MM-dd") + "_to_" +
                            DateFormatUtils.format(requestTimeTo, "MM-dd") + ".xls";
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

    // =================================== GETTER & SETTER ====================================

    public void setMlistApp(List<CenterCodeBundleTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public CenterCodeBundleTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(CenterCodeBundleTransactionModel mmodel) {
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

    public Date getRequestTimeFrom() {
        return requestTimeFrom;
    }

    public void setRequestTimeFrom(Date requestTimeFrom) {
        this.requestTimeFrom = requestTimeFrom;
    }

    public Date getRequestTimeTo() {
        return requestTimeTo;
    }

    public void setRequestTimeTo(Date requestTimeTo) {
        this.requestTimeTo = requestTimeTo;
    }

    public List<String> getSelectedProductNames() {
        return selectedProductNames;
    }

    public void setSelectedProductNames(List<String> selectedProductNames) {
        this.selectedProductNames = selectedProductNames;
    }
}
