package com.neo.vas.controller;

import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.repository.SystemFunctionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApiLogController {
    @Autowired
    private SystemFunctionalRepository systemFunctionalRepository;

    @GetMapping("/api-log")
    @PreAuthorize("hasAnyAuthority('Tra cứu lịch sử API:Xem')")
    public ModelAndView searchApiLog(Model model) {
        ModelAndView modelAndView = new ModelAndView("ApiLog/api_log");
        model.addAttribute("sysFunctional",systemFunctionalRepository.getAllSF());
//        ModelAndView modelAndView = new ModelAndView("TransactionApi/ApiTransactionLog");
        modelAndView.addObject("page", "1");
        modelAndView.addObject("size", "5");
        return modelAndView;
    }
}
