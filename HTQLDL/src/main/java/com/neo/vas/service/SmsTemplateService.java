/**
 * 
 */
package com.neo.vas.service;

import java.security.Principal;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.SmsTemplate;

/**
 * @author KhanhBQ
 *
 */
public interface SmsTemplateService {
	public boolean saveNew(JSONObject data, Principal principal);

	public boolean editSms(JSONObject data, Principal principal);

	public boolean deleteSms(String smsCode, Principal principal);

	public Page<SmsTemplate> searchSmsTemplate(SmsTemplate smsTemplate, int page, int size);

	public SmsTemplate getById(String id);
}
