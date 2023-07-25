package com.liferay.sales.engineering.pulse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping(value = {"/", "/url-tokens", "/interactions", "/campaign"})
    public String index() {
        return "index";
    }
}
