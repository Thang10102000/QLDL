//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.faplib.lib.admin.gui;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.Session;
import com.faplib.lib.SystemConfig;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.admin.gui.data.AppGuiModel;
import com.faplib.lib.admin.gui.data.MobileModuleGuiModel;
import com.faplib.lib.admin.gui.entity.AppGUI;
import com.faplib.lib.admin.gui.entity.MenuGUIAuthorizator;
import com.faplib.lib.admin.gui.entity.ModuleGUI;
import com.faplib.lib.admin.security.Authorizator;
import com.faplib.lib.config.Config;
import com.faplib.lib.config.Constant;
import com.faplib.util.StringUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

@Named
@ViewScoped
public class MenuController implements Serializable {
    private static final long serialVersionUID = 1L;
    private String HTML_MENU_ITEM = "<li id=\"{id}\" class=\"tls_module\"><a href=\"{url}\" class=\"ui-widget\"><i class=\"{icon}\"></i><span class=\"ui-menuitemspan\">{name}</span></a></li>";
    private String HTML_SUB_MENU = "<li id=\"{id}\" class=\"treeview tls_module\"><a href=\"#\" class=\"treeview-toggle\"><i class=\"{icon}\"></i><span class=\"ui-menuitemspan\">{name}</span><span class=\"pull-right-container\"><i class=\"fa fa-angle-left pull-right\"></i></span></a><ul class=\"treeview-menu\">";
    private String HTML_SUB_MENUBAR = "<li id=\"{id}\" class=\"dropdown-submenu tls_module\"><a href=\"#\" data-toggle=\"dropdown\"><i class=\"{icon}\"></i><span class=\"ui-menuitemspan\">{name}</span><span class=\"non-caret\"></span></a><ul class=\"dropdown-menu multi-level\" role=\"menu\">";
    private String HTML_SUB_MENU_END = "</ul></li>";
    private List<AppGUI> mlistApp;
    private MenuModel mdesktopMenu;
    private MenuModel mmobileMenu;
    private List<MenuGUIAuthorizator> menuAuthorizatedList = null;
    private List<ModuleGUI> mlistMobileModule;
    private String mstrHtmlMenu;
    private String mstrHtmlMenuBar;
    private String mstrHtmlMobileMenu;
    private String mstrHtmlMobileMenuBar;

    public MenuController() {
        try {
            this.menuAuthorizatedList = Authorizator.getListModuleAuthorization(AdminUser.getUserLogged().getUserId());
            if (Session.getSessionValue(Constant.K_LIST_APP_OBJECT) == null) {
                Session.setSessionValue(Constant.K_LIST_APP_OBJECT, (new AppGuiModel()).getListAppObj(SystemConfig.getConfig("APPCode")));
            }

            this.mlistApp = (List)Session.getSessionValue(Constant.K_LIST_APP_OBJECT);
            if (Session.getSessionValue(Constant.K_LIST_MOBILE_OBJECT) == null) {
                Session.setSessionValue(Constant.K_LIST_MOBILE_OBJECT, (new MobileModuleGuiModel()).getListMobileModuleByAppCode(SystemConfig.getConfig("APPCode")));
            }

            this.mlistMobileModule = (List)Session.getSessionValue(Constant.K_LIST_MOBILE_OBJECT);
            if (Config.isMobileDevice()) {
                if (Session.getSessionValue(Constant.K_MOBILE_MENU) == null) {
                    this.mmobileMenu = new DefaultMenuModel();
                    this.buildMobileMenu();
                    this.buildHtmlMobileMenu();
                    Session.setSessionValue(Constant.K_MOBILE_MENU, this.mmobileMenu);
                    Session.setSessionValue(Constant.K_HTML_MOBILE_MENU, this.mstrHtmlMobileMenu);
                    Session.setSessionValue(Constant.K_HTML_MOBILE_MENUBAR, this.mstrHtmlMobileMenuBar);
                } else {
                    this.mmobileMenu = (MenuModel)Session.getSessionValue(Constant.K_MOBILE_MENU);
                    this.mstrHtmlMobileMenu = (String)Session.getSessionValue(Constant.K_HTML_MOBILE_MENU);
                    this.mstrHtmlMobileMenuBar = (String)Session.getSessionValue(Constant.K_HTML_MOBILE_MENUBAR);
                }
            } else if (Session.getSessionValue(Constant.K_DESKTOP_MENU) == null) {
                this.mdesktopMenu = new DefaultMenuModel();
                this.buildMenuBar();
                this.buildHtmlMenu();
                Session.setSessionValue(Constant.K_DESKTOP_MENU, this.mdesktopMenu);
                Session.setSessionValue(Constant.K_HTML_MENU, this.mstrHtmlMenu);
                Session.setSessionValue(Constant.K_HTML_MENUBAR, this.mstrHtmlMenuBar);
            } else {
                this.mdesktopMenu = (MenuModel)Session.getSessionValue(Constant.K_DESKTOP_MENU);
                this.mstrHtmlMenu = (String)Session.getSessionValue(Constant.K_HTML_MENU);
                this.mstrHtmlMenuBar = (String)Session.getSessionValue(Constant.K_HTML_MENUBAR);
            }
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
        }

    }

