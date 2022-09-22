/**
 * 
 */
package com.neo.vas.service.impl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.SmsTemplate;
import com.neo.vas.repository.SmsTemplateRepository;
import com.neo.vas.service.SmsTemplateService;
import com.neo.vas.service.specification.SmsTemplateSpecification;

/**
 * @author KhanhBQ
 *
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

	@Autowired
	private SmsTemplateRepository smsTemplateRepository;

	Date date = new Date();

	@Override
	@Transactional
	public boolean saveNew(JSONObject data, Principal principal) {
		SmsTemplate smsTemplate = new SmsTemplate();
		smsTemplate.setUpdatedBy(principal.getName());
		smsTemplate.setUpdatedTime(new Date());
		try {
			if (data.has("shortCode")) {
				if (smsTemplateRepository.existsById(data.getString("shortCode"))) {
					return false;
				} else {
					smsTemplate.setTempCode(data.getString("shortCode"));
				}
			}
			if (data.has("status")) {
				if (data.getString("status").isEmpty()) {
					return false;
				} else {
					smsTemplate.setStatus(Integer.parseInt(data.getString("status")));
				}
			}
			if (data.has("startDate")) {
				if (data.getString("startDate").isEmpty()) {
					return false;
				} else {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate"));
					smsTemplate.setStartDate(start);
				}
			}
			if (data.has("endDate")) {
				if (data.getString("endDate").isEmpty()) {
					return false;
				} else {
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate"));
					smsTemplate.setEndDate(end);
				}
			}
			if (data.has("smsDescription")) {
				if (!data.getString("smsDescription").isEmpty()) {
					smsTemplate.setDescription(data.getString("smsDescription"));
				}
			}
			if (data.has("smsContent")) {
				if (!data.getString("smsContent").isEmpty()) {
					smsTemplate.setContent(data.getString("smsContent"));
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
		return smsTemplateRepository.saveAndFlush(smsTemplate) != null;
	}

	@Override
	@Transactional
	public boolean editSms(JSONObject data, Principal principal) {
		try {
			SmsTemplate smsTemplate = new SmsTemplate();
			smsTemplate.setUpdatedBy(principal.getName());
			smsTemplate.setUpdatedTime(new Date());
			if (data.has("tempCode")) {
				if (smsTemplateRepository.existsById(data.getString("tempCode"))) {
					smsTemplate = smsTemplateRepository.getOne(data.getString("tempCode"));
					smsTemplate.setTempCode(data.getString("tempCode"));
				} else {
					return false;
				}
			}
			if (data.has("status")) {
				if (data.getString("status").isEmpty()) {
					return false;
				} else {
					smsTemplate.setStatus(Integer.parseInt(data.getString("status").trim()));
				}
			}
			if (data.has("startDate")) {
				if (data.getString("startDate").isEmpty()) {
					return false;
				} else {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("startDate").trim());
					smsTemplate.setStartDate(start);
				}
			}
			if (data.has("endDate")) {
				if (data.getString("endDate").isEmpty()) {
					return false;
				} else {
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(data.getString("endDate").trim());
					smsTemplate.setEndDate(end);
				}
			}
			if (data.has("description") && !data.getString("description").isEmpty()) {
				smsTemplate.setDescription(data.getString("description").trim());
			}
			if (data.has("content") && !data.getString("content").isEmpty()) {
				smsTemplate.setContent(data.getString("content").trim());

			}
			smsTemplate.setUpdatedBy(principal.getName());
			smsTemplate.setUpdatedTime(new Date());
			return smsTemplateRepository.save(smsTemplate) != null;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteSms(String smsCode, Principal principal) {
		try {
			if (smsTemplateRepository.existsById(smsCode)) {
				smsTemplateRepository.deleteById(smsCode);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SmsTemplate> searchSmsTemplate(SmsTemplate smsTemplate, int page, int size) {
		Specification<SmsTemplate> conditions = Specification
				.where(SmsTemplateSpecification.hasSmsCode(smsTemplate.getTempCode()))
				.and((smsTemplate.getStatus() == -1) ? null
						: SmsTemplateSpecification.hasStatus(smsTemplate.getStatus()))
				.and((smsTemplate.getStartDate()) != null
						? SmsTemplateSpecification.hasStartDate(smsTemplate.getStartDate())
						: null)
				.and((smsTemplate.getEndDate()) != null ? SmsTemplateSpecification.hasEndDate(smsTemplate.getEndDate())
						: null);
		Page<SmsTemplate> data = smsTemplateRepository.findAll(conditions, PageRequest.of(page, size));
		return data;
	}

	@Override
	public SmsTemplate getById(String id) {
		Optional<SmsTemplate> optional = smsTemplateRepository.findById(id);
		SmsTemplate smsTemplate = new SmsTemplate();
		if (optional.isPresent()){
			smsTemplate = optional.get();
		}else {
			throw new RuntimeException("Khong tim thay " + id);
		}
		return smsTemplate;
	}

}
