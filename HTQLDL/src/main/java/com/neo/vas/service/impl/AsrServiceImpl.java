package com.neo.vas.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.AgencyServiceRequest;
import com.neo.vas.repository.*;
import com.neo.vas.service.AsrService;
import com.neo.vas.service.specification.AsrSpecification;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author: YNN
 */
@Service
public class AsrServiceImpl implements AsrService {
	@Autowired
	private AsrRepository asrRepository;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private BrandRepository brandRepository;

	@Override
	public Page<AgencyServiceRequest> getAllAsr() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		return asrRepository.findAll((Pageable) pageRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AgencyServiceRequest> searchAsrs(String agency, String packages, String asr_id, String status,
			Date createFrom, Date createTo, Date updateFrom, Date updateTo, int page, int pageSize) {
		Specification<AgencyServiceRequest> conditions = Specification
				.where(!asr_id.isEmpty() && asr_id != null ? AsrSpecification.hasRequestId(Long.parseLong(asr_id))
						: null)
				.and(!status.isEmpty() && status != null ? AsrSpecification.hasStatus(Integer.valueOf(status)) : null)
				.and(!packages.isEmpty() && !packages.equals("null")
						? AsrSpecification.hasPackage(brandRepository.getOne(Long.parseLong(packages)))
						: null)
				.and(!agency.isEmpty() && !agency.equals("null")
						? AsrSpecification.hasAgency(agencyRepository.getOne(Long.parseLong(agency)))
						: null)
				.and(createFrom != null ? AsrSpecification.hasCreatedDateFrom(createFrom) : null)
				.and(createTo != null ? AsrSpecification.hasCreatedDateTo(createTo) : null)
				.and(updateFrom != null ? AsrSpecification.hasUpdatedDateFrom(updateFrom) : null)
				.and(updateTo != null ? AsrSpecification.hasUpdatedDateTo(updateTo) : null);
		Page<AgencyServiceRequest> pageAsr = asrRepository.findAll(conditions, PageRequest.of(page, pageSize, Sort.by("createdDate").descending()));
		return pageAsr;

	}

	@Override
	@Transactional
	public String saveNewAsr(JSONObject data, Principal principal) {
		String result = null;
		try {
			result = asrRepository.createRequest(principal.getName(), data.getString("description"),
					Long.parseLong(data.getString("pkg")), data.getString("account_customer"),
					data.getString("total_value"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	@Transactional
	public String EditAsr(JSONObject data, Principal principal) {
		String result = null;
		try {
			result = asrRepository.editRequest(principal.getName(), Long.parseLong(data.getString("requestId")),
					data.getString("description"), Long.parseLong(data.getString("brandASR.id")),
					data.getString("customerAccount"), data.getString("totalValue"));
		} catch (Exception e) {
			System.err.println(e);
		}
		return result;
	}

	@Override
	@Transactional
	public boolean deleteAsr(long requestId) {
		try {
			asrRepository.deleteById(requestId);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean acceptAsr(long requestId, Principal principal) {
		try {
			AgencyServiceRequest newAsr = getAsrById(requestId);
			String typeAsr = asrRepository.acceptAsr(principal.getName(), newAsr.getRequestId(), newAsr.getTotalValue(),
					newAsr.getAgencyASR().getId(), newAsr.getBrandASR().getId());
			if (typeAsr.equals("FALSE"))
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean approveAsr(long requestId, Principal principal) {
		try {
			AgencyServiceRequest newAsr = getAsrById(requestId);
			String typeAsr = asrRepository.getTypeAsr(principal.getName(), newAsr.getRequestId(),
					newAsr.getTotalValue(), newAsr.getAgencyASR().getId(), newAsr.getBrandASR().getId());
			if (typeAsr.equals("FALSE"))
				return false;
			else if (typeAsr.equals("AMKAM")) {
				if (registerService(newAsr)) {
					asrRepository.updateAsr(principal.getName(), 5L, "", newAsr.getRequestId());
					return true;
				} else {
					asrRepository.updateAsr(principal.getName(), 3L, "Lỗi hệ thống", newAsr.getRequestId());
					return false;
				}
			} else if (typeAsr.equals("POSTPAID")) {
				if (registerService(newAsr)) {
					asrRepository.updateAsr(principal.getName(), 5L, "", newAsr.getRequestId());
					return true;
				} else {
					asrRepository.updateAsr(principal.getName(), 3L, "Lỗi hệ thống", newAsr.getRequestId());
					return false;
				}
			} else if (typeAsr.equals("TRUE")) {
				if (registerService(newAsr)) {
					asrRepository.updateAsr(principal.getName(), 5L, "", newAsr.getRequestId());
					return true;
				} else {
					asrRepository.updateAsr(principal.getName(), 3L, "Lỗi hệ thống", newAsr.getRequestId());
					return false;
				}
			} else {
				if (registerService(newAsr)) {
					try {
						asrRepository.chargeOrder(principal.getName(), newAsr.getTotalValue(), Long.parseLong(typeAsr),
								newAsr.getRequestId());
						asrRepository.updateAsr(principal.getName(), 5L, "", newAsr.getRequestId());
						try {
							asrRepository.insertSms(Long.parseLong(typeAsr), newAsr.getAgencyASR().getId(),
									newAsr.getRequestId(), newAsr.getTotalValue());
						} catch (Exception ex) {
							System.out.println(
									"Insert SMS for Agency_service_request: " + newAsr.getRequestId() + " error: ");
							ex.printStackTrace();
							return true;
						}
						return true;
					} catch (Exception e) {
						asrRepository.updateAsr(principal.getName(), 4L, "Trừ cước lỗi", newAsr.getRequestId());
						e.printStackTrace();
						return false;
					}
				} else {
					asrRepository.updateAsr(principal.getName(), 3L, "Lỗi hệ thống", newAsr.getRequestId());
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static Date date = new Date();

	public boolean registerService(AgencyServiceRequest agency) throws IOException {
		try {
//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date);
//			cal.add(Calendar.DAY_OF_MONTH, agency.getBrandASR().getActiveDay());
//			Date dateActive = cal.getTime();
//			OkHttpClient client = new OkHttpClient().newBuilder().build();
//			MediaType mediaType = MediaType.parse("text/plain");
//			RequestBody body = RequestBody.create(mediaType, "");
//			Request request = new Request.Builder().url(
//					"http://10.54.20.79:8888/api/registerPackage?isdn=" + agency.getCustomerAccount() + "&packageCode="
//							+ agency.getBrandASR().getBrandId() + "&agencyCode=" + agency.getAgencyASR().getAgencyCode()
//							+ "" + "&regDatetime=" + dateFormat.format(date) + "&staDatetime=" + dateFormat.format(date)
//							+ "&endDatetime=" + dateFormat.format(dateActive) + "&charge_price="
//							+ agency.getBrandASR().getPrice() + "&username=kammobiedu&password=kammobiedu@321")
//					.method("POST", body).build();
//			Response response = client.newCall(request).execute();
//			String res = response.body().string();
//			if (res.length() > 1024) {
//				res = res.substring(0, 1024);
//				System.out.println("Api register service ASR_ID-" + agency.getRequestId() + ": " + res);
//			} else
//				System.out.println("Api register service ASR_ID-" + agency.getRequestId() + ": " + res);
//			if (response.code() == 200) {
//				asrRepository.createEventLog("registerService ID: " + agency.getRequestId(),
//						"Response Code: " + response.code());
//				return true;
//			} else {
//				asrRepository.createEventLog("registerService ID: " + agency.getRequestId(),
//						"Response Code: " + response.code());
//				return false;
//			}
			return true;
		} catch (Exception ex) {
			String response;
			if (ex.getCause() != null)
				response = ex.getCause().getMessage();
			else
				response = ex.getLocalizedMessage();
			if (response.length() > 1024)
				response = response.substring(0, 1024);
			System.out.println("Error call api register service ASR_ID-" + agency.getRequestId() + ": " + response);
			return false;
		}
	}

	@Override
	public boolean rechargeAsr(long requestId, Principal principal) {
		try {
			AgencyServiceRequest newAsr = getAsrById(requestId);
			String orderId = asrRepository.processCharge(principal.getName(), newAsr.getRequestId(),
					newAsr.getTotalValue(), newAsr.getAgencyASR().getId(), newAsr.getBrandASR().getId());
			if (orderId.equals("FALSE"))
				return false;
			else {
				try {
					asrRepository.insertSms(Long.parseLong(orderId), newAsr.getAgencyASR().getId(),
							newAsr.getRequestId(), newAsr.getTotalValue());
				} catch (Exception ex) {
					System.out.println("Insert SMS for Agency_service_request: " + newAsr.getRequestId() + " error: ");
					ex.printStackTrace();
					return true;
				}
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	@Override
	public AgencyServiceRequest getAsrById(Long id) {
		Optional<AgencyServiceRequest> optAsr = asrRepository.findById(id);
		AgencyServiceRequest asr = null;
		if (optAsr.isPresent()) {
			asr = optAsr.get();
		} else {
			throw new RuntimeException("AgencyServiceRequest " + id);
		}
		return asr;
	}

	@Override
	public Long getTotalValue(Long brandId, String userName) {
		Long status = null;
		try {
			status = asrRepository.getTotalValue(userName, brandId);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
}
