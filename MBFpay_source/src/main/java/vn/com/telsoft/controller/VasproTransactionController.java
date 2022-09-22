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
import vn.com.telsoft.entity.VasproTransaction;
import vn.com.telsoft.model.VasproTransactionModel;
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
public class VasproTransactionController extends TSFuncTemplate implements Serializable {

    private List<VasproTransaction> mlistApp;
    private VasproTransaction mtmpApp;
    private List<VasproTransaction> mselectedApp;
    private VasproTransactionModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "vaspro_transaction_template.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "VASPRO_";
    private Date regDateFrom;
    private Date regDateTo;
    private String searchInput;

    public VasproTransactionController() throws Exception {
        mmodel = new VasproTransactionModel();
        mlistApp = new ArrayList<>();
        regDateTo = new Date();

        // Set default date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        regDateFrom = calendar.getTime();

        searchInput = "";
    }
    //////////////////////////////////////////////////////////////////////////////////
    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new VasproTransaction();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(VasproTransaction app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (VasproTransaction) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(VasproTransaction app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (VasproTransaction) SerializationUtils.clone(app);
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
            mtmpApp = new VasproTransaction();

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

    public void handleDelete(VasproTransaction ett) throws Exception {
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
            mlistApp = mmodel.getVasproTransactions(
                    this.regDateFrom, this.regDateTo, this.searchInput);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(VasproTransaction app) throws Exception {
        selectedIndex = this.mlistApp.indexOf(app);
        this.mtmpApp = (VasproTransaction) SerializationUtils.clone(app);
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<VasproTransaction> getMlistApp() {
        return mlistApp;
    }

    public VasproTransaction getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(VasproTransaction mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<VasproTransaction> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<VasproTransaction> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = "";

        if (regDateFrom != null & regDateTo != null) {
            fileName =
                    strPrefexFileName + DateFormatUtils.format(regDateFrom, "MM-dd") + "_to_" +
                            DateFormatUtils.format(regDateTo, "MM-dd") + ".xls";
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

    public void setMlistApp(List<VasproTransaction> mlistApp) {
        this.mlistApp = mlistApp;
    }

    public VasproTransactionModel getMmodel() {
        return mmodel;
    }

    public void setMmodel(VasproTransactionModel mmodel) {
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

    public Date getRegDateFrom() {
        return regDateFrom;
    }

    public void setRegDateFrom(Date regDateFrom) {
        this.regDateFrom = regDateFrom;
    }

    public Date getRegDateTo() {
        return regDateTo;
    }

    public void setRegDateTo(Date regDateTo) {
        this.regDateTo = regDateTo;
    }
}
