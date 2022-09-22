/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import vn.com.telsoft.entity.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author NOINV
 */
public class ProductsModel extends AMDataPreprocessor implements Serializable {
    public List<Products> getListApp() throws Exception {
        List<Products> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT DISTINCT\n" +
                    "    ( pay_type ),\n" +
                    "    pay_type_name\n" +
                    "FROM\n" +
                    "    mobishop";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                Products tmpProducts = new Products();
                tmpProducts.setPayType(mRs.getString(1));
                tmpProducts.setPayTypeName(mRs.getString(2));
                listReturn.add(tmpProducts);
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return listReturn;
    }
}
