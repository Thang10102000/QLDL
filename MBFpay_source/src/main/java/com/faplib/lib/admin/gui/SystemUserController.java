//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.faplib.lib.admin.gui;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.Session;
import com.faplib.lib.ClientMessage.MESSAGE_TYPE;
import com.faplib.lib.TSCookie;
import com.faplib.lib.admin.gui.data.UserGuiModel;
import com.faplib.lib.admin.gui.entity.UserDTL;
import com.faplib.lib.admin.security.PolicyProcessor;
import com.faplib.lib.config.Constant;
import com.faplib.lib.util.ResourceBundleUtil;
import com.faplib.util.StringUtil;
import java.io.Serializable;
import java.util.Date;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class SystemUserController implements Serializable {
    private static final long serialVersionUID = 1L;
    private UserDTL muserLogged = AdminUser.getUserLogged();
    private String mstrOldPassword = "";
    private String mstrNewPassword = "";
    private String mstrConfirmPassword = "";
    private String mstrReferer;

    public SystemUserController() {
    }

    public String getMstrConfirmPassword() {
        return this.mstrConfirmPassword;
    }

    public void setMstrConfirmPassword(String mstrConfirmPassword) {
        this.mstrConfirmPassword = mstrConfirmPassword;
    }

    public String getMstrNewPassword() {
        return this.mstrNewPassword;
    }

    public void setMstrNewPassword(String mstrNewPassword) {
        this.mstrNewPassword = mstrNewPassword;
    }

    public String getMstrOldPassword() {
        return this.mstrOldPassword;
    }

    public void setMstrOldPassword(String mstrOldPassword) {
        this.mstrOldPassword = mstrOldPassword;
    }

    public String handChangePassword() throws Exception {
        if (!StringUtil.encryptPassword(this.mstrOldPassword).equals(this.muserLogged.getPassword())) {
            ClientMessage.logPErr("PP_MNGGROUP", "old_password_not_correct");
        } else if (!this.mstrNewPassword.equals(this.mstrConfirmPassword)) {
            ClientMessage.logPErr("PP_MNGGROUP", "re_password_not_correct");
        } else {
            String strEncodePassword;
            if (PolicyProcessor.getPolicy("REQUIRE_STRONG_PASSWORD").equals("1")) {
                strEncodePassword = PolicyProcessor.getPolicy("MINIMUM_PASSWORD_LENGTH");
                if (!strEncodePassword.isEmpty() && this.mstrNewPassword.length() < Integer.parseInt(strEncodePassword)) {
                    String strErrorMess = ResourceBundleUtil.getAMObjectAsString("PP_MNGGROUP", "password_length_error");
                    ClientMessage.logErr(MESSAGE_TYPE.ADD, strErrorMess.replace("{1}", strEncodePassword));
                    return null;
                }
            }

            strEncodePassword = StringUtil.encryptPassword(this.mstrNewPassword);
            if (strEncodePassword.equals(this.muserLogged.getPassword())) {
                ClientMessage.logPErr("PP_MNGGROUP", "new_password_must_different");
            } else {
                (new UserGuiModel()).updatePassword(this.muserLogged.getUserId(), this.muserLogged.getPassword(), strEncodePassword);
                this.muserLogged.setPassword(StringUtil.encryptPassword(this.mstrNewPassword));
                Session.setSessionValue(Constant.K_USER_LOGGED, this.muserLogged);
                if (AdminUser.getUserLogged().getExpireStatus() == 0L || AdminUser.getUserLogged().getModifiedPassword() == null) {
                    this.muserLogged.setModifiedPassword(new Date());
                    this.muserLogged.setExpireStatus(1L);
                    //PrimeFaces.current().executeScript("location.reload();");
                }
                TSCookie.removeCookie(Constant.K_COOKIE_USER_COMEBACK);
                Session.destroySession();
                ClientMessage.logPUpdate("PP_MNGGROUP", "change_password_success");
                return "login?faces-redirect=true";
            }
        }
        return null;
    }
}
