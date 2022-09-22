/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.SystemConfig;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.admin.data.DataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import vn.com.telsoft.entity.News;
import vn.com.telsoft.util.DateUtil;

/**
 *
 * @author Chien Do Xo
 */
public class NewsModel extends DataPreprocessor implements Serializable {

    public List<News> getListNews() throws Exception {
        List<News> listNews = new ArrayList<News>();

        try {
            open();
            String strSQL = "  "
                    + "  SELECT   a.created_date, "
                    + "           a.content "
                    + "    FROM   news a "
                    + "   WHERE   ROWNUM <= 10 AND status = 1 AND app_id = (SELECT app_id FROM am_app WHERE code = ?) "
                    + "ORDER BY   created_date DESC";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, SystemConfig.getConfig("APPCode"));
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                News tmpNews = new News();
                tmpNews.setCreatedDate(mRs.getTimestamp("created_date"));
                tmpNews.setContent(mRs.getString("content"));
                listNews.add(tmpNews);
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }

        return listNews;
    }
    ////////////////////////////////////////////////////////////////////////    
    
    public List<News> getListAll() throws Exception {
        List<News> listNews = new ArrayList<News>();

        try {
            open();
            String strSQL = "  "
                    + "  SELECT   a.news_id, "
                    + "           a.created_date, "
                    + "           a.status, "
                    + "           a.created_id, "
                    + "           (SELECT   user_name "
                    + "              FROM   am_user "
                    + "             WHERE   user_id = a.created_id) "
                    + "               created_name, "
                    + "           (SELECT   user_name "
                    + "              FROM   am_user "
                    + "             WHERE   user_id = a.modified_id) "
                    + "               modified_name, "
                    + "           a.modified_id, "
                    + "           a.modified_date, "
                    + "           a.content, "
                    + "           a.title "
                    + "    FROM   news a WHERE app_id = (SELECT app_id FROM am_app WHERE code = ?) "
                    + "ORDER BY   created_date DESC";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, SystemConfig.getConfig("APPCode"));
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                News tmpNews = new News();
                tmpNews.setNewsId(mRs.getLong("news_id"));
                tmpNews.setCreatedDate(mRs.getTimestamp("created_date"));
                tmpNews.setStatus(mRs.getString("status"));
                tmpNews.setCreatedId(mRs.getLong("created_id"));
                tmpNews.setCreatedName(mRs.getString("created_name"));
                tmpNews.setModifiedId(mRs.getLong("modified_id"));
                tmpNews.setModifiedName(mRs.getString("modified_name"));
                tmpNews.setModifiedDate(mRs.getTimestamp("modified_date"));
                tmpNews.setContent(mRs.getString("content"));
                tmpNews.setTitle(mRs.getString("title"));
                listNews.add(tmpNews);
            }

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }

        return listNews;
    }
    ////////////////////////////////////////////////////////////////////////

    public void add(News news) throws Exception {
        try {
            open();
            String strSQL = "INSERT INTO news(news_id, created_date, status, created_id, content, title, app_id) "
                    + "VALUES(?, ?, ?, ?, to_clob(?), ?, (SELECT app_id FROM am_app WHERE code = ?))";
            news.setNewsId(SQLUtil.getSequenceValue(mConnection, "NEWS_SEQ"));

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setLong(1, news.getNewsId());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(news.getCreatedDate()));
            mStmt.setString(3, news.getStatus());
            mStmt.setLong(4, news.getCreatedId());
            mStmt.setCharacterStream(5, new StringReader(news.getContent()), news.getContent().length());
            mStmt.setString(6, news.getTitle());
            mStmt.setString(7, SystemConfig.getConfig("APPCode"));
            mStmt.execute();
            logAfterInsert("news", "news_id=" + news.getNewsId());

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
    }
    ////////////////////////////////////////////////////////////////////////

    public void edit(News news) throws Exception {
        try {
            open();
            List listChange = logBeforeUpdate("news", "news_id IN (" + news.getNewsId() + ")");
            String strSQL = "UPDATE news SET title = ?, content = to_clob(?), modified_id = ?, modified_date = ?, status = ? WHERE news_id = ?";

            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, news.getTitle());
            mStmt.setCharacterStream(2, new StringReader(news.getContent()), news.getContent().length());
            mStmt.setLong(3, news.getModifiedId());
            mStmt.setTimestamp(4, DateUtil.getSqlTimestamp(news.getModifiedDate()));
            mStmt.setString(5, news.getStatus());
            mStmt.setLong(6, news.getNewsId());
            mStmt.execute();
            logAfterUpdate(listChange);

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mStmt);
            close(mRs);
            close();
        }
    }
    ////////////////////////////////////////////////////////////////////////    

    public void delete(String newsId) throws Exception {
        try {
            open();

            List listChange = logBeforeUpdate("news", "news_id IN (" + newsId + ")");
            String strSQL = "UPDATE news SET status = 0 WHERE news_id IN (" + newsId + ")";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.execute();
            logAfterUpdate(listChange);

        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }
}
