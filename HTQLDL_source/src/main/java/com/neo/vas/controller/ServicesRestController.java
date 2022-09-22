package com.neo.vas.controller;

import java.security.Principal;
import java.util.*;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.repository.BrandGroupRepository;
import com.neo.vas.repository.BrandRepository;
import com.neo.vas.service.BrandGroupService;
import com.neo.vas.service.BrandService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.ServiceInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.vas.domain.Brand;
import com.neo.vas.domain.BrandGroup;
import com.neo.vas.domain.Services;
import com.neo.vas.service.impl.BrandServiceImpl;

/**
 * project_name: vasonline2021 Created_by: thulv time: 02/06/2021
 */
@RestController
public class ServicesRestController {
	@Autowired
	private BrandGroupService brandGroupService;
	@Autowired
	private BrandGroupRepository brandRepository;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private BrandService brandService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private BrandRepository bra;

	@GetMapping("/service-search")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Xem')")
	public Map<JSONObject, Integer> searchServices(@RequestParam String serviceId, String serviceName, String shortCode,
												   String status, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
												   @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		Map<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/service", ConstantLog.SEARCH,
					principal.getName()+" search service");
			Services services = new Services();
			services.setServiceId(serviceId);
			services.setServiceName(serviceName);
			services.setShortCode(shortCode);
			if (!status.isEmpty()) {
				services.setStatus(Integer.parseInt(status));
			} else {
				services.setStatus(100);
			}

			Page<Services> pageService = serviceInterface.searchService(services, realPage, size );
			for (Services se : pageService) {
				JSONObject dt = se.createJson();
				data.put(dt, pageService.getTotalPages());
			}
			System.err.println(data);
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}

	}

	@GetMapping("/brand-group-search")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý nhóm gói cước:Xem')")
	public Map<JSONObject, Integer> searchBrandGroup(@RequestParam String groupName, String serviceBG, String status,
			String description, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		LinkedHashMap<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/brand-group", ConstantLog.SEARCH,
					principal.getName()+" search band group");
			BrandGroup brandGroup = new BrandGroup();
			brandGroup.setGroupName(groupName);
			if (!serviceBG.isEmpty()) {
				brandGroup.setServicesBG(serviceInterface.getServicesById(Long.parseLong(serviceBG)));
			}
			if (!status.isEmpty()) {
				brandGroup.setStatus(Integer.parseInt(status));
			} else {
				brandGroup.setStatus(100);
			}

			brandGroup.setDescription(description);

			Page<BrandGroup> pageBG = brandGroupService.serachBrandGroup(brandGroup, realPage, size);
			for (BrandGroup brg : pageBG) {
				JSONObject dt = brg.createJson();
				data.put(dt, pageBG.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/brand-search")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý gói cước:Xem')")
	public Map<JSONObject, Integer> searchBrand(@RequestParam String brandId, String brandName, String status,
			String brandType,String brandGroupBId, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		Map<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			if (!brandId.isEmpty())
			{
				brandId = brandId.toLowerCase().trim();
			}
			if (!brandName.isEmpty())
			{
				brandName = brandName.toLowerCase().trim();
			}
			if (!status.isEmpty())
			{
				status = status.toLowerCase().trim();
			}
			if (!brandType.isEmpty())
			{
				brandType = brandType.toLowerCase().trim();
			}
			if (!brandGroupBId.isEmpty())
			{
				brandGroupBId = brandGroupBId.toLowerCase().trim();
			}

			insertLogService.insertLog(principal.getName(),"/vasonline/brand", ConstantLog.SEARCH,
					principal.getName()+" search brand");
			Brand brand = new Brand();
			brand.setBrandId(brandId);
			brand.setBrandName(brandName);
			if (!brandGroupBId.isEmpty()){
				brand.setBrandGroupB(brandRepository.findAllById(Long.parseLong(brandGroupBId)));
			}else {
				brand.setBrandGroupB(null);
			}
			if (!status.isEmpty()) {
				brand.setStatus(Integer.parseInt(status));
			} else {
				brand.setStatus(100);
			}
			if (!brandType.isEmpty()) {
				brand.setBrandType(Integer.parseInt(brandType));
			} else {
				brand.setBrandType(100);
			}
			Page<Brand> pageBrand = brandService.searchBrand(brand, realPage, size);
			for (Brand br : pageBrand) {
				JSONObject dt = br.createJson();
				data.put(dt, pageBrand.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
//	@GetMapping("/api/brand-search-by-bgname")
//	@PreAuthorize(value = "hasAnyAuthority('Quản lý nhóm gói cước:Xem')")
//	public Map<JSONObject, Integer> searchBrandByBrandGroupNam(@RequestParam String groupName) {
//
//		LinkedHashMap<JSONObject, Integer> data = new LinkedHashMap<>();
//		try {
//
//
//			Page<Brand> pageBG = bra.findAllByGroupName(groupName);
//			for (Brand brg : pageBG) {
//				JSONObject dt = brg.createJson();
//				data.put(dt, pageBG.getTotalPages());
//			}
//			return data;
//		} catch (Exception e) {
//			System.err.println(e);
//			return null;
//		}
//	}

}
