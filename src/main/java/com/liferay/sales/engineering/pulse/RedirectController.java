package com.liferay.sales.engineering.pulse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
public class RedirectController {
    Logger logger = LoggerFactory.getLogger(CampaignController.class);

    @RequestMapping(value = "/redirect")
    public void redirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        logger.info(httpServletRequest.getQueryString());
        Collections.list(httpServletRequest.getHeaderNames()).forEach((headerName -> logger.info("{} : {}", headerName, httpServletRequest.getHeader(headerName))));
        httpServletResponse.setStatus(200);
    }
}
