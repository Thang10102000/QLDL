/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.model;

import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.lib.util.SQLUtil;
import com.faplib.util.DateUtil;

import vn.com.telsoft.entity.SummarizeNotSucc;
import vn.com.telsoft.entity.SummarizeSucc;

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
public class SummarizeSuccModel extends AMDataPreprocessor implements Serializable {

    public void delete(List<SummarizeSucc> listApp) throws Exception {

        try {
            open();

            String strSQL = "DELETE FROM summarize_succ WHERE invoice_order_id=?";
            mConnection.setAutoCommit(false);

            //Delete app
            for (SummarizeSucc summarizeSucc : listApp) {
                logBeforeDelete("summarize_succ", "invoice_order_id=" + summarizeSucc.getInvoice_order_id());
                mStmt = mConnection.prepareStatement(strSQL);
                mStmt.setString(1, summarizeSucc.getInvoice_order_id());
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

    public String add(SummarizeSucc app) throws Exception {
        String idFromSequence;
        try {
            open();
            mConnection.setAutoCommit(false);
            idFromSequence = String.valueOf(SQLUtil.getSequenceValue(mConnection, "summarize_succ_seq"));
            app.setInvoice_order_id(idFromSequence);

            //Update app
            String strSQL = "INSERT INTO summarize_succ(status,sum_date,partner_trans_id,profile_id,invoice_order_id) VALUES (?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getSum_date()));
            mStmt.setString(3, app.getPartner_trans_id());
            mStmt.setString(4, app.getProfile_id());
            mStmt.setString(5, app.getInvoice_order_id());
            mStmt.execute();
            logAfterInsert("summarize_succ", "invoice_order_id=" + idFromSequence);

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

        return idFromSequence;
    }

    public void add2(SummarizeSucc app) throws Exception {
        try {
            open();
            mConnection.setAutoCommit(false);
            //Update app
            String strSQL = "INSERT INTO summarize_succ(status,sum_date,partner_trans_id,profile_id,invoice_order_id) VALUES (?,?,?,?,?)";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getSum_date()));
            mStmt.setString(3, app.getPartner_trans_id());
            mStmt.setString(4, app.getProfile_id());
            mStmt.setString(5, app.getInvoice_order_id());
            mStmt.execute();
            logAfterInsert("summarize_succ", "invoice_order_id=" + app.getInvoice_order_id());

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
    }

    public void edit(SummarizeSucc app) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
            List listChange = logBeforeUpdate("summarize_succ", "invoice_order_id=" + app.getInvoice_order_id());
            String strSQL = "UPDATE summarize_succ SET  status = ?, sum_date = ?, partner_trans_id = ?, profile_id = ? WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, app.getStatus());
            mStmt.setTimestamp(2, DateUtil.getSqlTimestamp(app.getSum_date()));
            mStmt.setString(3, app.getPartner_trans_id());
            mStmt.setString(4, app.getProfile_id());
            mStmt.setString(5, app.getInvoice_order_id());
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

    public List<SummarizeSucc> getListApp() throws Exception {
        List<SummarizeSucc> listReturn = new ArrayList<>();

        try {
            open();
            String strSQL = "SELECT status,sum_date,partner_trans_id,profile_id,invoice_order_id FROM summarize_succ";
            mStmt = mConnection.prepareStatement(strSQL);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                SummarizeSucc tmpSummarizeSucc = new SummarizeSucc();
                tmpSummarizeSucc.setStatus(mRs.getString(1));
                tmpSummarizeSucc.setSum_date(mRs.getTimestamp(2));
                tmpSummarizeSucc.setPartner_trans_id(mRs.getString(3));
                tmpSummarizeSucc.setProfile_id(mRs.getString(4));
                tmpSummarizeSucc.setInvoice_order_id(mRs.getString(5));
                listReturn.add(tmpSummarizeSucc);
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
    
    public List<SummarizeSucc> getListApp2(String profileID, Date dtStartDate, Date dtEndDate) throws Exception {
        List<SummarizeSucc> listReturn = new ArrayList<>();
        Vector vtParams = new Vector();
        Vector vtRow = new Vector();
        try {
            open();
            String strSQL = "SELECT b.profile_id,\n" +
                    "       b.status,\n" +
                    "       b.sum_date,\n" +
                    "       a.created_time,\n" +
                    "       a.init_time,\n" +
                    "       b.partner_trans_id,\n" +
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
                    "       nvl(a.quantity,1) quantity,\n" +
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
                    "       b.approve_status,\n" +
                    "       b.ROWID\n" +
                    "FROM mobishop a, summarize_succ b, profile_partner c\n" +
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
                SummarizeSucc tmpSummarizeSucc = new SummarizeSucc();
                tmpSummarizeSucc.setProfile_id(mRs.getString(1));
                tmpSummarizeSucc.setStatus(mRs.getString(2));
                tmpSummarizeSucc.setSum_date(mRs.getTimestamp(3));
                tmpSummarizeSucc.setCreated_time(mRs.getTimestamp(4));
                tmpSummarizeSucc.setInit_time(mRs.getTimestamp(5));
                tmpSummarizeSucc.setPartner_trans_id(mRs.getString(6));
                tmpSummarizeSucc.setConfirm_user(mRs.getString(7));
                tmpSummarizeSucc.setCreated_user(mRs.getString(8));
                tmpSummarizeSucc.setOrder_status_code(mRs.getString(9));
                tmpSummarizeSucc.setOrder_status(mRs.getString(10));
                tmpSummarizeSucc.setRelated_account(mRs.getString(11));
                tmpSummarizeSucc.setAccount_name(mRs.getString(12));
                tmpSummarizeSucc.setPayment_amount(mRs.getString(13));
                tmpSummarizeSucc.setRelated_fee(mRs.getString(14));
                tmpSummarizeSucc.setFee(mRs.getString(15));
                tmpSummarizeSucc.setGrand_amount(mRs.getString(16));
                tmpSummarizeSucc.setSale_fee(mRs.getString(17));
                tmpSummarizeSucc.setSale_discount(mRs.getString(18));
                tmpSummarizeSucc.setQuantity(mRs.getInt(19));
                tmpSummarizeSucc.setAmount(mRs.getInt(20));
                tmpSummarizeSucc.setProduct_account(mRs.getString(21));
                tmpSummarizeSucc.setDescription(mRs.getString(22));
                tmpSummarizeSucc.setProduct_detail(mRs.getString(23));
                tmpSummarizeSucc.setProduct_name(mRs.getString(24));
                tmpSummarizeSucc.setProduct_code(mRs.getString(25));
                tmpSummarizeSucc.setProduct_id(mRs.getString(26));
                tmpSummarizeSucc.setProduct_service_code(mRs.getString(27));
                tmpSummarizeSucc.setProduct_service_id(mRs.getString(28));
                tmpSummarizeSucc.setBank_reference_id(mRs.getString(29));
                tmpSummarizeSucc.setBank_code(mRs.getString(30));
                tmpSummarizeSucc.setPartner_invoice_id(mRs.getString(31));
                tmpSummarizeSucc.setPartner_order_id(mRs.getString(32));
                tmpSummarizeSucc.setPartner_reference_id(mRs.getString(33));
                tmpSummarizeSucc.setPartner_code(mRs.getString(34));
                tmpSummarizeSucc.setPay_type_name(mRs.getString(35));
                tmpSummarizeSucc.setPay_type(mRs.getString(36));
                tmpSummarizeSucc.setSale_order_id(mRs.getString(37));
                tmpSummarizeSucc.setSource_receipt_id(mRs.getString(38));
                tmpSummarizeSucc.setTransaction_id(mRs.getString(39));
                tmpSummarizeSucc.setInvoice_order_id(mRs.getString(40));
                tmpSummarizeSucc.setCode(mRs.getString(41));
                tmpSummarizeSucc.setApprove_status(mRs.getString(42));
                tmpSummarizeSucc.setRowid(mRs.getString(43));
                listReturn.add(tmpSummarizeSucc);
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
