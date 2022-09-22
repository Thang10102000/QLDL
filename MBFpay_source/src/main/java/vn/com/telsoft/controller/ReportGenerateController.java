/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemConfig;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.lib.util.ResourceBundleUtil;
import com.faplib.util.FileUtil;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.ReportGenerate;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.ReportGenerateModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class ReportGenerateController extends TSFuncTemplate implements Serializable {

    private List<ReportGenerate> mlistApp;
    private ReportGenerate mtmpApp;
    private List<ReportGenerate> mselectedApp;
    private ReportGenerateModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private List<String> mlistBlacklistSQL;
    private String mstrRptDesignPath;

    public ReportGenerateController() throws Exception {
        mmodel = new ReportGenerateModel();
        mlistApp = mmodel.getListApp();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();

        String[] listKey = StringUtils.split(SystemConfig.getConfig("ReportBlacklistSQL"), ",");
        mlistBlacklistSQL = new ArrayList<>();

        for (String s : listKey) {
            mlistBlacklistSQL.add(s.trim().toUpperCase() + " ");
        }
        mstrRptDesignPath = FileUtil.getRealPath("resources/report/temp/");
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new ReportGenerate();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(ReportGenerate app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (ReportGenerate) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(ReportGenerate app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (ReportGenerate) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleOK() throws Exception {
        if (isADD) {
            //Check permission
            if (!getPermission("I")) {
                return;
            }

            mmodel.add(mtmpApp);
            mlistApp.add(0, mtmpApp);

            //Reset form
            mtmpApp = new ReportGenerate();

            //Message to client
            ClientMessage.logAdd();

        } else if (isCOPY) {
            //Check permission
            if (!getPermission("I")) {
                return;
            }
            File pdfFile = mmodel.downloadPdf(mtmpApp, mstrRptDesignPath);
            File excelFile = mmodel.downloadExcel(mtmpApp, mstrRptDesignPath);
            mtmpApp.setExcelPath(excelFile.getPath());
            mtmpApp.setPdfPath(pdfFile.getPath());
            mmodel.add2(mtmpApp);
            mlistApp.add(0, mtmpApp);

            //Reset form
            mtmpApp = new ReportGenerate();

            //Message to client
            ClientMessage.logAdd();
    } else if (isEDIT) {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            mmodel.edit(mtmpApp);
            //mmodel.edit2(mtmpApp);
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

    public void handleDelete(ReportGenerate ett) throws Exception {
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

    public void changeStateView(ReportGenerate app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<ReportGenerate> getMlistApp() {
        return mlistApp;
    }

    public ReportGenerate getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(ReportGenerate mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<ReportGenerate> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<ReportGenerate> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }

    public String getProfileCode(String input) {
        for(ProfilePartner partner: mlistProfilePartner) {
            if(partner.getProfileID().equals(input)) {
                return partner.getCode();
            }
        }
        return "";
    }

    public void handleFileUploadPdf(FileUploadEvent event) throws Exception {
        //Check blacklist sql
        String fXmlFile = FileUtil.uploadTempFile(event.getFile().getInputstream(), "rptdesign");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        NodeList xmlProperty = doc.getElementsByTagName("xml-property");
        for (int j = 0; j < xmlProperty.getLength(); j++) {
            Node pNode = xmlProperty.item(j);
            Element pElement = (Element) pNode;
            if (pElement != null && "queryText".equals(pElement.getAttribute("name"))) {
                if(isBlacklistedSQL(pElement.getTextContent())) {
                    ClientMessage.logPErr("[" + ResourceBundleUtil.getModuleObjectAsString("sql_blacklisted") + "] => " + pElement.getTextContent());
                    return;
                }
            }
        }

        mtmpApp.setPdfDesign(event.getFile());
        mtmpApp.setPdfFileName(event.getFile().getFileName());
        mtmpApp.setPdfHash(FileUtil.hashSHA1(event.getFile().getInputstream()));
        PrimeFaces.current().resetInputs("form_main:pdf_design");
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void handleFileUploadExcel(FileUploadEvent event) throws Exception {
        //Check blacklist sql
        String fXmlFile = FileUtil.uploadTempFile(event.getFile().getInputstream(), "rptdesign");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        NodeList xmlProperty = doc.getElementsByTagName("xml-property");
        for (int j = 0; j < xmlProperty.getLength(); j++) {
            Node pNode = xmlProperty.item(j);
            Element pElement = (Element) pNode;
            if (pElement != null && "queryText".equals(pElement.getAttribute("name"))) {
                if(isBlacklistedSQL(pElement.getTextContent())) {
                    ClientMessage.logPErr("[" + ResourceBundleUtil.getModuleObjectAsString("sql_blacklisted") + "] => " + pElement.getTextContent());
                    return;
                }
            }
        }

        mtmpApp.setExcelDesign(event.getFile());
        mtmpApp.setExcelFileName(event.getFile().getFileName());
        mtmpApp.setExcelHash(FileUtil.hashSHA1(event.getFile().getInputstream()));
        PrimeFaces.current().resetInputs("form_main:excel_design");
    }
    //////////////////////////////////////////////////////////////////////////////////

    private boolean isBlacklistedSQL(String sql) {
        for (String s : mlistBlacklistSQL) {
            if (sql.toUpperCase().contains(s)) {
                return true;
            }
        }

        return false;
    }
    //////////////////////////////////////////////////////////////////////////////////

    public DefaultStreamedContent downloadTemplatePdf(ReportGenerate file) throws Exception {
        File reportFile = mmodel.downloadPdf(file, mstrRptDesignPath);
        return FileUtil.downloadFile(reportFile);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public DefaultStreamedContent downloadTemplateExcel(ReportGenerate file) throws Exception {
        File reportFile = mmodel.downloadExcel(file, mstrRptDesignPath);
        return FileUtil.downloadFile(reportFile);
    }
    //////////////////////////////////////////////////////////////////////////////////
}
