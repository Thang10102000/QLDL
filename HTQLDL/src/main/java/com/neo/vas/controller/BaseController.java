/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class BaseController {
	@GetMapping("/")
	public String index(Principal principal) {
		return principal != null ? "redirect:/dashboard" : "redirect:/login";
	}

	@GetMapping(value = { "/login" })
	public String login(@RequestParam(required = false) String message, final Model model, Principal principal) {
		if (message != null && !message.isEmpty()) {
			if (message.equals("timeout")) {
				model.addAttribute("message", "Phiên đăng nhập hết hạn");
			} else if (message.equals("max_session")) {
				model.addAttribute("message", "Phiên đăng hết hạn hoặc đang được đăng nhập ở nơi khác!");
			} else if (message.equals("logout")) {
				model.addAttribute("message", "Đăng xuất!");
			}
		}
		return principal != null ? "redirect:/dashboard" : "login";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "Error/error-403";
	}

	@GetMapping("/dashboard")
	public ModelAndView dashboard() {
		return new ModelAndView("index");
	}

}
