package vn.com.telsoft.model;

import com.faplib.admin.security.AdminUser;
import com.faplib.lib.admin.data.AMDataPreprocessor;
import com.faplib.util.DateUtil;
import vn.com.telsoft.entity.IncorrectFeeTransaction;
import vn.com.telsoft.entity.MatchedTransaction;
import vn.com.telsoft.entity.MismatchedTransaction;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class UploadAdjustedFileModel extends AMDataPreprocessor implements Serializable {
    private MismatchedTransactionModel mismatchedTransactionModel;
    private MatchedTransactionModel matchedTransactionModel;

    public UploadAdjustedFileModel() {
        this.mismatchedTransactionModel = new MismatchedTransactionModel();
        this.matchedTransactionModel = new MatchedTransactionModel();
    }

    // ====================== MISMATCHED TRANSACTIONS ===========================

    public MismatchedTransaction getMismatchedTransactionByID(String invoiceOrderID) throws Exception {
        MismatchedTransaction mismatchedTransaction = new MismatchedTransaction();

        try {
            open();
            String sqlString = "SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    mismatched_transaction\n" +
                    "WHERE\n" +
                    "    invoice_order_id = ? ";
            mStmt = mConnection.prepareStatement(sqlString);
            mStmt.setString(1, invoiceOrderID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                mismatchedTransaction.setInvoiceOrderID(mRs.getString(1));
                mismatchedTransaction.setTransactionID(mRs.getString(2));
                mismatchedTransaction.setSourceReceiptID(mRs.getString(3));
                mismatchedTransaction.setSaleOrderID(mRs.getString(4));
                mismatchedTransaction.setPayType(mRs.getString(5));
                mismatchedTransaction.setPayTypeName(mRs.getString(6));
                mismatchedTransaction.setPartnerCode(mRs.getString(7));
                mismatchedTransaction.setPartnerReferenceID(mRs.getString(8));
                mismatchedTransaction.setPartnerOrderID(mRs.getString(9));
                mismatchedTransaction.setPartnerInvoiceID(mRs.getString(10));
                mismatchedTransaction.setBankCode(mRs.getString(11));
                mismatchedTransaction.setBankReferenceID(mRs.getString(12));
                mismatchedTransaction.setProductServiceID(mRs.getString(13));
                mismatchedTransaction.setProductServiceCode(mRs.getString(14));
                mismatchedTransaction.setProductID(mRs.getString(15));
                mismatchedTransaction.setProductCode(mRs.getString(16));
                mismatchedTransaction.setProductName(mRs.getString(17));
                mismatchedTransaction.setProductDetail(mRs.getString(18));
                mismatchedTransaction.setDescription(mRs.getString(19));
                mismatchedTransaction.setProductAccount(mRs.getString(20));
                mismatchedTransaction.setAmount(mRs.getInt(21));
                mismatchedTransaction.setQuantity(mRs.getInt(22));
                mismatchedTransaction.setSaleDiscount(mRs.getString(23));
                mismatchedTransaction.setSaleFee(mRs.getString(24));
                mismatchedTransaction.setGrandAmount(mRs.getInt(25));
                mismatchedTransaction.setFee(mRs.getInt(26));
                mismatchedTransaction.setRelatedFee(mRs.getInt(27));
                mismatchedTransaction.setPaymentAmount(mRs.getInt(28));
                mismatchedTransaction.setAccountName(mRs.getString(29));
                mismatchedTransaction.setRelatedAccount(mRs.getString(30));
                mismatchedTransaction.setOrderStatus(mRs.getString(31));
                mismatchedTransaction.setOrderStatusCode(mRs.getString(32));
                mismatchedTransaction.setCreatedUser(mRs.getString(33));
                mismatchedTransaction.setConfirmUser(mRs.getString(34));
                mismatchedTransaction.setInitTime(mRs.getTimestamp(35));
                mismatchedTransaction.setCreatedTime(mRs.getTimestamp(36));
                mismatchedTransaction.setFileName(mRs.getString(37));
                mismatchedTransaction.setLogDateTime(mRs.getTimestamp(38));
                mismatchedTransaction.setImportDateTime(mRs.getTimestamp(39));
                mismatchedTransaction.setBankReferenceData(mRs.getString(40));
                mismatchedTransaction.setProfileID(mRs.getString(41));
                mismatchedTransaction.setPartnerTransID(mRs.getString(42));
                mismatchedTransaction.setForcontrolCode(mRs.getString(43));
                mismatchedTransaction.setPartnerStatus(mRs.getString(44));
                mismatchedTransaction.setSumDate(mRs.getTimestamp(45));
                mismatchedTransaction.setAdjustDate(mRs.getTimestamp(46));
                mismatchedTransaction.setProcessStatus(mRs.getString(47));
                mismatchedTransaction.setUserProcess(mRs.getString(48));
                mismatchedTransaction.setConfirmStatus(mRs.getString(49));
                mismatchedTransaction.setUserConfirm(mRs.getString(50));
                mismatchedTransaction.setAccountID(mRs.getString(51));
                mismatchedTransaction.setUserType(mRs.getString(52));
                mismatchedTransaction.setEndTime(mRs.getTimestamp(53));
                mismatchedTransaction.setTransTime(mRs.getTimestamp(54));
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return mismatchedTransaction;
    }

    // ====================== MATCHED TRANSACTIONS ===========================

    public MatchedTransaction getMatchedTransactionByID(String invoiceOrderID) throws Exception {
        MatchedTransaction matchedTransaction = new MatchedTransaction();

        try {
            open();
            String sqlString = "SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    matched_transaction\n" +
                    "WHERE\n" +
                    "    invoice_order_id = ? ";
            mStmt = mConnection.prepareStatement(sqlString);
            mStmt.setString(1, invoiceOrderID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                matchedTransaction.setInvoiceOrderID(mRs.getString(1));
                matchedTransaction.setTransactionID(mRs.getString(2));
                matchedTransaction.setSourceReceiptID(mRs.getString(3));
                matchedTransaction.setSaleOrderID(mRs.getString(4));
                matchedTransaction.setPayType(mRs.getString(5));
                matchedTransaction.setPayTypeName(mRs.getString(6));
                matchedTransaction.setPartnerCode(mRs.getString(7));
                matchedTransaction.setPartnerReferenceID(mRs.getString(8));
                matchedTransaction.setPartnerOrderID(mRs.getString(9));
                matchedTransaction.setPartnerInvoiceID(mRs.getString(10));
                matchedTransaction.setBankCode(mRs.getString(11));
                matchedTransaction.setBankReferenceID(mRs.getString(12));
                matchedTransaction.setProductServiceID(mRs.getString(13));
                matchedTransaction.setProductServiceCode(mRs.getString(14));
                matchedTransaction.setProductID(mRs.getString(15));
                matchedTransaction.setProductCode(mRs.getString(16));
                matchedTransaction.setProductName(mRs.getString(17));
                matchedTransaction.setProductDetail(mRs.getString(18));
                matchedTransaction.setDescription(mRs.getString(19));
                matchedTransaction.setProductAccount(mRs.getString(20));
                matchedTransaction.setAmount(mRs.getInt(21));
                matchedTransaction.setQuantity(mRs.getInt(22));
                matchedTransaction.setSaleDiscount(mRs.getString(23));
                matchedTransaction.setSaleFee(mRs.getString(24));
                matchedTransaction.setGrandAmount(mRs.getInt(25));
                matchedTransaction.setFee(mRs.getInt(26));
                matchedTransaction.setRelatedFee(mRs.getInt(27));
                matchedTransaction.setPaymentAmount(mRs.getInt(28));
                matchedTransaction.setAccountName(mRs.getString(29));
                matchedTransaction.setRelatedAccount(mRs.getString(30));
                matchedTransaction.setOrderStatus(mRs.getString(31));
                matchedTransaction.setOrderStatusCode(mRs.getString(32));
                matchedTransaction.setCreatedUser(mRs.getString(33));
                matchedTransaction.setConfirmUser(mRs.getString(34));
                matchedTransaction.setInitTime(mRs.getTimestamp(35));
                matchedTransaction.setCreatedTime(mRs.getTimestamp(36));
                matchedTransaction.setFileName(mRs.getString(37));
                matchedTransaction.setLogDateTime(mRs.getTimestamp(38));
                matchedTransaction.setImportDateTime(mRs.getTimestamp(39));
                matchedTransaction.setBankReferenceData(mRs.getString(40));
                matchedTransaction.setProfileID(mRs.getString(41));
                matchedTransaction.setPartnerTransID(mRs.getString(42));
                matchedTransaction.setForcontrolCode(mRs.getString(43));
                matchedTransaction.setPartnerStatus(mRs.getString(44));
                matchedTransaction.setSumDate(mRs.getTimestamp(45));
                matchedTransaction.setAdjustDate(mRs.getTimestamp(46));
                matchedTransaction.setProcessStatus(mRs.getString(47));
                matchedTransaction.setUserProcess(mRs.getString(48));
                matchedTransaction.setConfirmStatus(mRs.getString(49));
                matchedTransaction.setUserConfirm(mRs.getString(50));
                matchedTransaction.setAccountID(mRs.getString(51));
                matchedTransaction.setUserType(mRs.getString(52));
                matchedTransaction.setEndTime(mRs.getTimestamp(53));
                matchedTransaction.setTransTime(mRs.getTimestamp(54));
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return matchedTransaction;
    }

    public void updateTicketName(MismatchedTransaction mismatchedTransaction) throws Exception {

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE mismatched_transaction SET ticket_name = ? " +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setString(1, mismatchedTransaction.getTicketName());
            mStmt.setString(2, mismatchedTransaction.getInvoiceOrderID());
            mStmt.execute();
//            logAfterUpdate(listChange);
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

    public void updateIncorrectFeeTransaction(IncorrectFeeTransaction incorrectFeeTransaction) throws Exception {
        System.out.println(incorrectFeeTransaction.getUserProcess());

        try {
            open();
            mConnection.setAutoCommit(false);

            //Update app
//            List listChange = logBeforeUpdate("mismatched_transaction", "rowid='" + mismatchedTransaction.getForcontrolCode() +
//                    "'");
            String strSQL = "UPDATE incorrect_fee_transaction SET adjust_date = ?, refund_status = ?,  " +
                    "refund_amount = ?, ticket_name = ?, confirm_status = ?, user_process = ?," +
                    "user_confirm = ? " +
                    "WHERE  invoice_order_id = ?";
            mStmt = mConnection.prepareStatement(strSQL);
            mStmt.setTimestamp(1, DateUtil.getSqlTimestamp(incorrectFeeTransaction.getAdjustDate()));
            mStmt.setString(2, incorrectFeeTransaction.getRefundStatus());
            mStmt.setInt(3, incorrectFeeTransaction.getRefundAmount());
            mStmt.setString(4, incorrectFeeTransaction.getTicketName());
            mStmt.setString(5, incorrectFeeTransaction.getConfirmStatus());
            mStmt.setString(6, incorrectFeeTransaction.getUserProcess());
            mStmt.setString(7, incorrectFeeTransaction.getUserConfirm());
            mStmt.setString(8, incorrectFeeTransaction.getInvoiceOrderID());


            mStmt.execute();
//            logAfterUpdate(listChange);
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

    public IncorrectFeeTransaction getIncorrectFeeTransaction(String invoiceOrderID) throws Exception {
        IncorrectFeeTransaction incorrectFeeTransaction = new IncorrectFeeTransaction();

        try {
            open();
            String sqlString = "SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    incorrect_fee_transaction\n" +
                    "WHERE\n" +
                    "    invoice_order_id = ? ";
            mStmt = mConnection.prepareStatement(sqlString);
            mStmt.setString(1, invoiceOrderID);
            mRs = mStmt.executeQuery();

            while (mRs.next()) {
                incorrectFeeTransaction.setInvoiceOrderID(mRs.getString(1));
                incorrectFeeTransaction.setTransactionID(mRs.getString(2));
                incorrectFeeTransaction.setSourceReceiptID(mRs.getString(3));
                incorrectFeeTransaction.setSaleOrderID(mRs.getString(4));
                incorrectFeeTransaction.setPayType(mRs.getString(5));
                incorrectFeeTransaction.setPayTypeName(mRs.getString(6));
                incorrectFeeTransaction.setPartnerCode(mRs.getString(7));
                incorrectFeeTransaction.setPartnerReferenceID(mRs.getString(8));
                incorrectFeeTransaction.setPartnerOrderID(mRs.getString(9));
                incorrectFeeTransaction.setPartnerInvoiceID(mRs.getString(10));
                incorrectFeeTransaction.setBankCode(mRs.getString(11));
                incorrectFeeTransaction.setBankReferenceID(mRs.getString(12));
                incorrectFeeTransaction.setProductServiceID(mRs.getString(13));
                incorrectFeeTransaction.setProductServiceCode(mRs.getString(14));
                incorrectFeeTransaction.setProductID(mRs.getString(15));
                incorrectFeeTransaction.setProductCode(mRs.getString(16));
                incorrectFeeTransaction.setProductName(mRs.getString(17));
                incorrectFeeTransaction.setProductDetail(mRs.getString(18));
                incorrectFeeTransaction.setDescription(mRs.getString(19));
                incorrectFeeTransaction.setProductAccount(mRs.getString(20));
                incorrectFeeTransaction.setAmount(mRs.getInt(21));
                incorrectFeeTransaction.setQuantity(mRs.getInt(22));
                incorrectFeeTransaction.setSaleDiscount(mRs.getString(23));
                incorrectFeeTransaction.setSaleFee(mRs.getString(24));
                incorrectFeeTransaction.setGrandAmount(mRs.getInt(25));
                incorrectFeeTransaction.setFee(mRs.getInt(26));
                incorrectFeeTransaction.setRelatedFee(mRs.getInt(27));
                incorrectFeeTransaction.setPaymentAmount(mRs.getInt(28));
                incorrectFeeTransaction.setAccountName(mRs.getString(29));
                incorrectFeeTransaction.setRelatedAccount(mRs.getString(30));
                incorrectFeeTransaction.setOrderStatus(mRs.getString(31));
                incorrectFeeTransaction.setOrderStatusCode(mRs.getString(32));
                incorrectFeeTransaction.setCreatedUser(mRs.getString(33));
                incorrectFeeTransaction.setConfirmUser(mRs.getString(34));
                incorrectFeeTransaction.setInitTime(mRs.getTimestamp(35));
                incorrectFeeTransaction.setCreatedTime(mRs.getTimestamp(36));
                incorrectFeeTransaction.setFileName(mRs.getString(37));
                incorrectFeeTransaction.setLogDateTime(mRs.getTimestamp(38));
                incorrectFeeTransaction.setImportDateTime(mRs.getTimestamp(39));
                incorrectFeeTransaction.setBankReferenceData(mRs.getString(40));
//                incorrectFeeTransaction.setSum(mRs.getString(41));
                incorrectFeeTransaction.setAdjustDate(mRs.getTimestamp(42));
                incorrectFeeTransaction.setRefundStatus(mRs.getString(43));
                incorrectFeeTransaction.setUserProcess(mRs.getString(44));
                incorrectFeeTransaction.setConfirmStatus(mRs.getString(45));
                incorrectFeeTransaction.setUserConfirm(mRs.getString(46));
                incorrectFeeTransaction.setAccountID(mRs.getString(47));
                incorrectFeeTransaction.setUserType(mRs.getString(48));
                incorrectFeeTransaction.setEndTime(mRs.getTimestamp(49));
                incorrectFeeTransaction.setTransTime(mRs.getTimestamp(50));
                incorrectFeeTransaction.setTicketName(mRs.getString(51));
                incorrectFeeTransaction.setIssuerBankCode(mRs.getString(52));
                incorrectFeeTransaction.setRefundAmount(mRs.getInt(53));
            }
        } catch (Exception ex) {
            throw ex;

        } finally {
            close(mRs);
            close(mStmt);
            close();
        }

        return incorrectFeeTransaction;
    }
}
