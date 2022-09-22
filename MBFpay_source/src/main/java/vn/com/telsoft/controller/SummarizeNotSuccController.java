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
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SummarizeNotSucc;
import vn.com.telsoft.entity.SummarizeSucc;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SummarizeNotSuccModel;
import vn.com.telsoft.model.SummarizeSuccModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class SummarizeNotSuccController extends TSFuncTemplate implements Serializable {

    private List<SummarizeNotSucc> mlistApp;
    private SummarizeNotSucc mtmpApp;
    private List<SummarizeNotSucc> mselectedApp;
    private SummarizeNotSuccModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "temp_summarize_not_succ.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "khong_can_khop_";
    private SummarizeSuccModel msummarizeSuccModel;
    private SummarizeSucc msummarizeSucc;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private String mProfileID;
    private Date mdStartDate;
    private Date mdEndDate;


    public SummarizeNotSuccController() throws Exception {
        mmodel = new SummarizeNotSuccModel();
        mlistApp = new ArrayList<>();
        msummarizeSuccModel = new SummarizeSuccModel();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
        mdStartDate = new Date();
        mdEndDate = new Date();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new SummarizeNotSucc();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(SummarizeNotSucc app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SummarizeNotSucc) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(SummarizeNotSucc app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (SummarizeNotSucc) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleOK() throws Exception {
        if (isADD || isCOPY) {
            //Check permission
            if (!getPermission("I")) {
                return;
            }

            mmodel.add(mtmpApp);
            mlistApp.add(0, mtmpApp);

            //Reset form
            mtmpApp = new SummarizeNotSucc();

            //Message to client
            ClientMessage.logAdd();

        } else if (isEDIT) {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            mmodel.edit(mtmpApp);
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

    public void handleDelete(SummarizeNotSucc ett) throws Exception {
        //Check permission
        if (!getPermission("D")) {
            return;
        }

        if (ett == null) {
            mmodel.delete(mselectedApp);

        } else {
            mmodel.delete(Collections.singletonList(ett));
        }


        mlistApp = mmodel.getListApp();
        mselectedApp = null;

        //Message to client
        ClientMessage.logDelete();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void handleSearch() throws Exception {
        try {
            // System.out.printf("(mProfileID, mdStartDate, mdEndDate) : (%s, %s, %s)", mProfileID, mdStartDate, mdEndDate);
            mlistApp = mmodel.getListApp2(mProfileID, mdStartDate, mdEndDate);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(SummarizeNotSucc app) throws Exception {
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SummarizeNotSucc) SerializationUtils.clone(app);
    }

    public void handleProcess(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            Date dtExecute = new Date();
            mtmpApp.setStatus(input);
            mtmpApp.setExecute_datetime(dtExecute);
            mmodel.edit2(mtmpApp);
            mlistApp.set(selectedIndex, mtmpApp);

            if (input.equals("1") // Bo sung
                    || input.equals("2") //Giam tru
            ) {
                msummarizeSucc = new SummarizeSucc();
                msummarizeSucc.setInvoice_order_id(mtmpApp.getInvoice_order_id());
                msummarizeSucc.setPartner_trans_id(mtmpApp.getPartner_trans_id());
                msummarizeSucc.setProfile_id(mtmpApp.getProfile_id());
                msummarizeSucc.setStatus(input);
                msummarizeSucc.setSum_date(dtExecute);
                msummarizeSuccModel.add2(msummarizeSucc);
            }

            //Message to client
            ClientMessage.logUpdate();
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }


    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<SummarizeNotSucc> getMlistApp() {
        return mlistApp;
    }

    public SummarizeNotSucc getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(SummarizeNotSucc mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SummarizeNotSucc> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<SummarizeNotSucc> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public String getLastAmountTotal() {
        int total = 0;

        for (SummarizeNotSucc sale : mlistApp) {
            total += sale.getAmount();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getLastPartnerAmountTotal() {
        int total = 0;

        for (SummarizeNotSucc sale : mlistApp) {
            total += sale.getPartner_amount();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getLastQuantityTotal() {
        int total = 0;

        for (SummarizeNotSucc sale : mlistApp) {
            total += sale.getQuantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getLastPartnerQuantityTotal() {
        int total = 0;

        for (SummarizeNotSucc sale : mlistApp) {
            total += sale.getPartner_quantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = strPrefexFileName + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls";
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

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }

    public String getmProfileID() {
        return mProfileID;
    }

    public void setmProfileID(String mProfileID) {
        this.mProfileID = mProfileID;
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
}
