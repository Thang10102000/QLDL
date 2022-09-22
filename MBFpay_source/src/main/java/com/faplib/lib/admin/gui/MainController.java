//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.faplib.lib.admin.gui;
import com.faplib.admin.security.AdminUser;
import com.faplib.lib.Session;
import com.faplib.lib.SystemConfig;
import com.faplib.lib.TSCookie;
import com.faplib.lib.admin.gui.entity.UserDTL;
import com.faplib.lib.config.Config;
import com.faplib.lib.config.Constant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;

@Named
@SessionScoped
public class MainController implements Serializable {
    public static String adminVersion = "MobiFone Money (Build 1.0)";
    private String supportedLocale;
    private Locale mLocale;

    public MainController() {
        if (TSCookie.getCookie(Constant.K_COOKIE_USER_LOCALE) != null) {
            this.mLocale = new Locale(TSCookie.getCookie(Constant.K_COOKIE_USER_LOCALE));
        } else {
            this.mLocale = Faces.getDefaultLocale();
        }

        this.supportedLocale = "," + SystemConfig.getConfig("SupportedLocale") + ",";
        Session.setSessionValue(Constant.K_MAINCONTROLLER, this);
    }

    public String getSystemVersion() {
        return adminVersion;
    }

    public String getModuleName() {
        return Config.getModuleName();
    }

    public String getModuleDescription() {
        return Config.getModuleDescription();
    }

    public long getModuleId() {
        return Config.getModuleId();
    }

    public UserDTL getUser() {
        return AdminUser.getUserLogged();
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    private void setLocale(Locale locale) throws Exception {
        this.mLocale = locale;
        Faces.setLocale(this.mLocale);
        Session.setSessionValue(Constant.K_DESKTOP_MENU, (Object)null);
    }

    public void changeLanguage(String strLanguage) throws Exception {
        this.setLocale(new Locale(strLanguage));
    }

    public void changeLanguage(Locale locale) throws Exception {
        this.setLocale(locale);
    }

    public List<Locale> getListLocale() {
        if (Session.getSessionValue(Constant.K_SUPPORTED_LOCALE) == null) {
            List<Locale> listLocale = new ArrayList();
            Map<String, String> mapLanguage = new HashMap();
            if (this.supportedLocale == null) {
                this.supportedLocale = "vi,en";
            }

            Iterator var4 = Faces.getSupportedLocales().iterator();

            while(var4.hasNext()) {
                Locale locale = (Locale)var4.next();
                if (this.supportedLocale.contains("," + locale.getLanguage() + ",") && !mapLanguage.containsKey(locale.getLanguage())) {
                    mapLanguage.put(locale.getLanguage(), null);
                    listLocale.add(locale);
                }
            }

            Session.setSessionValue(Constant.K_SUPPORTED_LOCALE, listLocale);
        }

        return (List)Session.getSessionValue(Constant.K_SUPPORTED_LOCALE);
    }

    public List<Locale> getListOtherLocale() {
        List<Locale> listLocale = new ArrayList();
        Iterator var3 = this.getListLocale().iterator();

        while(var3.hasNext()) {
            Locale locale = (Locale)var3.next();
            if (!locale.getLanguage().equals(this.mLocale.getLanguage())) {
                listLocale.add(locale);
            }
        }

        return listLocale;
    }

    public boolean isSupportedLanguage(String language) {
        return this.supportedLocale.contains("," + language + ",");
    }
}
