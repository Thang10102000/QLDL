package com.neo.vas.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class CustomErrorController implements ErrorController {
	@GetMapping(value = "/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				System.err.println(request + ": Return 404");
				return "Error/error-404";
			} else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
				System.err.println(request + ":Return 400");
				return "Error/error-400";
			} else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
				System.err.println(request + ":Return 405");
				return "Error/error-405";
			} else if (statusCode == HttpStatus.I_AM_A_TEAPOT.value()) {
				System.err.println(request + ":Return 418");
				return "Error/error-418";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				System.err.println(request + ":Return 500");
				return "Error/error-500";
			}
		}
		return "Error/error";
	}

	@Override
	public String getErrorPath() {
		return null;
	}
}
