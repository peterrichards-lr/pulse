package com.liferay.sales.engineering.pulse.interfaces.rest;

import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/url-tokens")
public class UrlTokenController {
    private final UrlTokenRepository urlTokenRepository;

    UrlTokenController(final UrlTokenRepository urlTokenRepository) {
        this.urlTokenRepository = urlTokenRepository;
    }

    @GetMapping
    Page<UrlToken> getUrlTokens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "token") String sortBy) {
        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return urlTokenRepository.findAll(paging);
    }
}
