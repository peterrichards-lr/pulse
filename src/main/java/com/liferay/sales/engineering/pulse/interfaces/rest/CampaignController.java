package com.liferay.sales.engineering.pulse.interfaces.rest;

import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Interaction;
import com.liferay.sales.engineering.pulse.persistence.CampaignRepository;
import com.liferay.sales.engineering.pulse.persistence.InteractionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    private final CampaignRepository campaignRepository;
    private final InteractionRepository interactionRepository;

    CampaignController(CampaignRepository campaignRepository, final InteractionRepository interactionRepository) {
        this.campaignRepository = campaignRepository;
        this.interactionRepository = interactionRepository;
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
    Page<Interaction> getInteractions(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int pageSize,
                                      @RequestParam(defaultValue = "id") String sortBy
    ) {
        final Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        final Campaign campaign = getCampaign(id);
        return interactionRepository.findInteractionByCampaign(campaign, paging);
    }
}
