package com.liferay.sales.engineering.pulse;

import com.google.common.net.InternetDomainName;
import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Interaction;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.InteractionRepository;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import com.liferay.sales.engineering.pulse.util.HttpRequestResponseUtils;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public final class RedirectController {
    private final InteractionRepository interactionRepository;
    private final Logger logger = LoggerFactory.getLogger(RedirectController.class);
    private final UrlTokenRepository tokenRepository;
    @Value("${cookie.domain}")
    private String cookieDomain;
    @Value("${server.host}")
    private String serverHost;
    @Value("${server.port}")
    private String serverPort;
    @Value("${server.scheme}")
    private String serverScheme;

    @Autowired
    public RedirectController(final UrlTokenRepository tokenRepository,
                              final InteractionRepository interactionRepository) {
        this.tokenRepository = tokenRepository;
        this.interactionRepository = interactionRepository;
    }

    private void addCookies(final Map<String, String> cookieMap,
                            final InternetDomainName hostDomainName,
                            final HttpServletResponse httpServletResponse) {
        cookieMap.keySet().stream().map((key) -> {
            final String value = cookieMap.get(key);
            final Cookie cookie = new Cookie(key, value);
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
            if (!StringUtils.isBlank(cookieDomain)) {
                cookie.setDomain(cookieDomain);
            } else if (hostDomainName.isUnderRegistrySuffix() && StringUtils.isNotBlank(hostDomainName.publicSuffix().toString())) {
                cookie.setDomain(hostDomainName.publicSuffix().toString());
            }
            return cookie;
        }).forEach(httpServletResponse::addCookie);
    }

    private URL buildUrl(final String campaignUrl,
                         final Acquisition acquisition) throws MalformedURLException {
        final String baseUrl;
        if (campaignUrl.startsWith("/")) {
            logger.info("Default server scheme : {}", serverScheme);
            logger.info("Default server serverHost : {}", serverHost);
            logger.info("Default server serverPort : {}", serverPort);
            baseUrl = serverScheme + "://" + serverHost + (StringUtils.isNotBlank(serverPort) ? ":" + serverPort : "") + campaignUrl;
        } else {
            baseUrl = campaignUrl;
        }
        logger.info("baseUrl : {}", baseUrl);
        if (acquisition == null) return new URL(baseUrl);

        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        if (StringUtils.isNotBlank(acquisition.getCampaign())) {
            url.append("utm_campaign=");
            url.append(acquisition.getCampaign());
        }
        if (StringUtils.isNotBlank(acquisition.getContent())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_content=");
            url.append(acquisition.getContent());
        }
        if (StringUtils.isNotBlank(acquisition.getMedium())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_medium=");
            url.append(acquisition.getMedium());
        }
        if (StringUtils.isNotBlank(acquisition.getSource())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_source=");
            url.append(acquisition.getSource());
        }
        if (StringUtils.isNotBlank(acquisition.getTerm())) {
            if (url.length() > 1) {
                url.append("&");
            }
            url.append("utm_term=");
            url.append(acquisition.getTerm());
        }
        return new URL(url.toString());
    }

    private void configureRedirection(final Campaign campaign,
                                      final Acquisition acquisition,
                                      final String urlToken,
                                      final Long interactionId,
                                      final InternetDomainName hostDomainName,
                                      final HttpServletResponse httpServletResponse) throws MalformedURLException {
        final String redirectionUrl = buildUrl(campaign.getCampaignUrl(), acquisition).toString();
        httpServletResponse.setHeader("Location", redirectionUrl);

        final Map<String, String> cookies = new HashMap<>() {{
            put("__pcId", String.valueOf(campaign.getId()));
            put("__pcUt", urlToken);
            put("__intId", String.valueOf(interactionId));
        }};

        addCookies(cookies, hostDomainName, httpServletResponse);
        httpServletResponse.setStatus(302);
    }

    private boolean isCampaignActive(final Campaign campaign,
                                     final LocalDateTime interactionTime) {
        final String status = campaign.getStatus().getName();
        return status.equals("Active") && (campaign.getEnd() == null || campaign.getEnd().isAfter(interactionTime));
    }

    private boolean isCampaignDraft(final Campaign campaign) {
        final String status = campaign.getStatus().getName();
        return status.equals("Draft");
    }

    private Long recordInteraction(final Campaign campaign,
                                   final LocalDateTime interactionTime,
                                   final HttpServletRequest httpServletRequest) {
        final String userAgent = httpServletRequest.getHeader("User-Agent");
        final String ipAddress = HttpRequestResponseUtils.getClientIpAddressIfServletRequestExist();
        final Interaction interaction = new Interaction(Interaction.Type.CLICK);
        interaction.setTimestamp(interactionTime);
        interaction.setCampaign(campaign);
        interaction.setIpAddress(ipAddress);
        interaction.setUserAgent(userAgent);
        interactionRepository.save(interaction);
        return interaction.getId();
    }

    @RequestMapping(value = "/redirect")
    public void redirect(final HttpServletRequest httpServletRequest,
                         final HttpServletResponse httpServletResponse) {
        logger.info(httpServletRequest.getQueryString());
        Collections.list(httpServletRequest.getHeaderNames()).forEach((headerName -> logger.info("{} : {}", headerName, httpServletRequest.getHeader(headerName))));
        httpServletResponse.setStatus(204);
    }

    @RequestMapping(value = {"/{urlToken:[A-z]{8}}"})
    public void redirect(@RequestHeader final String host,
                         @PathVariable final String urlToken,
                         final HttpServletRequest httpServletRequest,
                         final HttpServletResponse httpServletResponse) throws MalformedURLException {
        logger.info("host : {}", host);
        InternetDomainName hostDomainName = null;
        try {
            Pattern pattern = Pattern.compile("^([^#]*?):?\\d*?$");
            Matcher matcher = pattern.matcher(host);
            if (matcher.find()) {
                hostDomainName = InternetDomainName.from(matcher.group(1));
            }
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
        }

        final LocalDateTime interactionTime = LocalDateTime.now(ZoneId.of("UTC"));
        final Optional<UrlToken> optionalToken = tokenRepository.findById(urlToken);

        if (optionalToken.isEmpty()) {
            logger.info("No token found for : {}", urlToken);
            httpServletResponse.setStatus(404);
            return;
        }

        final UrlToken token = optionalToken.get();
        logger.info("token : {}", token);

        final Campaign campaign = token.getCampaign();

        if (isCampaignDraft(campaign)) {
            httpServletResponse.setStatus(423);
            return;
        }

        if (!isCampaignActive(campaign, interactionTime)) {
            httpServletResponse.setStatus(410);
            return;
        }

        final Long interactionId = recordInteraction(campaign, interactionTime, httpServletRequest);
        final Acquisition acquisition = token.getAcquisition();

        configureRedirection(campaign, acquisition, urlToken, interactionId, hostDomainName, httpServletResponse);
        logger.info("Redirecting to {}", httpServletResponse.getHeader("Location"));
    }
}
