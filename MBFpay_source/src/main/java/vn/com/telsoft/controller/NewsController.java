/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.lib.util.DataUtil;
import com.faplib.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import vn.com.telsoft.entity.News;
import vn.com.telsoft.model.NewsModel;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Chien Do Xo
 */
@Named
@ViewScoped
public class NewsController extends TSFuncTemplate implements Serializable {

    private List<News> mlistNews;
    private List<News> mlistNewsFilterred;
    private News[] mselectedNews;
    private News mtmpNews;
    private boolean mbDeleteMany = false;

    private SelectItem[] statusOptions;

    public NewsController() throws Exception {
        mlistNews = DataUtil.getData(NewsModel.class, "getListAll");
    }
    ////////////////////////////////////////////////////////////////////////

    public void doubleClickSelection() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String strCell = map.get("row").trim();

        if (!StringUtils.isEmpty(strCell)) {
            for (News tmpAttr : mlistNews) {
                if (String.valueOf(tmpAttr.getNewsId()).equals(strCell)) {
                    changeStateView(tmpAttr);
                    break;
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void handleOK() throws Exception {
        if (isADD) {
            //Access database
            DataUtil.getStringData(NewsModel.class, "add", mtmpNews);

            //Access list
            mlistNews.add(0, new News(mtmpNews));

            //Reset form
            changeStateAdd();

            //Message to client
            ClientMessage.logAdd();

        } else {
            //Access database
            DataUtil.performAction(NewsModel.class, "edit", mtmpNews);

            //Message to client
            ClientMessage.logUpdate();
        }
    }
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void handleDelete() throws Exception {
        //Access database
        if (mbDeleteMany) {
            //Get ids
            String strNewsId = "";
            for (News tmp : mselectedNews) {
                strNewsId += tmp.getNewsId() + ",";
            }

            //Delete
            DataUtil.performAction(NewsModel.class, "delete", StringUtil.removeLastChar(strNewsId));

            //Refresh
            mlistNews = DataUtil.getData(NewsModel.class, "getListAll");

        } else {
            //Delete
            DataUtil.performAction(NewsModel.class, "delete", String.valueOf(mtmpNews.getNewsId()));

            //Refresh
            mlistNews = DataUtil.getData(NewsModel.class, "getListAll");
        }

        //Message to client
        ClientMessage.logDelete();
    }
    ////////////////////////////////////////////////////////////////////////

    public String dateToString(Date input) {
        if (input == null) {
            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(input);
    }
    ////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpNews = new News();
        mtmpNews.setCreatedDate(new Date());
        mtmpNews.setCreatedId(AdminUser.getUserLogged().getUserId());
        mtmpNews.setCreatedName(AdminUser.getUserLogged().getUserName());
    }
    ////////////////////////////////////////////////////////////////////////    

    public void changeStateView(News ett) throws Exception {
        super.changeStateView();
        mtmpNews = ett;
    }
    ////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(News ett) throws Exception {
        super.changeStateEdit();
        mtmpNews = ett;
        mtmpNews.setModifiedDate(new Date());
        mtmpNews.setModifiedId(AdminUser.getUserLogged().getUserId());
        mtmpNews.setModifiedName(AdminUser.getUserLogged().getUserName());
    }
    ////////////////////////////////////////////////////////////////////////

    public void changeStateDel(News ett) throws Exception {
        super.changeStateDel();
        mbDeleteMany = false;
        mtmpNews = new News(ett);
    }
    ////////////////////////////////////////////////////////////////////////  

    public void changeStateDelMany() throws Exception {
        super.changeStateDel();
        mbDeleteMany = true;
    }
    ////////////////////////////////////////////////////////////////////////

    //Setters, Gettters
    public List<News> getMlistNews() {
        return mlistNews;
    }

    public List<News> getMlistNewsFilterred() {
        return mlistNewsFilterred;
    }

    public void setMlistNewsFilterred(List<News> mlistNewsFilterred) {
        this.mlistNewsFilterred = mlistNewsFilterred;
    }

    public News[] getMselectedNews() {
        return mselectedNews;
    }

    public void setMselectedNews(News[] mselectedNews) {
        this.mselectedNews = mselectedNews;
    }

    public News getMtmpNews() {
        return mtmpNews;
    }

    public void setMtmpNews(News mtmpNews) {
        this.mtmpNews = mtmpNews;
    }

    public boolean isMbDeleteMany() {
        return mbDeleteMany;
    }

    public void setMbDeleteMany(boolean mbDeleteMany) {
        this.mbDeleteMany = mbDeleteMany;
    }

    public SelectItem[] getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(SelectItem[] statusOptions) {
        this.statusOptions = statusOptions;
    }

}