    public String getMstrHtmlMenuBar() {
        return this.mstrHtmlMenuBar;
    }

    private void buildHtmlMenu() {
        this.mstrHtmlMenu = "";
        this.mstrHtmlMenuBar = "";
        Iterator var1 = this.mdesktopMenu.getElements().iterator();

        while(var1.hasNext()) {
            MenuElement me = (MenuElement)var1.next();
            if (me instanceof DefaultSubMenu) {
                this.mstrHtmlMenu = this.mstrHtmlMenu + this.buildHtmlSubMenu((DefaultSubMenu)me, false);
                String htmlSubMenubar = this.buildHtmlSubMenu((DefaultSubMenu)me, true);
                this.mstrHtmlMenuBar = this.mstrHtmlMenuBar + htmlSubMenubar.replaceFirst("dropdown-submenu", "dropdown").replaceFirst("non-caret", "caret");
            }
        }

    }

    private void buildHtmlMobileMenu() {
        this.mstrHtmlMobileMenu = "";
        this.mstrHtmlMobileMenuBar = "";
        Iterator var1 = this.mmobileMenu.getElements().iterator();

        while(var1.hasNext()) {
            MenuElement me = (MenuElement)var1.next();
            if (me instanceof DefaultSubMenu) {
                this.mstrHtmlMobileMenu = this.mstrHtmlMobileMenu + this.buildHtmlSubMenu((DefaultSubMenu)me, false);
                String htmlSubMenubar = this.buildHtmlSubMenu((DefaultSubMenu)me, true);
                this.mstrHtmlMobileMenuBar = this.mstrHtmlMobileMenuBar + htmlSubMenubar.replaceFirst("dropdown-submenu", "dropdown").replaceFirst("non-caret", "caret");
            }
        }

    }

    private String buildHtmlSubMenu(DefaultSubMenu sm, boolean isMenubar) {
        String html = "";
        html = html + (isMenubar ? this.HTML_SUB_MENUBAR : this.HTML_SUB_MENU).replace("{name}", sm.getLabel()).replace("{id}", sm.getId()).replace("{icon}", sm.getIcon());
        Iterator var4 = sm.getElements().iterator();

        while(var4.hasNext()) {
            MenuElement m = (MenuElement)var4.next();
            if (m instanceof DefaultSubMenu) {
                html = html + this.buildHtmlSubMenu((DefaultSubMenu)m, isMenubar);
            } else if (m instanceof DefaultMenuItem) {
                html = html + this.buildHtmlMenuItem((DefaultMenuItem)m);
            }
        }

        html = html + this.HTML_SUB_MENU_END;
        return html;
    }

