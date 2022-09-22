package vn.com.telsoft.entity;

import java.io.Serializable;
import java.util.Date;

public class SummarizeSucc implements Serializable {
	private String rowid;
	private String code;
	private String profile_id;
	private String status;
	private String approve_status;
	private Date sum_date;
	private Date created_time;
	private Date init_time;
	private String partner_trans_id;
	private String confirm_user;
	private String created_user;
	private String order_status_code;
	private String order_status;
	private String related_account;
	private String account_name;
	private String payment_amount;
	private String related_fee;
	private String fee;
	private String grand_amount;
	private String sale_fee;
	private String sale_discount;
	private int quantity;
	private int amount;
	private String product_account;
	private String description;
	private String product_detail;
	private String product_name;
	private String product_code;
	private String product_id;
	private String product_service_code;
	private String product_service_id;
	private String bank_reference_id;
	private String bank_code;
	private String partner_invoice_id;
	private String partner_order_id;
	private String partner_reference_id;
	private String partner_code;
	private String pay_type_name;
	private String pay_type;
	private String sale_order_id;
	private String source_receipt_id;
	private String transaction_id;
	private String invoice_order_id;
	private Date execute_datetime;

	public SummarizeSucc() {
	}

	public SummarizeSucc(String rowid, String code, String profile_id, String status, String approve_status,
			Date sum_date, Date created_time, Date init_time, String partner_status, String partner_trans_id,
			int partner_quantity, int partner_amount, String confirm_user, String created_user,
			String order_status_code, String order_status, String related_account, String account_name,
			String payment_amount, String related_fee, String fee, String grand_amount, String sale_fee,
			String sale_discount, int quantity, int amount, String product_account, String description,
			String product_detail, String product_name, String product_code, String product_id,
			String product_service_code, String product_service_id, String bank_reference_id, String bank_code,
			String partner_invoice_id, String partner_order_id, String partner_reference_id, String partner_code,
			String pay_type_name, String pay_type, String sale_order_id, String source_receipt_id,
			String transaction_id, String invoice_order_id) {

		this.rowid = rowid;
		this.code = code;
		this.profile_id = profile_id;
		this.status = status;
		this.approve_status = approve_status;
		this.sum_date = sum_date;
		this.created_time = created_time;
		this.init_time = init_time;
		this.partner_trans_id = partner_trans_id;
		this.confirm_user = confirm_user;
		this.created_user = created_user;
		this.order_status_code = order_status_code;
		this.order_status = order_status;
		this.related_account = related_account;
		this.account_name = account_name;
		this.payment_amount = payment_amount;
		this.related_fee = related_fee;
		this.fee = fee;
		this.grand_amount = grand_amount;
		this.sale_fee = sale_fee;
		this.sale_discount = sale_discount;
		this.quantity = quantity;
		this.amount = amount;
		this.product_account = product_account;
		this.description = description;
		this.product_detail = product_detail;
		this.product_name = product_name;
		this.product_code = product_code;
		this.product_id = product_id;
		this.product_service_code = product_service_code;
		this.product_service_id = product_service_id;
		this.bank_reference_id = bank_reference_id;
		this.bank_code = bank_code;
		this.partner_invoice_id = partner_invoice_id;
		this.partner_order_id = partner_order_id;
		this.partner_reference_id = partner_reference_id;
		this.partner_code = partner_code;
		this.pay_type_name = pay_type_name;
		this.pay_type = pay_type;
		this.sale_order_id = sale_order_id;
		this.source_receipt_id = source_receipt_id;
		this.transaction_id = transaction_id;
		this.invoice_order_id = invoice_order_id;
	}

