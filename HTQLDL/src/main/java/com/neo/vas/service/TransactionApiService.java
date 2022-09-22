package com.neo.vas.service;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.neo.vas.domain.ApiTransactionLog;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

/**
 * author: YNN
 */

public interface TransactionApiService {

	boolean callApi(JSONObject reqParamObj, Principal principal) throws IOException;
	Page<ApiTransactionLog> searchApiTransactionLog(String fromDate, String toDate,int realPage,int size);
	String callApiInvoice(JSONObject data, Principal principal);

}
