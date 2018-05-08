package com.msmp.platform.gateway.controller;

import com.msmp.platform.common.result.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 **/
@RestController
public class ErrorHandlerController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseData error(HttpServletRequest request) {
        return ResponseData.fail(getErrorAttributes(request),  HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private String getErrorAttributes(HttpServletRequest request) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable error = errorAttributes.getError(requestAttributes);
        return error.getMessage();
    }
}