	public SummarizeSucc(SummarizeSucc ett) {

		this.rowid = ett.rowid;
		this.code = ett.code;
		this.profile_id = ett.profile_id;
		this.status = ett.status;
		this.approve_status = ett.approve_status;
		this.sum_date = ett.sum_date;
		this.created_time = ett.created_time;
		this.init_time = ett.init_time;
		this.partner_trans_id = ett.partner_trans_id;
		this.confirm_user = ett.confirm_user;
		this.created_user = ett.created_user;
		this.order_status_code = ett.order_status_code;
		this.order_status = ett.order_status;
		this.related_account = ett.related_account;
		this.account_name = ett.account_name;
		this.payment_amount = ett.payment_amount;
		this.related_fee = ett.related_fee;
		this.fee = ett.fee;
		this.grand_amount = ett.grand_amount;
		this.sale_fee = ett.sale_fee;
		this.sale_discount = ett.sale_discount;
		this.quantity = ett.quantity;
		this.amount = ett.amount;
		this.product_account = ett.product_account;
		this.description = ett.description;
		this.product_detail = ett.product_detail;
		this.product_name = ett.product_name;
		this.product_code = ett.product_code;
		this.product_id = ett.product_id;
		this.product_service_code = ett.product_service_code;
		this.product_service_id = ett.product_service_id;
		this.bank_reference_id = ett.bank_reference_id;
		this.bank_code = ett.bank_code;
		this.partner_invoice_id = ett.partner_invoice_id;
		this.partner_order_id = ett.partner_order_id;
		this.partner_reference_id = ett.partner_reference_id;
		this.partner_code = ett.partner_code;
		this.pay_type_name = ett.pay_type_name;
		this.pay_type = ett.pay_type;
		this.sale_order_id = ett.sale_order_id;
		this.source_receipt_id = ett.source_receipt_id;
		this.transaction_id = ett.transaction_id;
		this.invoice_order_id = ett.invoice_order_id;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProfile_id() {
		return this.profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprove_status() {
		return this.approve_status;
	}

	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}

	public Date getSum_date() {
		return this.sum_date;
	}

	public void setSum_date(Date sum_date) {
		this.sum_date = sum_date;
	}

	public Date getCreated_time() {
		return this.created_time;
	}

	public void setCreated_time(Date created_time) {
		this.created_time = created_time;
	}

	public Date getInit_time() {
		return this.init_time;
	}

	public void setInit_time(Date init_time) {
		this.init_time = init_time;
	}

	public String getPartner_trans_id() {
		return this.partner_trans_id;
	}

	public void setPartner_trans_id(String partner_trans_id) {
		this.partner_trans_id = partner_trans_id;
	}

	public String getConfirm_user() {
		return this.confirm_user;
	}

	public void setConfirm_user(String confirm_user) {
		this.confirm_user = confirm_user;
	}

	public String getCreated_user() {
		return this.created_user;
	}

	public void setCreated_user(String created_user) {
		this.created_user = created_user;
	}

	public String getOrder_status_code() {
		return this.order_status_code;
	}

	public void setOrder_status_code(String order_status_code) {
		this.order_status_code = order_status_code;
	}

	public String getOrder_status() {
		return this.order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getRelated_account() {
		return this.related_account;
	}

	public void setRelated_account(String related_account) {
		this.related_account = related_account;
	}

	public String getAccount_name() {
		return this.account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getPayment_amount() {
		return this.payment_amount;
	}

	public void setPayment_amount(String payment_amount) {
		this.payment_amount = payment_amount;
	}

	public String getRelated_fee() {
		return this.related_fee;
	}

	public void setRelated_fee(String related_fee) {
		this.related_fee = related_fee;
	}

	public String getFee() {
		return this.fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getGrand_amount() {
		return this.grand_amount;
	}

	public void setGrand_amount(String grand_amount) {
		this.grand_amount = grand_amount;
	}

	public String getSale_fee() {
		return this.sale_fee;
	}

	public void setSale_fee(String sale_fee) {
		this.sale_fee = sale_fee;
	}

	public String getSale_discount() {
		return this.sale_discount;
	}

	public void setSale_discount(String sale_discount) {
		this.sale_discount = sale_discount;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getProduct_account() {
		return this.product_account;
	}

	public void setProduct_account(String product_account) {
		this.product_account = product_account;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProduct_detail() {
		return this.product_detail;
	}

	public void setProduct_detail(String product_detail) {
		this.product_detail = product_detail;
	}

	public String getProduct_name() {
		return this.product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_code() {
		return this.product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getProduct_id() {
		return this.product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_service_code() {
		return this.product_service_code;
	}

	public void setProduct_service_code(String product_service_code) {
		this.product_service_code = product_service_code;
	}

	public String getProduct_service_id() {
		return this.product_service_id;
	}

	public void setProduct_service_id(String product_service_id) {
		this.product_service_id = product_service_id;
	}

	public String getBank_reference_id() {
		return this.bank_reference_id;
	}

	public void setBank_reference_id(String bank_reference_id) {
		this.bank_reference_id = bank_reference_id;
	}

	public String getBank_code() {
		return this.bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getPartner_invoice_id() {
		return this.partner_invoice_id;
	}

	public void setPartner_invoice_id(String partner_invoice_id) {
		this.partner_invoice_id = partner_invoice_id;
	}

	public String getPartner_order_id() {
		return this.partner_order_id;
	}

	public void setPartner_order_id(String partner_order_id) {
		this.partner_order_id = partner_order_id;
	}

	public String getPartner_reference_id() {
		return this.partner_reference_id;
	}

	public void setPartner_reference_id(String partner_reference_id) {
		this.partner_reference_id = partner_reference_id;
	}

	public String getPartner_code() {
		return this.partner_code;
	}

	public void setPartner_code(String partner_code) {
		this.partner_code = partner_code;
	}

	public String getPay_type_name() {
		return this.pay_type_name;
	}

	public void setPay_type_name(String pay_type_name) {
		this.pay_type_name = pay_type_name;
	}

	public String getPay_type() {
		return this.pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getSale_order_id() {
		return this.sale_order_id;
	}

	public void setSale_order_id(String sale_order_id) {
		this.sale_order_id = sale_order_id;
	}

	public String getSource_receipt_id() {
		return this.source_receipt_id;
	}

	public void setSource_receipt_id(String source_receipt_id) {
		this.source_receipt_id = source_receipt_id;
	}

	public String getTransaction_id() {
		return this.transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getInvoice_order_id() {
		return this.invoice_order_id;
	}

	public void setInvoice_order_id(String invoice_order_id) {
		this.invoice_order_id = invoice_order_id;
	}

	public Date getExecute_datetime() {
		return execute_datetime;
	}

	public void setExecute_datetime(Date execute_datetime) {
		this.execute_datetime = execute_datetime;
	}
}
