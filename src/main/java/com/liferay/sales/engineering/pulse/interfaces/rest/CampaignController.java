package com.liferay.sales.engineering.pulse.interfaces.rest;

import com.liferay.sales.engineering.pulse.model.*;
import com.liferay.sales.engineering.pulse.persistence.*;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final AcquisitionRepository acquisitionRepository;
    private final CampaignRepository campaignRepository;
    private final InteractionRepository interactionRepository;
    private final Logger logger = LoggerFactory.getLogger(CampaignController.class);
    private final StatusRepository statusRepository;
    private final UrlTokenRepository urlTokenRepository;

    CampaignController(final AcquisitionRepository acquisitionRepository, CampaignRepository campaignRepository, final InteractionRepository interactionRepository, final UrlTokenRepository urlTokenRepository, final StatusRepository statusRepository) {
        this.acquisitionRepository = acquisitionRepository;
        this.campaignRepository = campaignRepository;
        this.interactionRepository = interactionRepository;
        this.urlTokenRepository = urlTokenRepository;
        this.statusRepository = statusRepository;
    }

    private Acquisition buildAcquisition(CampaignDto campaignDto) {
        Acquisition.AcquisitionBuilder acquisitionBuilder = new Acquisition.AcquisitionBuilder();
        acquisitionBuilder.withSource(campaignDto.getUtmSource());
        acquisitionBuilder.withMedium(campaignDto.getUtmMedium());
        acquisitionBuilder.withContent(campaignDto.getUtmContent());
        acquisitionBuilder.withTerm(campaignDto.getUtmTerm());
        Acquisition acquisition = acquisitionBuilder.build();
        if (acquisition != null) {
            acquisitionRepository.save(acquisition);
        }
        return acquisition;
    }

    @PostMapping
    UrlToken create(@Valid @RequestBody CampaignDto campaignDto) {
        if (campaignRepository.existsByName(campaignDto.getName())) {
            throw new DuplicateCampaignNameException(campaignDto.getName());
        }

        final Status status = getStatus(campaignDto.getStatus());
        final Acquisition acquisition = buildAcquisition(campaignDto);

        Campaign.CampaignBuilder campaignBuilder = new Campaign.CampaignBuilder(campaignDto.getName(), status, campaignDto.getCampaignUrl());
        final Campaign campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        final String token = createToken();
        UrlToken.UrlTokenBuilder tokenBuilder = new UrlToken.UrlTokenBuilder(token, campaign);

        if (acquisition != null) {
            tokenBuilder.withAcquisition(acquisition);
        }

        UrlToken urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        return urlToken;
    }

    private String createToken() {
        String token = StringUtils.generateToken(8);
        Optional<UrlToken> urlToken = urlTokenRepository.findById(token);
        while (!urlToken.isEmpty()) {
            token = StringUtils.generateToken(8);
            urlToken = urlTokenRepository.findById(token);
        }
        return token;
    }

    @GetMapping("/{id}")
    Campaign getCampaign(@PathVariable Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(id));
    }

    @GetMapping
    Page<Campaign> getCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return campaignRepository.findAll(paging);
    }

    @GetMapping("/{id}/interactions")
    Page<Interaction> getInteractions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        final Campaign campaign = getCampaign(id);
        return interactionRepository.findInteractionByCampaign(campaign, paging);
    }

    private Status getStatus(String value) {
        final String name = StringUtils.convertToTitleCaseIteratingChars(value);
        return statusRepository.findByName(name);
    }

    @GetMapping("/{id}/url-tokens")
    Page<UrlToken> getUrlTokens(@PathVariable Long id,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int pageSize,
                                @RequestParam(defaultValue = "token") String sortBy) {
        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        final Campaign campaign = getCampaign(id);
        return urlTokenRepository.findUrlTokensByCampaign(campaign, paging);
    }
}