    private String buildHtmlMenuItem(DefaultMenuItem m) {
        String html = "";
        html = html + this.HTML_MENU_ITEM.replace("{url}", m.getUrl()).replace("{name}", m.getTitle()).replace("{id}", m.getId()).replace("{icon}", m.getIcon());
        return html;
    }

    private void buildMobileMenu() throws Exception {
        Map mapMobileModule = new HashMap();
        Iterator var2 = this.mlistMobileModule.iterator();

        while(var2.hasNext()) {
            ModuleGUI tmpModule = (ModuleGUI)var2.next();
            mapMobileModule.put(tmpModule.getObjectId(), (Object)null);
        }

        var2 = this.mlistApp.iterator();

        while(var2.hasNext()) {
            AppGUI app = (AppGUI)var2.next();
            this.filterMenuList(app.getMlistModule());
            Iterator var4 = app.getMlistModule().iterator();

            while(var4.hasNext()) {
                ModuleGUI module = (ModuleGUI)var4.next();
                if (mapMobileModule.containsKey(module.getObjectId())) {
                    if (module.getObjType().equals("M")) {
                        MenuItem menu = this.buildMenuItemByETT(module);
                        this.mmobileMenu.addElement(menu);
                    } else if (module.getObjType().equals("H")) {
                        DefaultMenuItem tmpMenu = new DefaultMenuItem();
                        tmpMenu.setValue(module.getName());
                        tmpMenu.setUrl(module.getPath());
                        tmpMenu.setTarget("_blank");
                        this.mmobileMenu.addElement(tmpMenu);
                    } else {
                        DefaultSubMenu subMenu = this.buildSubmenuById(app.getMlistModule(), module.getObjectId(), mapMobileModule);
                        subMenu.setLabel(module.getName());
                        this.mmobileMenu.addElement(subMenu);
                    }
                }
            }
        }

    }

    private void buildMenuBar() throws Exception {
        Iterator var1 = this.mlistApp.iterator();

        while(var1.hasNext()) {
            AppGUI app = (AppGUI)var1.next();
            this.filterMenuList(app.getMlistModule());
            List<ModuleGUI> listParent = new ArrayList();
            Iterator var4 = app.getMlistModule().iterator();

            ModuleGUI parent;
            while(var4.hasNext()) {
                parent = (ModuleGUI)var4.next();
                if (parent.getParentId() == 0L) {
                    listParent.add(parent);
                }
            }

            var4 = listParent.iterator();

            while(var4.hasNext()) {
                parent = (ModuleGUI)var4.next();
                if (parent.getObjType().equals("M")) {
                    MenuItem menu = this.buildMenuItemByETT(parent);
                    this.mdesktopMenu.addElement(menu);
                } else if (parent.getObjType().equals("H")) {
                    DefaultMenuItem tmpMenu = new DefaultMenuItem();
                    tmpMenu.setValue(parent.getName());
                    tmpMenu.setUrl(parent.getPath());
                    tmpMenu.setTarget("_blank");
                    this.mdesktopMenu.addElement(tmpMenu);
                } else {
                    DefaultSubMenu subMenu = this.buildSubmenuById(app.getMlistModule(), parent.getObjectId(), (Map)null);
                    subMenu.setLabel(parent.getName());
                    this.mdesktopMenu.addElement(subMenu);
                }
            }
        }

    }

