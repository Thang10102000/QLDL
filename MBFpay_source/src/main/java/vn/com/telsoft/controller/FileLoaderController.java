/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.FileLoader;
import vn.com.telsoft.model.FileLoaderModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class FileLoaderController extends TSFuncTemplate implements Serializable {

    private List<FileLoader> mlistApp;
    private FileLoader mtmpApp;
    private List<FileLoader> mselectedApp;
    private FileLoaderModel mmodel;

    public FileLoaderController() throws Exception {
        mmodel = new FileLoaderModel();
        mlistApp = mmodel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new FileLoader();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(FileLoader app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (FileLoader) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(FileLoader app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (FileLoader) SerializationUtils.clone(app);
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
            mtmpApp = new FileLoader();

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

    public void handleDelete(FileLoader ett) throws Exception {
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
    public void changeStateView(FileLoader app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<FileLoader> getMlistApp() {
        return mlistApp;
    }

    public FileLoader getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(FileLoader mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<FileLoader> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<FileLoader> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }
}
