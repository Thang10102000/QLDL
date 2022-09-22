/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.admin.security.AdminUser;
import com.faplib.applet.util.StringUtil;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import org.primefaces.event.FileUploadEvent;
import vn.com.telsoft.entity.FileUpload;
import vn.com.telsoft.model.FileUploadModel;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class FileUploadController extends TSFuncTemplate implements Serializable {
    public static final int BUFFER_SIZE = 65536;
    private List<FileUpload> mlistApp;
    private FileUpload mtmpApp;
    private FileUploadModel mmodel;
    private String strDirectory = "";
    private List<SelectItem> mlistSources;
    private Date fromDate, toDate;
    private String partnerCode;
    private List<String> partnerCodeList;
    private String searchInput;

    public FileUploadController() throws Exception {
        mmodel = new FileUploadModel();
        mlistApp = mmodel.getListApp();
        mlistSources = mmodel.getListSources();
        partnerCodeList = mmodel.getListPartnerCode();
        fromDate = new Date();
        toDate = new Date();
        searchInput = "";
    }

    @Override
    public void handleOK() throws Exception {
        //Check permission
        if (!getPermission("I")) {
            return;
        }

        mmodel.add(mtmpApp);
        mlistApp.add(0, mtmpApp);

        mtmpApp = new FileUpload();

        //Message to client
        ClientMessage.log(ClientMessage.MESSAGE_TYPE.INF, "Upload file thành công!");
    }

    @Override
    public void handleDelete() throws Exception {
    }

    public void handleSearch() throws Exception {
        try {
            mlistApp = mmodel.getFileUpload(this.partnerCode, this.fromDate, this.toDate, this.searchInput);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws Exception {
        if (strDirectory.isEmpty()) {
            return;
        }
        ExternalContext extContext
                = FacesContext.getCurrentInstance().getExternalContext();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String strDesFolder = extContext.getRealPath("//resources//backup//file//");
        String strDesFolder = strDirectory;
        String strTmpFolder = extContext.getRealPath("//resources//tmp//file//");
        String strFileName = StringUtil.clearHornUnicode(event.getFile().getFileName().toLowerCase());
        String strDesFileName = strFileName;//.toUpperCase().split("\\.")[0] + df.format(new Date()) + ".txt";

        try {
            FileUtil.forceFolderExist(strDesFolder);
            if (!((strDesFolder.endsWith("/")) || (strDesFolder.endsWith("\\")))) {
                strDesFolder += "/";
            }
            FileUtil.forceFolderExist(strTmpFolder);
            if (!((strTmpFolder.endsWith("/")) || (strTmpFolder.endsWith("\\")))) {
                strTmpFolder += "/";
            }
        } catch (IOException ex) {
            SystemLogger.getLogger().error(ex);
        }
        File result = new File(strTmpFolder + strFileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(result);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            mtmpApp = new FileUpload();
            mtmpApp.setFile_name(strDesFileName);
            mtmpApp.setData_dir(strDesFolder);
            mtmpApp.setInsert_datetime(new Date());
            mtmpApp.setUser_name(AdminUser.getUserLogged().getUserName());

            // select partner code corresponding to the directory
            String partnerCode = "";
            for(SelectItem item: mlistSources){
                if(item.getValue().equals(strDirectory)){
                    partnerCode = item.getLabel();
                }

            }
            mtmpApp.setPartnerCode(partnerCode);
            handleOK();
        } catch (IOException ex) {
            SystemLogger.getLogger().error(ex);
            //xoa file trong thu muc tmp
            FileUtil.deleteFile(strTmpFolder + strFileName);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ADD, ex.getMessage());
        } finally {
            //chuyen file moi tu thu muc tmp ra thu muc backup
            FileUtil.renameFile(strTmpFolder + strFileName, strDesFolder + strDesFileName, true);
        }
    }

    public List<FileUpload> getMlistApp() {
        return mlistApp;
    }

    public String getStrDirectory() {
        return strDirectory;
    }

    public void setStrDirectory(String strDirectory) {
        this.strDirectory = strDirectory;
    }

    public List<SelectItem> getMlistSources() {
        return mlistSources;
    }

    public void setMlistSources(List<SelectItem> mlistSources) {
        this.mlistSources = mlistSources;
    }

    public List<String> getPartnerCodeList() {
        return partnerCodeList;
    }

    public void setPartnerCodeList(List<String> partnerCodeList) {
        this.partnerCodeList = partnerCodeList;
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

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
    public String getSearchInput() {
        return searchInput;
    }
    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }
}
