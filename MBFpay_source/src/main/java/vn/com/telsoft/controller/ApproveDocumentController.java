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
import com.faplib.lib.admin.gui.entity.GroupDTL;
import com.faplib.lib.admin.gui.entity.GroupGUI;
import com.faplib.lib.util.DataUtil;
import com.faplib.lib.util.ResourceBundleUtil;
import com.faplib.util.FileUtil;
import org.apache.axis.utils.Admin;
import org.apache.commons.lang.SerializationUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.ApproveDocument;
import vn.com.telsoft.entity.LogApproveDocument;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.ApParamModel;
import vn.com.telsoft.model.ApproveDocumentModel;
import vn.com.telsoft.model.LogApproveDocumentModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import vn.com.telsoft.utils.EmailUtils;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class ApproveDocumentController extends TSFuncTemplate implements Serializable {
    private List<ApproveDocument> mlistApp;
    private ApproveDocument mtmpApp;
    private List<ApproveDocument> mselectedApp;
    private ApproveDocumentModel mmodel;
    private LogApproveDocumentModel mlogApproveDocumentModel;
    private LogApproveDocument mlogApproveDocument;
    //    private List<LogApproveDocument> mlistLogApproveDocument;
    private final long ltitle; //0: CV, 1: To truong, 2: Ke toan, 3: Ke toan truong
    //    private StreamedContent streamedContent;
    private String mstrFileName = "";
    private boolean isShow = true;
    private ProfilePartnerModel profilePartnerModel;
    private static List<ProfilePartner> mlistProfilePartner;
    private String mProfileID;
    private Date mdStartDate;
    private Date mdEndDate;
    private Integer cycleDate;
    private ApParamModel apParamModel;
    private String mName;
    private List<ProfilePartner> mSelectedProfilePartner;
    private String strProfileIdList;
    private List<ProfilePartner> mSelectedProfilePartner2;
    private String strProfileIdList2;

    public ApproveDocumentController() throws Exception {
        mmodel = new ApproveDocumentModel();
        mlistApp = new ArrayList<>();
        mlogApproveDocumentModel = new LogApproveDocumentModel();
        ltitle = AdminUser.getUserLogged().getTitle(); //bug
        profilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = profilePartnerModel.getListApp();
        mdStartDate = new Date();
        mdEndDate = new Date();
        apParamModel = new ApParamModel();
        mSelectedProfilePartner = new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new ApproveDocument();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(ApproveDocument app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (ApproveDocument) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(ApproveDocument app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (ApproveDocument) SerializationUtils.clone(app);
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
            mtmpApp = new ApproveDocument();

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

    public void handleDelete(ApproveDocument ett) throws Exception {
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

    public boolean checkApprove(String input) {
        if (Long.parseLong(input) == ltitle) {
            return true;
        }
        return false;
    }

    public String getHeaderApprove(String input) {
        if (Long.parseLong(input) == ltitle) {
            return "    " + ResourceBundleUtil.getCTObjectAsString("PP_APPROVEDOCUMENT", "approve");
        }
        if (input.equals(JsfConstant
                .PHE_DUYET)) {
            return ResourceBundleUtil.getCTObjectAsString("PP_APPROVEDOCUMENT", "approved");
        }
        return getStatus(input);
    }

    public String checkApproveStatus(int title, ApproveDocument app) {
        if (app != null && app.getListLogApproveDocument().size() > title) {
            return app.getListLogApproveDocument().get(title).getApprove();
        } else if (app != null && app.getListLogApproveDocument().size() == title) {
            return getStatus(app.getStatus());
        } else {
            return "";
        }
    }

    public String getApproveStyle(int title, ApproveDocument app) {
        if (app != null && app.getListLogApproveDocument().size() == title) {
            return "fa fa-exclamation-triangle bg-yellow";
        } else if (app != null && app.getListLogApproveDocument().size() < title) {
            return "fa fa-exclamation-triangle";
        } else {
            return "fa fa-check bg-green";
        }
    }

    public String getApprovedStyle(String input) {
        if (Long.parseLong(input) == ltitle) {
            return "fa fa-pencil";
        }
        return "";
    }

    public boolean checkApproveDisable(int title, ApproveDocument app) {
        if (app != null && app.getListLogApproveDocument().size() >= title) {
            return false;
        } else {
            return true;
        }
    }

    public String getStatus(String input) {
        switch (input) {
            case JsfConstant
                    .CV_PHE_DUYET:
                return JsfConstant
                        .CHO_CV_PHE_DUYET;
            case JsfConstant
                    .TO_TRUONG_PHE_DUYET:
                return JsfConstant
                        .CHO_TO_TRUONG_PHE_DUYET;
            case JsfConstant
                    .KE_TOAN_PHE_DUYET:
                return JsfConstant
                        .CHO_KE_TOAN_PHE_DUYET;
            case JsfConstant
                    .KE_TOAN_TRUONG_PHE_DUYET:
                return JsfConstant
                        .CHO_KE_TOAN_TRUONG_PHE_DUYET;
            case JsfConstant
                    .PHE_DUYET:
                return JsfConstant
                        .CHO_PHE_DUYET;
            default:
                return "";
        }
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<ApproveDocument> getMlistApp() {
        return mlistApp;
    }

    public ApproveDocument getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(ApproveDocument mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<ApproveDocument> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<ApproveDocument> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public void handlePdf(ApproveDocument app) throws Exception {
        this.isEDIT = true;
        mstrFileName = app.getPdf_path();
        isShow = true;
    }

    public void handleExcel(ApproveDocument app) throws Exception {
        this.isEDIT = true;
        mstrFileName = app.getExcel_path();
        isShow = false;
    }

    public DefaultStreamedContent downloadPdf(ApproveDocument app) throws Exception {
        File file = new File(FileUtil.getRealPath(app.getPdf_path()));
        return downloadFile(file.getName(), file.getPath(), app, "pdf");
    }

    public DefaultStreamedContent downloadExcel(ApproveDocument app) throws Exception {
        File file = new File(FileUtil.getRealPath(app.getExcel_path()));
        return downloadFile(file.getName(), file.getPath(), app, "excel");
    }

    public static DefaultStreamedContent downloadFile(String strFileName, String strFilePath, ApproveDocument app, String type) throws Exception {
        String strContentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(strFileName);
        InputStream stream = new FileInputStream(new File(strFilePath));
        String newfileName;
        String profileCode = getProfileCode(app.getProfile_id());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // If from date minus to date greater than 1 day
        if (app.getTo_date().getTime() - app.getFrom_date().getTime() > (1000 * 60 * 60 * 24))
            newfileName = app.getName() + "_" + dateFormat.format(app.getFrom_date()) + "_" + dateFormat.format(app.getTo_date());
        else
            newfileName = app.getName() + "_" + dateFormat.format(app.getFrom_date());

        // Add file extension
        if (type.equals("pdf"))
            newfileName = newfileName + ".pdf";
        else
            newfileName = newfileName + ".xlsx";
        return new DefaultStreamedContent(stream, strContentType, newfileName);
    }

    public String getMstrFileName() {
        return mstrFileName;
    }

    public void setMstrFileName(String mstrFileName) {
        this.mstrFileName = mstrFileName;
    }

    public boolean isShow() {
        return isShow;
    }

    public static String getProfileCode(String input) {
        for (ProfilePartner partner : mlistProfilePartner) {
            if (partner.getProfileID().equals(input)) {
                return partner.getCode();
            }
        }
        return "";
    }

    //////////////////////////////////////////////////////////////////////////////////

    public void handleSearch() throws Exception {
        try {
            // System.out.printf("(mProfileID, mdStartDate, mdEndDate) : (%s, %s, %s)", mProfileID, mdStartDate, mdEndDate);
            //mlistApp = mmodel.getListApp2(mName, mProfileID, mdStartDate, mdEndDate, cycleDate);
            List<ApproveDocument> newArr = new ArrayList<>();
            if (mSelectedProfilePartner2.isEmpty()) {
                mlistApp = mmodel.getListApp2(mName, mProfileID, mdStartDate, mdEndDate, cycleDate);
            } else {
                Iterator var1 = this.mSelectedProfilePartner2.iterator();
                while (var1.hasNext()) {
                    ProfilePartner profilePartner = (ProfilePartner) var1.next();
                    Stream.of(mmodel.getListApp2(mName, profilePartner.getProfileID(), mdStartDate, mdEndDate, cycleDate)).forEach(newArr::addAll);
                }
                mlistApp = newArr;
            }

        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public String getmProfileID() {
        return mProfileID;
    }

    public void setmProfileID(String mProfileID) {
        this.mProfileID = mProfileID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
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

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }

    public Integer getcycleDate() {
        return cycleDate;
    }

    public void setcycleDate(Integer cycleDate) {
        this.cycleDate = cycleDate;
    }

    public void handleApprove(ApproveDocument approveDocument) throws Exception {
        boolean blnSendEmail = false;

        //Check permission
        if (!getPermission("U")) {
            return;
        }
        selectedIndex = mlistApp.indexOf(approveDocument);
        mtmpApp = (ApproveDocument) SerializationUtils.clone(approveDocument);

        mlogApproveDocument = new LogApproveDocument();
        mlogApproveDocument.setApprove_id(mtmpApp.getApprove_id());
        mlogApproveDocument.setUser_id(String.valueOf(AdminUser.getUserLogged().getUserId()));
        mlogApproveDocument.setStatus(mtmpApp.getStatus());

        if (ltitle == JsfConstant.CV && approveDocument.getStatus().equals(JsfConstant.CV_PHE_DUYET)) {
            blnSendEmail = true;
            mtmpApp.setStatus(JsfConstant.TO_TRUONG_PHE_DUYET);
        } else if (ltitle == JsfConstant.TO_TRUONG && approveDocument.getStatus().equals(JsfConstant.TO_TRUONG_PHE_DUYET)) {
            blnSendEmail = true;
            mtmpApp.setStatus(JsfConstant.KE_TOAN_PHE_DUYET);
        } else if (ltitle == JsfConstant.KE_TOAN && approveDocument.getStatus().equals(JsfConstant.KE_TOAN_PHE_DUYET)) {
            blnSendEmail = true;
            mtmpApp.setStatus(JsfConstant.KE_TOAN_TRUONG_PHE_DUYET);
        } else if (ltitle == JsfConstant.KE_TOAN_TRUONG && approveDocument.getStatus().equals(JsfConstant.KE_TOAN_TRUONG_PHE_DUYET)) {
            blnSendEmail = true;
            mtmpApp.setStatus(JsfConstant.PHE_DUYET);
        } else {
            ClientMessage.logErr("Văn bản đã được phê duyệt thành công.");
        }

        mmodel.edit(mtmpApp);

        mlogApproveDocument.setInsert_datetime(new Date());
        mlogApproveDocumentModel.add(mlogApproveDocument);

        mtmpApp.setListLogApproveDocument((List<LogApproveDocument>) DataUtil.performAction(
                LogApproveDocumentModel.class, "getListApp", mtmpApp.getApprove_id()));

        mlistApp.set(selectedIndex, mtmpApp);

//        Send Email
        if (blnSendEmail && mtmpApp.getStatus() != "4") {
            List<String> toEmail = mmodel.getEmailByTitle(mtmpApp.getStatus());
            String fromEmail = apParamModel.getEmailUsername();
            String subject = "Biên bản chờ phê duyệt!";

            String content = "<!DOCTYPE html>\n" +
                    "<div>\n" +
                    "    <h3>Bạn đang có biên bản đối soát chờ phê duyệt:</h3>\n" +
                    "- Mã biên bản: <b>" + mtmpApp.getApprove_id() + "</b> <br>\n" +
                    "- Tên biên bản: <b>" + mtmpApp.getName() + "</b> <br>\n" +
                    "- Chu kỳ đối soát: Từ <b>" + new SimpleDateFormat("dd/MM/yyyy").format(mtmpApp.getFrom_date()) +
                    "</b> đến <b>" + new SimpleDateFormat("dd/MM/yyyy").format(mtmpApp.getTo_date()) +
                    "</b> <br>\n" +
                    "- Người vừa phê duyệt:  <b>" + AdminUser.getUserLogged().getFullName() + "</b> <br><br>\n" +
                    "Vui lòng tiếp tục vào phê duyệt biên bản.\n" +
                    "</div>";

            String userName = apParamModel.getEmailUsername().replace("@mobifone.vn", "");
            String password = apParamModel.getEmailPassword();
            String host = apParamModel.getEmailHost();
            String port = apParamModel.getEmailPort();

            //Attachment file path
            String exelPath = FileUtil.getRealPath(mtmpApp.getExcel_path());
            String pdfPath = FileUtil.getRealPath(mtmpApp.getPdf_path());

            EmailUtils.sendMail(toEmail, fromEmail, subject, content, userName,
                    password, host, port, exelPath, pdfPath, mtmpApp.getName());
        } else if (blnSendEmail && mtmpApp.getStatus() == "4") {// ke toan truong da phe duyet
            String profileID = mtmpApp.getProfile_id();
            if (profileID == null) {// if the document does not belong to a specific partner then stop
                return;
            } else {
                List<String> toEmail = mmodel.getEmailByProfileID(mtmpApp.getProfile_id());

                // If no email was set for the corresponding role, raise error
                if (toEmail.size() == 0) {
                    String errorMessage = "Không tim thấy Email nào thuộc về ";
                    switch ((int) AdminUser.getUserLogged().getTitle()) {
                        case 0:
                            errorMessage += "Tổ trưởng";
                            break;
                        case 1:
                            errorMessage += "Kế toán";
                            break;
                        case 2:
                            errorMessage += "Kế toán trưởng";
                            break;
                    }
                    ClientMessage.log(ClientMessage.MESSAGE_TYPE.ERR, errorMessage);
                    return;
                }

                String fromEmail = apParamModel.getEmailUsername(); // ds-mobifonepay@mobifone.vn
                String subject = "Số liệu đối soát";
                String userName = apParamModel.getEmailUsername().replace("@mobifone.vn", ""); // thong.nguyenminh@mobifone.vn
                String password = apParamModel.getEmailPassword();
                String host = apParamModel.getEmailHost();
                String port = apParamModel.getEmailPort();

                //Attachment file path
                String excelPath = FileUtil.getRealPath(mtmpApp.getExcel_path());
                String pdfPath = FileUtil.getRealPath(mtmpApp.getPdf_path());

                String content = "";
                String code = profilePartnerModel.getCodeByProfileID(profileID);
                FileInputStream file = new FileInputStream(excelPath);

                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
                Sheet sheet = xssfWorkbook.getSheetAt(0);

                switch (code) {
                    case "NAPAS":
                        content = getNAPASEmailContent(code, sheet);
                        break;
                    case "BIDV":
                        content = getBIDVEmailContent(code, sheet);
                        break;
                    case "SACOMBANK":
                        content = getSCBEmailContent(code, sheet);
                        break;
                    case "VIETCOMBANK":
                        content = getVCBEmailContent(code, sheet);
                        break;
                    case "VIETINBANK":
                        content = getVietinBankEmailContent(code, sheet);
                        break;
                    case "IRIS":
                        content = getIRISEmailContent(code, sheet);
                        break;
                }
//
                EmailUtils.sendMail(toEmail, fromEmail, subject, content, userName,
                        password, host, port, excelPath, pdfPath, mtmpApp.getName());
            }
        }
        //Message to client
        ClientMessage.log(ClientMessage.MESSAGE_TYPE.INF, "Phê duyệt biên bản thành công!");
    }

    public String getNAPASEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalDepositValue = 0; // Giao dịch Nạp tiền thành công(MVASCE) - B
        String totalWithdrawValue = "0"; // tổng giá trị rút tiền
        long totalPaymentValue = 0; // Giao dịch thanh toán thành công dịch vụ thông thường(MVASWL1) + Giao dịch thanh toán thành công
        // dịch vụ đặc thù(MVASWL3) - B
        String totalIBFTWithdrawValue = "0"; // tổng giá trị GD rút IBFT
        long totalRefundWL1 = 0; // Giao dịch hủy hoàn trả dịch vụ hàng hóa thông thường(MVASWL1) - B
        long totalRefundWL3 = 0; // Giao dịch hủy hoàn trả dịch vụ hàng hóa đặc thù(MVASWL3) - B
        long totalRequireValue = 0; // Số tiền phí TT MDS phải thanh toán cho Napas đã bao gồm VAT: - A
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 8; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row.getCell(0) != null
                    && row.getCell(0).getStringCellValue().equals("Số tiền phí TT MDS phải thanh toán cho Napas đã bao gồm VAT:")) {
                Cell totalValueCell = row.getCell(5);
                totalRequireValue = (long) totalValueCell.getNumericCellValue();
                break;
            }

            Cell valueCell = row.getCell(9);
            Cell serviceNameCell = row.getCell(1);

            if (valueCell != null && serviceNameCell != null) {
                valueCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                serviceNameCell.setCellType(Cell.CELL_TYPE_STRING);
                String serviceNameCellValue = serviceNameCell.getStringCellValue();

                if (serviceNameCellValue.contains("Giao dịch Nạp tiền thành công(MVASCE)")) {
                    totalDepositValue = (long) valueCell.getNumericCellValue();
                } else if (serviceNameCellValue.equals("Giao dịch thanh toán thành công dịch vụ thông thường(MVASWL1)") ||
                        serviceNameCellValue.equals("Giao dịch thanh toán thành công dịch vụ đặc thù(MVASWL3)")) {
                    totalPaymentValue += (long) valueCell.getNumericCellValue();
                } else if (serviceNameCellValue.contains("Giao dịch hủy hoàn trả dịch vụ hàng hóa thông thường(MVASWL1)")) {
                    totalRefundWL1 = (long) valueCell.getNumericCellValue() * -1;
                } else if (serviceNameCellValue.contains("Giao dịch hủy hoàn trả dịch vụ hàng hóa đặc thù(MVASWL3)")) {
                    totalRefundWL3 = (long) valueCell.getNumericCellValue() * -1;
                }
            }
        }

        content =
                "<!DOCTYPE html>\n" +
                        "<style>\n" +
                        "    #summarize {\n" +
                        "        border-collapse: collapse;\n" +
                        "        width: 100%;\n" +
                        "    }\n" +
                        "    #summarize td,\n" +
                        "    #summarize th {\n" +
                        "        border: 1px solid #ddd;\n" +
                        "        padding: 8px;\n" +
                        "    }\n" +
                        "    #summarize tr:nth-child(even) {\n" +
                        "        background-color: #f2f2f2;\n" +
                        "    }\n" +
                        "\n" +
                        "    #summarize tr:hover {\n" +
                        "        background-color: #ddd;\n" +
                        "    }\n" +
                        "\n" +
                        "    #summarize th {\n" +
                        "        padding-top: 12px;\n" +
                        "        padding-bottom: 12px;\n" +
                        "        text-align: left;\n" +
                        "        background-color: #04aa6d;\n" +
                        "        color: white;\n" +
                        "    }\n" +
                        "</style>\n" +
                        "<div>\n" +
                        "    <p>Cảm ơn Quý Ngân hàng <b>" + code + "</b> đã hợp tác cùng <b>MDS</b> trong thời gian vừa qua.</p>\n" +
                        "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                        "    <br />\n" +
                        "    <p>MDS kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> " +
                        "đến hết " +
                        "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                        "</div>\n" +
                        "<table id=\"summarize\">\n" +
                        "    <tr>\n" +
                        "        <th>Danh mục</th>\n" +
                        "        <th>Giá trị (VNĐ)</th>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>I. Tổng giá trị nạp tiền</td>\n" +
                        "        <td>" + decimalFormat.format(totalDepositValue) + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>II. Tổng giá trị rút tiền</td>\n" +
                        "        <td>" + totalWithdrawValue + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>III. Tổng giá trị GD thanh toán</td>\n" +
                        "        <td>" + decimalFormat.format(totalPaymentValue) + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>IV. Tổng giá trị giao dịch rút IBFT</td>\n" +
                        "        <td>" + totalIBFTWithdrawValue + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>V. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL1)</td>\n" +
                        "        <td>" + decimalFormat.format(totalRefundWL1) + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>VI. Tổng giá trị GD hoàn trả dịch vụ hàng hóa đặc thù (WL3)</td>\n" +
                        "        <td>" + decimalFormat.format(totalRefundWL3) + "</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>VII. Số tiền phí MDS phải thanh toán cho Quý Ngân hàng (đã bao gồm VAT)</td>\n" +
                        "        <td>" + decimalFormat.format(totalRequireValue) + "</td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "<div>\n" +
                        "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                        "\n" +
                        "    <br />\n" +
                        "    <br />\n" +
                        "    Trân trọng, <br />\n" +
                        "    MBF Money Team.\n" +
                        "</div>\n";
        return content;
    }

    public String getBIDVEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalDepositValue = 0; // GD Nạp tiền vào Ví
        long totalWithdrawValue = 0; // GD Rút tiền từ Ví
        String totalPaymentValue = "0"; // tổng giá trị GD thanh toán
        String totalIBFTWithdrawValue = "0"; // tổng giá trị GD rút IBFT
        String totalRefundWL1 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL1
        String totalRefundWL3 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa đặc thù WL3
        long totalRequireValue = 0; // số tiền phí MDS phải thanh toán
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 8; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row.getCell(0) != null && row.getCell(0).getStringCellValue().equals("Tổng số tiền phí dịch vụ BIDV được hưởng (đã bao " +
                    "gồm VAT) trong kỳ đối soát là:")) { // column D
                totalRequireValue = (long) row.getCell(6).getNumericCellValue();
                break;
            }

            if (row.getCell(3).getStringCellValue().equals("GD nạp tiền vào Ví")) { // column D
                totalDepositValue += row.getCell(6).getNumericCellValue(); // column G
            } else if (row.getCell(3).getStringCellValue().equals("GD rút tiền từ Ví")) { // column D
                totalWithdrawValue += row.getCell(6).getNumericCellValue(); // column G
            }
        }

        content = "<!DOCTYPE html>\n" +
                "<style>\n" +
                "    #summarize {\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "    }\n" +
                "    #summarize td,\n" +
                "    #summarize th {\n" +
                "        border: 1px solid #ddd;\n" +
                "        padding: 8px;\n" +
                "    }\n" +
                "    #summarize tr:nth-child(even) {\n" +
                "        background-color: #f2f2f2;\n" +
                "    }\n" +
                "\n" +
                "    #summarize tr:hover {\n" +
                "        background-color: #ddd;\n" +
                "    }\n" +
                "\n" +
                "    #summarize th {\n" +
                "        padding-top: 12px;\n" +
                "        padding-bottom: 12px;\n" +
                "        text-align: left;\n" +
                "        background-color: #04aa6d;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "<div>\n" +
                "    <p>Cảm ơn Quý Ngân hàng <b>" + code + "</b> đã hợp tác cùng <b>MDS</b> trong thời gian vừa qua.</p>\n" +
                "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                "    <br />\n" +
                "    <p><b>MDS</b> kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> " +
                "đến hết " +
                "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                "</div>\n" +
                "<table id=\"summarize\">\n" +
                "    <tr>\n" +
                "        <th>Danh mục</th>\n" +
                "        <th>Giá trị (VNĐ)</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>I. Tổng giá trị nạp tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalDepositValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>II. Tổng giá trị rút tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalWithdrawValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>III. Tổng giá trị GD thanh toán</td>\n" +
                "        <td>" + totalPaymentValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>IV. Tổng giá trị giao dịch rút IBFT</td>\n" +
                "        <td>" + totalIBFTWithdrawValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>V. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL1)</td>\n" +
                "        <td>" + totalRefundWL1 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VI. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL3)</td>\n" +
                "        <td>" + totalRefundWL3 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VII. Số tiền phí MDS phải thanh toán cho Quý Ngân hàng (đã bao gồm VAT)</td>\n" +
                "        <td>" + decimalFormat.format(totalRequireValue) + "</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                "\n" +
                "    <br />\n" +
                "    <br />\n" +
                "    Trân trọng, <br />\n" +
                "    MBF Money Team.\n" +
                "</div>\n";

        return content;
    }

    public String getSCBEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalDepositValue = 0; // Giao dịch nạp ví - B
        long totalWithdrawValue = 0; // Giao dịch rút ví - B
        long totalPaymentValue = 0; // Giao dịch thanh toán - B
        long totalIBFTWithdrawValue = 0; // Giao dịch rút IBFT - B
        String totalRefundWL1 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL1
        String totalRefundWL3 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL3
        String totalRequireValue = ""; // số tiền phí MDS phải thanh toán G17
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 9; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row.getCell(0) != null &&
                    row.getCell(0).getStringCellValue().equals("Số tiền phí TT MDS phải thanh toán cho Sacombank đã bao gồm VAT:")) {
                totalRequireValue = row.getCell(6).getStringCellValue();
                break;
            }

            Cell valueCell = row.getCell(9); // column J
            Cell serviceNameCell = row.getCell(1); // column B

            if (valueCell != null && serviceNameCell != null) {
                String serviceNameCellValue = serviceNameCell.getStringCellValue();
                switch (serviceNameCellValue) {
                    case "Giao dịch nạp ví":
                        totalDepositValue = (long) valueCell.getNumericCellValue();
                        break;
                    case "Giao dịch thanh toán":
                        totalPaymentValue += (long) valueCell.getNumericCellValue();
                        break;
                    case "Giao dịch rút ví":
                        totalWithdrawValue += (long) valueCell.getNumericCellValue();
                        break;
                    case "Giao dịch rút IBFT":
                        totalIBFTWithdrawValue = (long) valueCell.getNumericCellValue();
                        break;
                }
            }
        }

        content = "<!DOCTYPE html>\n" +
                "<style>\n" +
                "    #summarize {\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "    }\n" +
                "    #summarize td,\n" +
                "    #summarize th {\n" +
                "        border: 1px solid #ddd;\n" +
                "        padding: 8px;\n" +
                "    }\n" +
                "    #summarize tr:nth-child(even) {\n" +
                "        background-color: #f2f2f2;\n" +
                "    }\n" +
                "\n" +
                "    #summarize tr:hover {\n" +
                "        background-color: #ddd;\n" +
                "    }\n" +
                "\n" +
                "    #summarize th {\n" +
                "        padding-top: 12px;\n" +
                "        padding-bottom: 12px;\n" +
                "        text-align: left;\n" +
                "        background-color: #04aa6d;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "<div>\n" +
                "    <p>Cảm ơn Quý Ngân hàng <b>" + code + "</b> đã hợp tác cùng MDS trong thời gian vừa qua.</p>\n" +
                "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                "    <br />\n" +
                "    <p>MDS kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> " +
                "đến hết " +
                "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                "</div>\n" +
                "<table id=\"summarize\">\n" +
                "    <tr>\n" +
                "        <th>Danh mục</th>\n" +
                "        <th>Giá trị (VNĐ)</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>I. Tổng giá trị nạp tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalDepositValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>II. Tổng giá trị rút tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalWithdrawValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>III. Tổng giá trị GD thanh toán</td>\n" +
                "        <td>" + decimalFormat.format(totalPaymentValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>IV. Tổng giá trị giao dịch rút IBFT</td>\n" +
                "        <td>" + decimalFormat.format(totalIBFTWithdrawValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>V. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL1)</td>\n" +
                "        <td>" + totalRefundWL1 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VI. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL3)</td>\n" +
                "        <td>" + totalRefundWL3 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VII. Số tiền phí MDS phải thanh toán cho Quý Ngân hàng (đã bao gồm VAT)</td>\n" +
                "        <td>" + totalRequireValue + "</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                "\n" +
                "    <br />\n" +
                "    <br />\n" +
                "    Trân trọng, <br />\n" +
                "    MBF Money Team.\n" +
                "</div>\n";

        return content;
    }

    public String getVCBEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalDepositValue = 0; // Giao dịch nạp ví - B
        long totalWithdrawValue = 0; // Giao dịch rút ví - B
        String totalPaymentValue = "0"; // tổng giá trị GD thanh toán
        String totalIBFTWithdrawValue = "0"; // tổng giá trị GD rút IBFT
        String totalRefundWL1 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL1
        String totalRefundWL3 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL3
        long totalRequireValue = 0; // Số tiền phí bên B phải thanh toán cho Vietcombank đã bao gồm VAT: - A
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 10; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row.getCell(0) != null &&
                    row.getCell(0).getStringCellValue().equals("Số tiền phí bên B phải thanh toán cho Vietcombank đã bao gồm VAT:")) { //
                // column A
                Cell totalRequireValueCell = row.getCell(6); // column G
                totalRequireValue = (long) totalRequireValueCell.getNumericCellValue();
                break;
            }
            if (row.getCell(1) != null && row.getCell(1).getStringCellValue().equals("Giao dịch nạp ví")) { // column B
                totalDepositValue = (long) row.getCell(9).getNumericCellValue(); // column J
            } else if (row.getCell(1) != null && row.getCell(1).getStringCellValue().equals("Giao dịch rút ví")) { // column B
                totalWithdrawValue = (long) row.getCell(9).getNumericCellValue(); // column J
            }
        }

        content = "<!DOCTYPE html>\n" +
                "<style>\n" +
                "    #summarize {\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "    }\n" +
                "    #summarize td,\n" +
                "    #summarize th {\n" +
                "        border: 1px solid #ddd;\n" +
                "        padding: 8px;\n" +
                "    }\n" +
                "    #summarize tr:nth-child(even) {\n" +
                "        background-color: #f2f2f2;\n" +
                "    }\n" +
                "\n" +
                "    #summarize tr:hover {\n" +
                "        background-color: #ddd;\n" +
                "    }\n" +
                "\n" +
                "    #summarize th {\n" +
                "        padding-top: 12px;\n" +
                "        padding-bottom: 12px;\n" +
                "        text-align: left;\n" +
                "        background-color: #04aa6d;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "<div>\n" +
                "    <p>Cảm ơn Quý Ngân hàng <b>" + code + "</b> đã hợp tác cùng MDS trong thời gian vừa qua.</p>\n" +
                "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                "    <br />\n" +
                "    <p>MDS kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> " +
                "đến hết " +
                "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                "</div>\n" +
                "<table id=\"summarize\">\n" +
                "    <tr>\n" +
                "        <th>Danh mục</th>\n" +
                "        <th>Giá trị (VNĐ)</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>I. Tổng giá trị nạp tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalDepositValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>II. Tổng giá trị rút tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalWithdrawValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>III. Tổng giá trị GD thanh toán</td>\n" +
                "        <td>" + totalPaymentValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>IV. Tổng giá trị giao dịch rút IBFT</td>\n" +
                "        <td>" + totalIBFTWithdrawValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>V. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL1)</td>\n" +
                "        <td>" + totalRefundWL1 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VI. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL3)</td>\n" +
                "        <td>" + totalRefundWL3 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VII. Số tiền phí MDS phải thanh toán cho Quý Ngân hàng (đã bao gồm VAT)</td>\n" +
                "        <td>" + decimalFormat.format(totalRequireValue) + "</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                "\n" +
                "    <br />\n" +
                "    <br />\n" +
                "    Trân trọng, <br />\n" +
                "    MBF Money Team.\n" +
                "</div>\n";

        return content;
    }

    public String getVietinBankEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalDepositValue = 0; // tổng giá trị nạp tiền
        long totalWithdrawValue = 0; // tổng giá trị rút tiền
        String totalPaymentValue = "0"; // tổng giá trị GD thanh toán
        String totalIBFTWithdrawValue = "0"; // tổng giá trị GD rút IBFT
        String totalRefundWL1 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL1
        String totalRefundWL3 = "0"; // tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường WL3
        String totalRequireValue = ""; // số tiền phí MDS phải thanh toán
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 10; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            if (row.getCell(0) != null && row.getCell(0).getStringCellValue().contains("Số tiền phí TT MDS phải thanh toán " +
                    "cho Vietinbank đã bao gồm VAT:")) {
                totalRequireValue = row.getCell(6).getStringCellValue();
                break;
            }

            Cell serviceNameCell = row.getCell(1); // column B
            Cell valueCell = row.getCell(9); // column J

            if (valueCell != null && serviceNameCell != null) {
                if (serviceNameCell.getStringCellValue().equals("Giao dịch nạp ví")) {
                    totalDepositValue = (long) valueCell.getNumericCellValue();
                } else if (serviceNameCell.getStringCellValue().equals("Giao dịch rút ví")) {
                    totalWithdrawValue = (long) valueCell.getNumericCellValue();
                }
            }
        }

        content = "<!DOCTYPE html>\n" +
                "<style>\n" +
                "    #summarize {\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "    }\n" +
                "    #summarize td,\n" +
                "    #summarize th {\n" +
                "        border: 1px solid #ddd;\n" +
                "        padding: 8px;\n" +
                "    }\n" +
                "    #summarize tr:nth-child(even) {\n" +
                "        background-color: #f2f2f2;\n" +
                "    }\n" +
                "\n" +
                "    #summarize tr:hover {\n" +
                "        background-color: #ddd;\n" +
                "    }\n" +
                "\n" +
                "    #summarize th {\n" +
                "        padding-top: 12px;\n" +
                "        padding-bottom: 12px;\n" +
                "        text-align: left;\n" +
                "        background-color: #04aa6d;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "<div>\n" +
                "    <p>Cảm ơn Quý Ngân hàng <b>" + code + "</b> đã hợp tác cùng MDS trong thời gian vừa qua.</p>\n" +
                "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                "    <br />\n" +
                "    <p>MDS kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> " +
                "đến hết " +
                "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                "</div>\n" +
                "<table id=\"summarize\">\n" +
                "    <tr>\n" +
                "        <th>Danh mục</th>\n" +
                "        <th>Giá trị (VNĐ)</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>I. Tổng giá trị nạp tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalDepositValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>II. Tổng giá trị rút tiền</td>\n" +
                "        <td>" + decimalFormat.format(totalWithdrawValue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>III. Tổng giá trị GD thanh toán</td>\n" +
                "        <td>" + totalPaymentValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>IV. Tổng giá trị giao dịch rút IBFT</td>\n" +
                "        <td>" + totalIBFTWithdrawValue + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>V. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL1)</td>\n" +
                "        <td>" + totalRefundWL1 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VI. Tổng giá trị GD hoàn trả dịch vụ hàng hóa thông thường (WL3)</td>\n" +
                "        <td>" + totalRefundWL3 + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>VII. Số tiền phí MDS phải thanh toán cho Quý Ngân hàng (đã bao gồm VAT)</td>\n" +
                "        <td>" + totalRequireValue + "</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                "\n" +
                "    <br />\n" +
                "    <br />\n" +
                "    Trân trọng, <br />\n" +
                "    MBF Money Team.\n" +
                "</div>\n";

        return content;
    }

    public String getIRISEmailContent(String code, Sheet sheet) {
        String content = "";
        String fromDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getFrom_date());
        String toDate = new SimpleDateFormat("dd-MM-yyyy").format(mtmpApp.getTo_date());
        long totalCollectSupportRevenue = 0; // Doanh thu hỗ trợ thu hộ TT MDS phải thanh toán cho IRIS chưa trừ phí TT MDS được hưởng:
        long totalMDSSupportFee = 0; // Phí hỗ trợ thu hộ MDS được hưởng:
        long totalMDSMustPay = 0; // Số tiền MDS phải thanh toán cho IRIS
        long totalIRISMustPay = 0; // Số tiền IRIS phải thanh toán cho MDS
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        int rowNum = sheet.getPhysicalNumberOfRows();

        for (int i = 12; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            Cell feeNameCell = row.getCell(0);
            Cell valueCell = row.getCell(8);

            if (feeNameCell != null && feeNameCell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (feeNameCell.getStringCellValue().equals("Doanh thu hỗ trợ thu hộ TT MDS phải thanh toán cho IRIS chưa trừ phí TT MDS được hưởng:")) {
                    totalCollectSupportRevenue = (long) valueCell.getNumericCellValue();
                    totalMDSMustPay = (long) valueCell.getNumericCellValue();
                } else if (feeNameCell.getStringCellValue().equals("Số tiền phí IRIS phải thanh toán cho TT MDS chưa bao gồm thuế GTGT:")) {
                    totalMDSSupportFee = (long) valueCell.getNumericCellValue();
                    totalIRISMustPay = (long) valueCell.getNumericCellValue();
                    break;
                }
            }

        }

        content = "<!DOCTYPE html>\n" +
                "<style>\n" +
                "    #summarize {\n" +
                "        border-collapse: collapse;\n" +
                "        width: 100%;\n" +
                "    }\n" +
                "    #summarize td,\n" +
                "    #summarize th {\n" +
                "        border: 1px solid #ddd;\n" +
                "        padding: 8px;\n" +
                "    }\n" +
                "    #summarize tr:nth-child(even) {\n" +
                "        background-color: #f2f2f2;\n" +
                "    }\n" +
                "\n" +
                "    #summarize tr:hover {\n" +
                "        background-color: #ddd;\n" +
                "    }\n" +
                "\n" +
                "    #summarize th {\n" +
                "        padding-top: 12px;\n" +
                "        padding-bottom: 12px;\n" +
                "        text-align: left;\n" +
                "        background-color: #04aa6d;\n" +
                "        color: white;\n" +
                "    }\n" +
                "</style>\n" +
                "<div>\n" +
                "    <p>Cảm ơn Quý Merchant <b>" + code + "</b> đã hợp tác cùng <b>MDS</b> trong thời gian vừa qua.</p>\n" +
                "    <p>Chúng tôi rất mong tiếp tục nhận được sự hợp tác của Quý đối tác trong thời gian tới.</p>\n" +
                "    <br />\n" +
                "    <p><b>MDS</b> kính gửi Quý đối tác số liệu đối soát phát sinh từ <b>00h</b> ngày <b>" + fromDate + "</b> đến hết " +
                "<b>23h:59:59</b> ngày <b>" + toDate + "</b>:</p>\n" +
                "</div>\n" +
                "<table id=\"summarize\">\n" +
                "    <tr>\n" +
                "        <th>Danh mục</th>\n" +
                "        <th>Giá trị (VNĐ)</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>I. Tổng giá trị hỗ trợ thu hộ</td>\n" +
                "        <td>" + decimalFormat.format(totalCollectSupportRevenue) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>II. Phí hỗ trợ MDS được hưởng</td>\n" +
                "        <td>" + decimalFormat.format(totalMDSSupportFee) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>III.Số tiền MDS phải thanh toán cho Qúy đối tác</td>\n" +
                "        <td>" + decimalFormat.format(totalMDSMustPay) + "</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td>IV.Số tiền phí Qúy đối tác phải thanh toán cho MDS (đã bao gồm VAT)</td>\n" +
                "        <td>" + decimalFormat.format(totalIRISMustPay) + "</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "<div>\n" +
                "    <p>Chi tiết số liệu đối soát vui lòng xem tại file đính kèm.</p>\n" +
                "\n" +
                "    <br />\n" +
                "    <br />\n" +
                "    Trân trọng, <br />\n" +
                "    <b>MBF Money Team</b>.\n" +
                "</div>\n";

        return content;
    }

    public List<ProfilePartner> getmSelectedProfilePartner() {
        return mSelectedProfilePartner;
    }

    public void setmSelectedProfilePartner(List<ProfilePartner> mSelectedProfilePartner) {
        this.mSelectedProfilePartner = mSelectedProfilePartner;
    }

    public String getStrProfileIdList() {
        ProfilePartner profilePartner;
        strProfileIdList = "";

        for (Iterator var2 = this.mSelectedProfilePartner.iterator(); var2.hasNext(); strProfileIdList = strProfileIdList + profilePartner.getCode() + ", ") {
            profilePartner = (ProfilePartner) var2.next();
        }

        strProfileIdList = com.faplib.util.StringUtil.removeLastChar(strProfileIdList);
        if (strProfileIdList == "") {
            strProfileIdList = "------------------------------------------------------------ Tất cả ---------------------------------------------------------------";
        }
        return strProfileIdList;
    }

    public void setStrProfileIdList(String strProfileIdList) {
        this.strProfileIdList = strProfileIdList;
    }

    public List<ProfilePartner> getmSelectedProfilePartner2() {
        return mSelectedProfilePartner2;
    }

    public void setmSelectedProfilePartner2(List<ProfilePartner> mSelectedProfilePartner2) {
        this.mSelectedProfilePartner2 = mSelectedProfilePartner2;
    }

    public String getStrProfileIdList2() {
        return strProfileIdList2;
    }

    public void setStrProfileIdList2(String strProfileIdList2) {
        this.strProfileIdList2 = strProfileIdList2;
    }
}
