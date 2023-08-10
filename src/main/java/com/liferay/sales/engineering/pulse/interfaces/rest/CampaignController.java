package com.liferay.sales.engineering.pulse.interfaces.rest;

import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Interaction;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import com.liferay.sales.engineering.pulse.persistence.CampaignRepository;
import com.liferay.sales.engineering.pulse.persistence.InteractionRepository;
import com.liferay.sales.engineering.pulse.persistence.UrlTokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignRepository campaignRepository;
    private final InteractionRepository interactionRepository;
    private final UrlTokenRepository urlTokenRepository;

    CampaignController(CampaignRepository campaignRepository, final InteractionRepository interactionRepository, final UrlTokenRepository urlTokenRepository) {
        this.campaignRepository = campaignRepository;
        this.interactionRepository = interactionRepository;
        this.urlTokenRepository = urlTokenRepository;
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
