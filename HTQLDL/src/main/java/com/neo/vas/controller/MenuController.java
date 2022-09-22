/**
 * 
 */
package com.neo.vas.controller;

import com.neo.vas.service.LevelsService;
import com.neo.vas.service.LoadMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.neo.vas.service.impl.LevelsServiceImpl;
import com.neo.vas.service.impl.LoadMenuServiceImpl;

@ControllerAdvice
public class MenuController {
	@Autowired
	private LoadMenuService lmsService;

	@Autowired
	private LevelsService levelsService;

	@ModelAttribute
	public void addMenuAttribute(Model model) {
		model.addAttribute("myMenu", lmsService.loadMenuByUser());
	}

	@ModelAttribute
	public void addLevelAttribute(Model model) {
		model.addAttribute("levels", levelsService.getAllLevels());
	}
	
	@ModelAttribute
	public void addLevelNotInAmkam(Model model) {
		model.addAttribute("levelsNotInAmkam", levelsService.getLevelsNotInAmkam());
	}

}
