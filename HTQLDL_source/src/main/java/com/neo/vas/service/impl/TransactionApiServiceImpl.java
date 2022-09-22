package com.neo.vas.service.impl;

import com.neo.vas.domain.ApiTransactionLog;
import com.neo.vas.repository.ServiceRequestRepository;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.repository.TransactionApiRepository;
import com.neo.vas.service.TransactionApiService;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransactionApiServiceImpl implements TransactionApiService {
	public static final int STATUS_OK = 1;
	public static final int STATUS_NOK = 0;
	public static final String URL_API_PRODUCT = "http://10.3.7.172:8130/SMAPI/bhtt/create-sale-transaction";
	public static final String URL_API_UAT = "http://10.3.7.185:8160/SMAPI/bhtt/create-sale-transaction";
	public static final String username = "mds";
	public static final String password = "mds#2021";
	@Autowired
	private TransactionApiRepository taRepository;
	@Autowired
	private SystemFunctionalRepository systemFunctional;
	@Autowired
	private ServiceRequestRepository srRepository;

	@Override
	public boolean callApi(JSONObject data, Principal principal) throws IOException {
		try {
			Date created = new Date();
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			String bodyRaw = "{ "
					+ "    \"saleTransDate\": \"" + data.getString("saleTransDate") + "\", "
					+ "    \"saleTransType\": " + data.getString("saleTransType") + ", "
					+ "    \"shopCode\": \"" + data.getString("shopCode") + "\", "
					+ "    \"staffCode\": \"" + data.getString("staffCode") + "\", "
					+ "    \"customerName\": \"" + data.getString("customerName") + "\", "
					+ "    \"telNumber\": \"" + data.getString("telNumber") + "\", "
					+ "    \"email\": \"" + data.getString("email") + "\", "
					+ "    \"company\": \"" + data.getString("company") + "\", "
					+ "    \"address\": \"" + data.getString("address") + "\", "
					+ "    \"tin\": \"" + data.getString("tin") + "\", "
					+ "    \"note\": \"" + data.getString("note") + "\", "
					+ "    \"amount\": " + Long.parseLong(data.getString("amount")) + ", "
					+ "    \"vat\": " + data.getString("vat") + ", "
					+ "    \"discount\": " + Long.parseLong(data.getString("discount")) + ", "
					+ "    \"reasonCode\": \"" + data.getString("reasonCode") + "\", "
					+ "    \"payMethod\": \"" + data.getString("payMethod") + "\", "
					+ "    \"ltsDetail\": [ "
					+ "        { "
					+ "            \"goodCode\": \"" + data.getString("goodCode1") + "\", "
					+ "            \"quantity\": " + data.getString("quantity1") + ", "
					+ "            \"price\": " + Long.parseLong(data.getString("price1")) + " "
					+ "        },  "
					+ "        { "
					+ "            \"goodCode\": \"" + data.getString("goodCode2") + "\", "
					+ "            \"quantity\": " + data.getString("quantity2") + ", "
					+ "            \"price\": " + Long.parseLong(data.getString("price2")) + " "
					+ "        } "
					+ "    ] "
					+ "} ";
			RequestBody body = RequestBody.create(mediaType, bodyRaw);
			String credential = Credentials.basic(username, password);
			Request request = new Request.Builder().url(URL_API_PRODUCT)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", credential)
					.method("POST", body).build();
			Response response = client.newCall(request).execute();
			String res = response.body().string();
			if (res.length() > 1024)
				res = res.substring(0, 1024);
			if (response.code() == 200) {
				ApiTransactionLog atl = new ApiTransactionLog();
				atl.setRequest(bodyRaw);
				atl.setResponse(res);
				atl.setUrl(URL_API_PRODUCT);
				atl.setCreated(created);
				atl.setCreator(principal.getName());
				atl.setResponseTime(new Date());
				taRepository.saveAndFlush(atl);
				return true;
			} else {
				ApiTransactionLog atl = new ApiTransactionLog();
				atl.setRequest(body.toString());
				atl.setResponse(res);
				atl.setUrl(URL_API_PRODUCT);
				atl.setCreated(created);
				atl.setCreator(principal.getName());
				atl.setResponseTime(new Date());
				taRepository.saveAndFlush(atl);
				return false;
			}
		} catch (Exception ex) {
			String response;
			if (ex.getCause() != null)
				response = ex.getCause().getMessage();
			else
				response = ex.getLocalizedMessage();
			if (response.length() > 1024)
				response = response.substring(0, 1024);
			return false;
		}
	}


	@Override
	public Page<ApiTransactionLog> searchApiTransactionLog(String fromDate, String toDate, int realPage, int size) {
		try {
			Pageable pageable = PageRequest.of(realPage, size);
			if (!fromDate.isEmpty() && !toDate.isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(fromDate);
				Date endDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(toDate);
				return taRepository.findAllLogApiFullDate(startDate, endDate, pageable);
			}
			if (!fromDate.isEmpty() && toDate.isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(fromDate);
				return taRepository.findAllLogApiFromDate(startDate, pageable);
			}
			if (fromDate.isEmpty() && !toDate.isEmpty()) {
				Date endDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(toDate);
				return taRepository.findAllLogApiToDate(endDate, pageable);
			}

			return taRepository.findAllNoDate(pageable);
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@Override
	public String callApiInvoice(JSONObject data, Principal principal) {
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			String bodyRaw = "{ "
					+ "    \"saleTransDate\": \"" + data.getString("saleTransDate") + "\", "
					+ "    \"saleTransType\": " + data.getString("saleTransType") + ", "
					+ "    \"shopCode\": \"" + data.getString("shopCode") + "\", "
					+ "    \"staffCode\": \"" + data.getString("staffCode") + "\", "
					+ "    \"customerName\": \"" + data.getString("customerName") + "\", "
					+ "    \"telNumber\": \"" + data.getString("telNumber") + "\", "
					+ "    \"email\": \"" + data.getString("email") + "\", "
					+ "    \"company\": \"" + data.getString("company") + "\", "
					+ "    \"address\": \"" + data.getString("address") + "\", "
					+ "    \"tin\": \"" + data.getString("tin") + "\", "
					+ "    \"note\": \"" + data.getString("note") + "\", "
					+ "    \"amount\": " + Long.parseLong(data.getString("amount")) + ", "
					+ "    \"vat\": " + data.getString("vat") + ", "
					+ "    \"discount\": " + Long.parseLong(data.getString("discount")) + ", "
					+ "    \"reasonCode\": \"" + data.getString("reasonCode") + "\", "
					+ "    \"payMethod\": \"" + data.getString("payMethod") + "\", "
					+ "    \"ltsDetail\": [ ";

			for (int i = 1; i < data.getInt("sizeArr") + 1; i++) {
				if (i < data.getInt("sizeArr")) {
					bodyRaw += "        { "
							+ "            \"goodCode\": \"" + data.getString("goodCode" + i) + "\", "
							+ "            \"quantity\": " + data.getString("quantity" + i) + ", "
							+ "            \"price\": " + Long.parseLong(data.getString("price" + i)) + " "
							+ "        }, ";
				} else {
					bodyRaw += "        { "
							+ "            \"goodCode\": \"" + data.getString("goodCode" + i) + "\", "
							+ "            \"quantity\": " + data.getString("quantity" + i) + ", "
							+ "            \"price\": " + Long.parseLong(data.getString("price" + i)) + " "
							+ "        } ";
				}
			}
			bodyRaw += "    ] "
					+ "} ";
			RequestBody body = RequestBody.create(mediaType, bodyRaw);
			String credential = Credentials.basic(username, password);
			Request request = new Request.Builder().url(URL_API_PRODUCT)
					.addHeader("Content-Type", "application/json")
					.addHeader("Authorization", credential)
					.method("POST", body).build();
			Response response = client.newCall(request).execute();
			String res = response.body().string();
			Long referenceId = null;
			if (!data.getString("orderId").isEmpty()) {
				referenceId = data.getLong("orderId");
			}
			if (response.code() == 200) {
				JSONObject jsonObject = new JSONObject(res);
				if(jsonObject.getString("code").equals("API000")) {
					srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/agency-order").getSfId(), bodyRaw,
							res, principal.getName(), URL_API_PRODUCT, referenceId, STATUS_OK);

					return "STATUS200";
				}
				else {
					srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/agency-order").getSfId(), bodyRaw,
							res, principal.getName(), URL_API_PRODUCT, referenceId, STATUS_NOK);
					return jsonObject.getString("message");
				}
			} else if (response.code() == 400) {
				srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/agency-order").getSfId(), bodyRaw,
						"HTTP Status 400 – Bad Request", principal.getName(), URL_API_PRODUCT, referenceId, STATUS_NOK);
				return "STATUS400";
			} else {
				srRepository.createApiLog(systemFunctional.getMenuCallApi("/vasonline/agency-order").getSfId(), bodyRaw,
						res, principal.getName(), URL_API_PRODUCT, referenceId, STATUS_NOK);
				return "FALSE";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			String response;
			if (ex.getCause() != null)
				response = ex.getCause().getMessage();
			else
				response = ex.getLocalizedMessage();
			if (response.length() > 1024)
				response = response.substring(0, 1024);
			return "FALSE";
		}
	}

}