    private DefaultSubMenu buildSubmenuById(List<ModuleGUI> listModule, long id, Map mapWhiteList) throws Exception {
        ModuleGUI ettSubmenu = null;
        Iterator var7 = listModule.iterator();

        while(var7.hasNext()) {
            ModuleGUI moduleGui = (ModuleGUI)var7.next();
            if (moduleGui.getObjectId() == id) {
                ettSubmenu = moduleGui;
                break;
            }
        }

        if (ettSubmenu == null) {
            return null;
        } else {
            DefaultSubMenu returnValue = this.buildSubmenuByETT(ettSubmenu);
            List<ModuleGUI> tmpList = new ArrayList();
            Iterator var11 = listModule.iterator();

            ModuleGUI moduleGui;
            while(var11.hasNext()) {
                moduleGui = (ModuleGUI)var11.next();
                if (moduleGui.getParentId() == id) {
                    tmpList.add(moduleGui);
                }
            }

            var11 = tmpList.iterator();

            while(true) {
                do {
                    if (!var11.hasNext()) {
                        return returnValue;
                    }

                    moduleGui = (ModuleGUI)var11.next();
                } while(mapWhiteList != null && !mapWhiteList.containsKey(moduleGui.getObjectId()));

                if (moduleGui.getObjType().equals("G")) {
                    returnValue.addElement(this.buildSubmenuById(listModule, moduleGui.getObjectId(), mapWhiteList));
                } else {
                    returnValue.addElement(this.buildMenuItemByETT(moduleGui));
                }
            }
        }
    }

    private MenuItem buildMenuItemByETT(ModuleGUI moduleGUI) throws Exception {
        DefaultMenuItem menu = new DefaultMenuItem();
        menu.setRendered(moduleGUI.isRender());
        menu.setTitle(moduleGUI.getName());
        menu.setValue(moduleGUI.getName());
        menu.setUrl(Config.getBaseUrl() + moduleGUI.getPath());
        menu.setIcon(StringUtil.nvl(moduleGUI.getIcon(), "fa fa-external-link-square"));
        menu.setId("func_" + moduleGUI.getObjectId());
        return menu;
    }

    private DefaultSubMenu buildSubmenuByETT(ModuleGUI moduleGUI) {
        DefaultSubMenu subMenu = new DefaultSubMenu();
        subMenu.setRendered(moduleGUI.isRender());
        subMenu.setLabel(moduleGUI.getName());
        subMenu.setIcon(StringUtil.nvl(moduleGUI.getIcon(), "fa fa-bars"));
        subMenu.setId("func_" + moduleGUI.getObjectId());
        return subMenu;
    }

    private void filterMenuList(List<ModuleGUI> listModule) {
        Map mapMobileMudule = new HashMap();
        Iterator var3 = this.mlistMobileModule.iterator();

        while(var3.hasNext()) {
            ModuleGUI tmpMmodule = (ModuleGUI)var3.next();
            if (tmpMmodule.isIsOnlyMobile()) {
                mapMobileMudule.put(tmpMmodule.getObjectId(), (Object)null);
            }
        }

        for(int i = 0; i < listModule.size(); ++i) {
            if (((ModuleGUI)listModule.get(i)).isRender() && !mapMobileMudule.containsKey(((ModuleGUI)listModule.get(i)).getObjectId())) {
                boolean tmpFlag = true;

                for(Iterator var5 = this.menuAuthorizatedList.iterator(); var5.hasNext(); tmpFlag = true) {
                    MenuGUIAuthorizator menuAuthorizatedList1 = (MenuGUIAuthorizator)var5.next();
                    if (((ModuleGUI)listModule.get(i)).getPath().equals(menuAuthorizatedList1.getPath())) {
                        tmpFlag = false;
                        break;
                    }
                }

                if (tmpFlag) {
                    listModule.remove(i--);
                }
            } else {
                listModule.remove(i--);
            }
        }

    }

    public MenuModel getMmenuBar() {
        return this.mdesktopMenu;
    }

    public MenuModel getMmobileMenu() {
        return this.mmobileMenu;
    }

    public String getMstrHtmlMenu() {
        return Config.isMobileDevice() ? this.mstrHtmlMobileMenu : this.mstrHtmlMenu;
    }

    public void setMstrHtmlMenu(String mstrHtmlMenu) {
        this.mstrHtmlMenu = mstrHtmlMenu;
    }
}
