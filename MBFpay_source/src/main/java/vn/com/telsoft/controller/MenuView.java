package vn.com.telsoft.controller;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
public class MenuView implements Serializable {

    private MenuModel model;

    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();

        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu();
        firstSubmenu.setLabel("Options");

        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue("Save (Non-Ajax)");
        item.setIcon("pi pi-save");
        item.setAjax(false);
        item.setCommand("#{menuView.save}");
        item.setUpdate("messages");
        firstSubmenu.getElements().add(item);

        item = new DefaultMenuItem();
        item.setValue("Update");
        item.setIcon("pi pi-refresh");
        item.setCommand("#{menuView.update}");
        item.setUpdate("messages");
        firstSubmenu.getElements().add(item);

        item = new DefaultMenuItem();
        item.setValue("Delete");
        item.setIcon("pi pi-times");
        item.setCommand("#{menuView.delete}");
        firstSubmenu.getElements().add(item);

        model.getElements().add(firstSubmenu);

        //Second submenu
        DefaultSubMenu secondSubmenu = new DefaultSubMenu();
        secondSubmenu.setLabel("Navigations");

        item = new DefaultMenuItem();
        item.setValue("Website");
        item.setUrl("http://www.primefaces.org");
        item.setIcon("pi pi-external-link");
        secondSubmenu.getElements().add(item);

        item = new DefaultMenuItem();
        item.setValue("Internal");
        item.setIcon("pi pi-upload");
        item.setCommand("#{menuView.redirect}");
        secondSubmenu.getElements().add(item);

        model.getElements().add(secondSubmenu);
    }

    public MenuModel getModel() {
        return model;
    }

    public void redirect() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath());
    }

    public void save() {
        addMessage("Save", "Data saved");
    }

    public void update() {
        addMessage("Update", "Data updated");
    }

    public void delete() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Delete", "Data deleted");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}