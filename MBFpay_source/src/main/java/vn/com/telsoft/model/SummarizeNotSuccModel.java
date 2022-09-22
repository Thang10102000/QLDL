/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.SummarizeNotSucc;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @author NOINV
 */
public class SummarizeNotSuccModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<SummarizeNotSucc> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM summarize_not_succ WHERE invoice_order_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (SummarizeNotSucc profilePartner : listApp) {
                logBeforeDelete("summarize_not_succ", "invoice_order_id=" + profilePartner.getProfile_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, profilePartner.getProfile_id());
                mStmt.execute();
            }

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;
        } finally {
            close(mStmt);
            close(mConnection);
        }
    }

    public String add(SummarizeNotSucc app) throws Exception {
        String strId = "";
        try {
            open();
            mConnection.setAutoCommit(false);
            strId = String.valueOf(SQLUtil.getSequenceValue(mConnection, "summarize_not_succ_seq"));
            app.setProfile_id(strId);

            //Update app
            String strSQL = "INSERT INTO summarize_not_succ(profile_id,status,sum_date,created_time,init_time,partner_status,partner_trans_id,partner_quantity,partner_amount,confirm_user,created_user,order_status_code,order_status,related_account,account_name,payment_amount,related_fee,fee,grand_amount,sale_fee,sale_discount,quantity,amount,product_account,description,product_detail,product_name,product_code,product_id,product_service_code,product_service_id,bank_reference_id,bank_code,partner_invoice_id,partner_order_id,partner_reference_id,partner_code,pay_type_name,pay_type,sale_order_id,source_receipt_id,transaction_id,invoice_order_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            mStmt.setString(1, app.getProfile_id());
            mStmt.setString(2, app.getStatus());
            mStmt.setTimestamp(3, DateUtil.getSqlTimestamp(app.getSum_date()));
            mStmt.setTimestamp(4, DateUtil.getSqlTimestamp(app.getCreated_time()));
            mStmt.setTimestamp(5, DateUtil.getSqlTimestamp(app.getInit_time()));
            mStmt.setString(6, app.getPartner_status());
            mStmt.setString(7, app.getPartner_trans_id());
            mStmt.setInt(8, app.getPartner_quantity());
            mStmt.setInt(9, app.getPartner_amount());
            mStmt.setString(10, app.getConfirm_user());
            mStmt.setString(11, app.getCreated_user());
            mStmt.setString(12, app.getOrder_status_code());
            mStmt.setString(13, app.getOrder_status());
            mStmt.setString(14, app.getRelated_account());
            mStmt.setString(15, app.getAccount_name());
            mStmt.setString(16, app.getPayment_amount());
            mStmt.setString(17, app.getRelated_fee());
            mStmt.setString(18, app.getFee());
            mStmt.setString(19, app.getGrand_amount());
            mStmt.setString(20, app.getSale_fee());
            mStmt.setString(21, app.getSale_discount());
            mStmt.setInt(22, app.getQuantity());
            mStmt.setInt(23, app.getAmount());
            mStmt.setString(24, app.getProduct_account());
            mStmt.setString(25, app.getDescription());
            mStmt.setString(26, app.getProduct_detail());
            mStmt.setString(27, app.getProduct_name());
            mStmt.setString(28, app.getProduct_code());
            mStmt.setString(29, app.getProduct_id());
            mStmt.setString(30, app.getProduct_service_code());
            mStmt.setString(31, app.getProduct_service_id());
            mStmt.setString(32, app.getBank_reference_id());
            mStmt.setString(33, app.getBank_code());
            mStmt.setString(34, app.getPartner_invoice_id());
            mStmt.setString(35, app.getPartner_order_id());
            mStmt.setString(36, app.getPartner_reference_id());
            mStmt.setString(37, app.getPartner_code());
            mStmt.setString(38, app.getPay_type_name());
            mStmt.setString(39, app.getPay_type());
            mStmt.setString(40, app.getSale_order_id());
            mStmt.setString(41, app.getSource_receipt_id());
            mStmt.setString(42, app.getTransaction_id());
            mStmt.setString(43, app.getInvoice_order_id());
            mStmt.execute();
            logAfterInsert("summarize_not_succ", "invoice_order_id=" + strId);

            //Done
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return strId;
    }

    public void edit(SummarizeNotSucc app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("summarize_not_succ", "invoice_order_id=" + app.getProfile_id());
            String strSQL = "UPDATE summarize_not_succ SET  profile_id = ?, status = ?, sum_date = ?, created_time = ?, init_time = ?, partner_status = ?, partner_trans_id = ?, partner_quantity = ?, partner_amount = ?, confirm_user = ?, created_user = ?, order_status_code = ?, order_status = ?, related_account = ?, account_name = ?, payment_amount = ?, related_fee = ?, fee = ?, grand_amount = ?, sale_fee = ?, sale_discount = ?, quantity = ?, amount = ?, product_account = ?, description = ?, product_detail = ?, product_name = ?, product_code = ?, product_id = ?, product_service_code = ?, product_service_id = ?, bank_reference_id = ?, bank_code = ?, partner_invoice_id = ?, partner_order_id = ?, partner_reference_id = ?, partner_code = ?, pay_type_name = ?, pay_type = ?, sale_order_id = ?, source_receipt_id = ?, transaction_id = ? WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getProfile_id());
            mStmt.setString(2, app.getStatus());
            mStmt.setTimestamp(3, DateUtil.getSqlTimestamp(app.getSum_date()));
            mStmt.setTimestamp(4, DateUtil.getSqlTimestamp(app.getCreated_time()));
            mStmt.setTimestamp(5, DateUtil.getSqlTimestamp(app.getInit_time()));
            mStmt.setString(6, app.getPartner_status());
            mStmt.setString(7, app.getPartner_trans_id());
            mStmt.setInt(8, app.getPartner_quantity());
            mStmt.setInt(9, app.getPartner_amount());
            mStmt.setString(10, app.getConfirm_user());
            mStmt.setString(11, app.getCreated_user());
            mStmt.setString(12, app.getOrder_status_code());
            mStmt.setString(13, app.getOrder_status());
            mStmt.setString(14, app.getRelated_account());
            mStmt.setString(15, app.getAccount_name());
            mStmt.setString(16, app.getPayment_amount());
            mStmt.setString(17, app.getRelated_fee());
            mStmt.setString(18, app.getFee());
            mStmt.setString(19, app.getGrand_amount());
            mStmt.setString(20, app.getSale_fee());
            mStmt.setString(21, app.getSale_discount());
            mStmt.setInt(22, app.getQuantity());
            mStmt.setInt(23, app.getAmount());
            mStmt.setString(24, app.getProduct_account());
            mStmt.setString(25, app.getDescription());
            mStmt.setString(26, app.getProduct_detail());
            mStmt.setString(27, app.getProduct_name());
            mStmt.setString(28, app.getProduct_code());
            mStmt.setString(29, app.getProduct_id());
            mStmt.setString(30, app.getProduct_service_code());
            mStmt.setString(31, app.getProduct_service_id());
            mStmt.setString(32, app.getBank_reference_id());
            mStmt.setString(33, app.getBank_code());
            mStmt.setString(34, app.getPartner_invoice_id());
            mStmt.setString(35, app.getPartner_order_id());
            mStmt.setString(36, app.getPartner_reference_id());
            mStmt.setString(37, app.getPartner_code());
            mStmt.setString(38, app.getPay_type_name());
            mStmt.setString(39, app.getPay_type());
            mStmt.setString(40, app.getSale_order_id());
            mStmt.setString(41, app.getSource_receipt_id());
            mStmt.setString(42, app.getTransaction_id());
            mStmt.setString(43, app.getInvoice_order_id());
            mStmt.execute();
            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }

    public void edit2(SummarizeNotSucc app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("summarize_not_succ", "rowid='" + app.getRowid() + "'");
            String strSQL = "UPDATE summarize_not_succ SET status = ?, execute_datetime = ? WHERE  rowid = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getExecute_datetime()));
            mStmt.setString(3, app.getRowid());
            mStmt.execute();
            logAfterUpdate(listChange);
            close(mStmt);

            //Commit
            mConnection.commit();

        } catch (Exception ex) {
            mConnection.rollback();
            throw ex;

        } finally {
            close(mStmt);
            close();
        }
    }

    public List<SummarizeNotSucc> getListApp() throws Exception {
        List<SummarizeNotSucc> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT b.profile_id,\n" +
                    "       b.status,\n" +
                    "       b.sum_date,\n" +
                    "       a.created_time,\n" +
                    "       a.init_time,\n" +
                    "       b.partner_status,\n" +
                    "       b.partner_trans_id,\n" +
                    "       b.partner_quantity,\n" +
                    "       b.partner_amount,\n" +
                    "       a.confirm_user,\n" +
                    "       a.created_user,\n" +
                    "       a.order_status_code,\n" +
                    "       a.order_status,\n" +
                    "       a.related_account,\n" +
                    "       a.account_name,\n" +
                    "       a.payment_amount,\n" +
                    "       a.related_fee,\n" +
                    "       a.fee,\n" +
                    "       a.grand_amount,\n" +
                    "       a.sale_fee,\n" +
                    "       a.sale_discount,\n" +
                    "       a.quantity,\n" +
                    "       a.amount,\n" +
                    "       a.product_account,\n" +
                    "       a.description,\n" +
                    "       a.product_detail,\n" +
                    "       a.product_name,\n" +
                    "       a.product_code,\n" +
                    "       a.product_id,\n" +
                    "       a.product_service_code,\n" +
                    "       a.product_service_id,\n" +
                    "       a.bank_reference_id,\n" +
                    "       a.bank_code,\n" +
                    "       a.partner_invoice_id,\n" +
                    "       a.partner_order_id,\n" +
                    "       a.partner_reference_id,\n" +
                    "       a.partner_code,\n" +
                    "       a.pay_type_name,\n" +
                    "       a.pay_type,\n" +
                    "       a.sale_order_id,\n" +
                    "       a.source_receipt_id,\n" +
                    "       a.transaction_id,\n" +
                    "       a.invoice_order_id,\n" +
                    "       c.code,\n" +
                    "       b.ROWID\n" +
                    "FROM mobishop a, summarize_not_succ b, profile_partner c\n" +
                    "WHERE b.profile_id = c.profile_id AND a.invoice_order_id = b.invoice_order_id\n" +
                    "ORDER BY c.code, a.invoice_order_id";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SummarizeNotSucc tmpSummarizeNotSucc = new SummarizeNotSucc();
                tmpSummarizeNotSucc.setProfile_id(mRs.getString(1));
                tmpSummarizeNotSucc.setStatus(mRs.getString(2));
                tmpSummarizeNotSucc.setSum_date(mRs.getTimestamp(3));
                tmpSummarizeNotSucc.setCreated_time(mRs.getTimestamp(4));
                tmpSummarizeNotSucc.setInit_time(mRs.getTimestamp(5));
                tmpSummarizeNotSucc.setPartner_status(mRs.getString(6));
                tmpSummarizeNotSucc.setPartner_trans_id(mRs.getString(7));
                tmpSummarizeNotSucc.setPartner_quantity(mRs.getInt(8));
                tmpSummarizeNotSucc.setPartner_amount(mRs.getInt(9));
                tmpSummarizeNotSucc.setConfirm_user(mRs.getString(10));
                tmpSummarizeNotSucc.setCreated_user(mRs.getString(11));
                tmpSummarizeNotSucc.setOrder_status_code(mRs.getString(12));
                tmpSummarizeNotSucc.setOrder_status(mRs.getString(13));
                tmpSummarizeNotSucc.setRelated_account(mRs.getString(14));
                tmpSummarizeNotSucc.setAccount_name(mRs.getString(15));
                tmpSummarizeNotSucc.setPayment_amount(mRs.getString(16));
                tmpSummarizeNotSucc.setRelated_fee(mRs.getString(17));
                tmpSummarizeNotSucc.setFee(mRs.getString(18));
                tmpSummarizeNotSucc.setGrand_amount(mRs.getString(19));
                tmpSummarizeNotSucc.setSale_fee(mRs.getString(20));
                tmpSummarizeNotSucc.setSale_discount(mRs.getString(21));
                tmpSummarizeNotSucc.setQuantity(mRs.getInt(22));
                tmpSummarizeNotSucc.setAmount(mRs.getInt(23));
                tmpSummarizeNotSucc.setProduct_account(mRs.getString(24));
                tmpSummarizeNotSucc.setDescription(mRs.getString(25));
                tmpSummarizeNotSucc.setProduct_detail(mRs.getString(26));
                tmpSummarizeNotSucc.setProduct_name(mRs.getString(27));
                tmpSummarizeNotSucc.setProduct_code(mRs.getString(28));
                tmpSummarizeNotSucc.setProduct_id(mRs.getString(29));
                tmpSummarizeNotSucc.setProduct_service_code(mRs.getString(30));
                tmpSummarizeNotSucc.setProduct_service_id(mRs.getString(31));
                tmpSummarizeNotSucc.setBank_reference_id(mRs.getString(32));
                tmpSummarizeNotSucc.setBank_code(mRs.getString(33));
                tmpSummarizeNotSucc.setPartner_invoice_id(mRs.getString(34));
                tmpSummarizeNotSucc.setPartner_order_id(mRs.getString(35));
                tmpSummarizeNotSucc.setPartner_reference_id(mRs.getString(36));
                tmpSummarizeNotSucc.setPartner_code(mRs.getString(37));
                tmpSummarizeNotSucc.setPay_type_name(mRs.getString(38));
                tmpSummarizeNotSucc.setPay_type(mRs.getString(39));
                tmpSummarizeNotSucc.setSale_order_id(mRs.getString(40));
                tmpSummarizeNotSucc.setSource_receipt_id(mRs.getString(41));
                tmpSummarizeNotSucc.setTransaction_id(mRs.getString(42));
                tmpSummarizeNotSucc.setInvoice_order_id(mRs.getString(43));
                tmpSummarizeNotSucc.setCode(mRs.getString(44));
                tmpSummarizeNotSucc.setRowid(mRs.getString(45));
                listReturn.add(tmpSummarizeNotSucc);
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

    public List<SummarizeNotSucc> getListApp2(String profileID, Date dtStartDate, Date dtEndDate) throws Exception {
        List<SummarizeNotSucc> listReturn = new ArrayList<>();
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            open();
            String strSQL = "SELECT b.profile_id,\n" +
                    "       b.status,\n" +
                    "       b.sum_date,\n" +
                    "       a.created_time,\n" +
                    "       a.init_time,\n" +
                    "       b.partner_status,\n" +
                    "       b.partner_trans_id,\n" +
                    "       b.partner_quantity,\n" +
                    "       b.partner_amount,\n" +
                    "       a.confirm_user,\n" +
                    "       a.created_user,\n" +
                    "       a.order_status_code,\n" +
                    "       a.order_status,\n" +
                    "       a.related_account,\n" +
                    "       a.account_name,\n" +
                    "       a.payment_amount,\n" +
                    "       a.related_fee,\n" +
                    "       a.fee,\n" +
                    "       a.grand_amount,\n" +
                    "       a.sale_fee,\n" +
                    "       a.sale_discount,\n" +
                    "       a.quantity,\n" +
                    "       a.amount,\n" +
                    "       a.product_account,\n" +
                    "       a.description,\n" +
                    "       a.product_detail,\n" +
                    "       a.product_name,\n" +
                    "       a.product_code,\n" +
                    "       a.product_id,\n" +
                    "       a.product_service_code,\n" +
                    "       a.product_service_id,\n" +
                    "       a.bank_reference_id,\n" +
                    "       a.bank_code,\n" +
                    "       a.partner_invoice_id,\n" +
                    "       a.partner_order_id,\n" +
                    "       a.partner_reference_id,\n" +
                    "       a.partner_code,\n" +
                    "       a.pay_type_name,\n" +
                    "       a.pay_type,\n" +
                    "       a.sale_order_id,\n" +
                    "       a.source_receipt_id,\n" +
                    "       a.transaction_id,\n" +
                    "       a.invoice_order_id,\n" +
                    "       c.code,\n" +
                    "       b.ROWID\n" +
                    "FROM mobishop a, summarize_not_succ b, profile_partner c\n" +
                    "WHERE     b.profile_id = c.profile_id\n" +
                    "      AND a.invoice_order_id = b.invoice_order_id\n" +
                    "      $PROFILE_ID$ " +
                    "      AND b.sum_date >= TRUNC (?)\n" +
                    "      AND b.sum_date < TRUNC (?) + 1\n" +
                    "ORDER BY c.code, a.invoice_order_id";

            if (profileID != null && !profileID.isEmpty()) {
                strSQL = strSQL.replace("$PROFILE_ID$", " AND c.profile_id = ? ");
                // Profile ID
                vtRow = new Vector();
                vtRow.addElement("String");
                vtRow.addElement(profileID);
                vtParams.add(vtRow);
            } else {
                strSQL = strSQL.replace("$PROFILE_ID$", "");
            }

            // dtStartDate
            vtRow = new Vector();
            vtRow.addElement("Timestamp");
            vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtStartDate));
            vtParams.add(vtRow);

            // dtEndDate
            vtRow = new Vector();
            vtRow.addElement("Timestamp");
            vtRow.addElement(vn.com.telsoft.util.DateUtil.getSqlTimestamp(dtEndDate));
            vtParams.add(vtRow);

            mStmt = mConnection.prepareStatement(strSQL);
            for (int i = 0; i < vtParams.size(); i++) {
                Vector vtTemp = (Vector) vtParams.get(i);
                String strType = vtTemp.elementAt(0).toString();
                switch (strType) {
                    case "String":
                        mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                        break;
                    case "Blob":
                        mStmt.setBlob(i + 1, (InputStream) vtTemp.elementAt(1));
                        break;
                    case "Timestamp":
                        mStmt.setTimestamp(i + 1, (Timestamp) vtTemp.elementAt(1));
                        break;
                    default:
                        mStmt.setString(i + 1, vtTemp.elementAt(1).toString());
                        break;
                }
            }
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SummarizeNotSucc tmpSummarizeNotSucc = new SummarizeNotSucc();
                tmpSummarizeNotSucc.setProfile_id(mRs.getString(1));
                tmpSummarizeNotSucc.setStatus(mRs.getString(2));
                tmpSummarizeNotSucc.setSum_date(mRs.getTimestamp(3));
                tmpSummarizeNotSucc.setCreated_time(mRs.getTimestamp(4));
                tmpSummarizeNotSucc.setInit_time(mRs.getTimestamp(5));
                tmpSummarizeNotSucc.setPartner_status(mRs.getString(6));
                tmpSummarizeNotSucc.setPartner_trans_id(mRs.getString(7));
                tmpSummarizeNotSucc.setPartner_quantity(mRs.getInt(8));
                tmpSummarizeNotSucc.setPartner_amount(mRs.getInt(9));
                tmpSummarizeNotSucc.setConfirm_user(mRs.getString(10));
                tmpSummarizeNotSucc.setCreated_user(mRs.getString(11));
                tmpSummarizeNotSucc.setOrder_status_code(mRs.getString(12));
                tmpSummarizeNotSucc.setOrder_status(mRs.getString(13));
                tmpSummarizeNotSucc.setRelated_account(mRs.getString(14));
                tmpSummarizeNotSucc.setAccount_name(mRs.getString(15));
                tmpSummarizeNotSucc.setPayment_amount(mRs.getString(16));
                tmpSummarizeNotSucc.setRelated_fee(mRs.getString(17));
                tmpSummarizeNotSucc.setFee(mRs.getString(18));
                tmpSummarizeNotSucc.setGrand_amount(mRs.getString(19));
                tmpSummarizeNotSucc.setSale_fee(mRs.getString(20));
                tmpSummarizeNotSucc.setSale_discount(mRs.getString(21));
                tmpSummarizeNotSucc.setQuantity(mRs.getInt(22));
                tmpSummarizeNotSucc.setAmount(mRs.getInt(23));
                tmpSummarizeNotSucc.setProduct_account(mRs.getString(24));
                tmpSummarizeNotSucc.setDescription(mRs.getString(25));
                tmpSummarizeNotSucc.setProduct_detail(mRs.getString(26));
                tmpSummarizeNotSucc.setProduct_name(mRs.getString(27));
                tmpSummarizeNotSucc.setProduct_code(mRs.getString(28));
                tmpSummarizeNotSucc.setProduct_id(mRs.getString(29));
                tmpSummarizeNotSucc.setProduct_service_code(mRs.getString(30));
                tmpSummarizeNotSucc.setProduct_service_id(mRs.getString(31));
                tmpSummarizeNotSucc.setBank_reference_id(mRs.getString(32));
                tmpSummarizeNotSucc.setBank_code(mRs.getString(33));
                tmpSummarizeNotSucc.setPartner_invoice_id(mRs.getString(34));
                tmpSummarizeNotSucc.setPartner_order_id(mRs.getString(35));
                tmpSummarizeNotSucc.setPartner_reference_id(mRs.getString(36));
                tmpSummarizeNotSucc.setPartner_code(mRs.getString(37));
                tmpSummarizeNotSucc.setPay_type_name(mRs.getString(38));
                tmpSummarizeNotSucc.setPay_type(mRs.getString(39));
                tmpSummarizeNotSucc.setSale_order_id(mRs.getString(40));
                tmpSummarizeNotSucc.setSource_receipt_id(mRs.getString(41));
                tmpSummarizeNotSucc.setTransaction_id(mRs.getString(42));
                tmpSummarizeNotSucc.setInvoice_order_id(mRs.getString(43));
                tmpSummarizeNotSucc.setCode(mRs.getString(44));
                tmpSummarizeNotSucc.setRowid(mRs.getString(45));
                listReturn.add(tmpSummarizeNotSucc);
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
