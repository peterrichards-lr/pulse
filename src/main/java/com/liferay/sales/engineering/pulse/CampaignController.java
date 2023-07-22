package com.liferay.sales.engineering.pulse;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CampaignController {
    Logger logger = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    public CampaignController(UrlTokenRepository tokenRepository, final InteractionRepository interactionRepository) {
        this.tokenRepository = tokenRepository;
        this.interactionRepository = interactionRepository;
    }

    @RequestMapping(value = "/{urlToken:[A-z]{8}}")
    public void campaignRedirect(@PathVariable final String urlToken, final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws UnsupportedEncodingException, MalformedURLException {
        final LocalDateTime interactionTime = LocalDateTime.now(ZoneId.of("UTC"));
        final Optional<UrlToken> optionalToken = tokenRepository.findById(urlToken);

        if (optionalToken.isEmpty()) {
            httpServletResponse.setStatus(404);
            return;
        }

        final UrlToken token = optionalToken.get();
        logger.info(token.toString());

        final Campaign campaign = token.getCampaign();

        if (!isCampaignActive(campaign, interactionTime)) {
            httpServletResponse.setStatus(410);
            return;
        }

        final Long interactionId = recordInteraction(campaign, interactionTime, httpServletRequest);
        final Acquisition acquisition = token.getAcquisition();

        configureRedirection(campaign, acquisition, urlToken, interactionId, httpServletResponse);
    }

    private boolean isCampaignActive(final Campaign campaign, final LocalDateTime interactionTime) {
        final String status = campaign.getStatus().getName();
        return status.equals("Active") && (campaign.getEnd() == null || campaign.getEnd().isAfter(interactionTime));
    }

    private Long recordInteraction(final Campaign campaign, final LocalDateTime interactionTime, final HttpServletRequest httpServletRequest) {
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

    private void configureRedirection(final Campaign campaign, final Acquisition acquisition, final String urlToken, final Long interactionId, final HttpServletResponse httpServletResponse) throws UnsupportedEncodingException, MalformedURLException {
        final String redirectionUrl = buildUrl(campaign.getCampaignUrl(), acquisition).toString();
        httpServletResponse.setHeader("Location", redirectionUrl);

        final Map<String, String> cookies = new HashMap<>() {{
            put("__pcId", String.valueOf(campaign.getId()));
            put("__pcUt", urlToken);
            put("__intId", String.valueOf(interactionId));
        }};

        addCookies(cookies, httpServletResponse);
        httpServletResponse.setStatus(302);
    }

    private URL buildUrl(String campaignUrl, Acquisition acquisition) throws MalformedURLException {
        if (acquisition == null) return new URL(campaignUrl);

        StringBuilder url = new StringBuilder(campaignUrl);
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

    private void addCookies(final Map<String, String> cookieMap, final HttpServletResponse httpServletResponse) {
        cookieMap.keySet().stream().map((key) -> {
            final String value = cookieMap.get(key);
            final Cookie cookie = new Cookie(key,value);
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
            return cookie;
        }).forEach(httpServletResponse::addCookie);
    }

    private final UrlTokenRepository tokenRepository;
    private final InteractionRepository interactionRepository;
}