package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.Status;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final AcquisitionRepository acquisitionRepository;
    private final CampaignRepository campaignRepository;
    private final StatusRepository statusRepository;
    private final UrlTokenRepository urlTokenRepository;

    Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    public DatabaseLoader(AcquisitionRepository acquisitionRepository,
                          CampaignRepository campaignRepository,
                          StatusRepository statusRepository,
                          UrlTokenRepository urlTokenRepository) {
        this.acquisitionRepository = acquisitionRepository;
        this.campaignRepository = campaignRepository;
        this.statusRepository = statusRepository;
        this.urlTokenRepository = urlTokenRepository;
    }

    private void buildCampaigns() {
        // Complete campaign
        Campaign.CampaignBuilder campaignBuilder = new Campaign.CampaignBuilder("23Q1", getStatus("Complete"), "/redirect");
        campaignBuilder.withBegin(LocalDateTime.parse("2023-01-10T00:00:00"));
        Campaign campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        UrlToken.UrlTokenBuilder tokenBuilder = new UrlToken.UrlTokenBuilder("TJLZtsCk", campaign);
        UrlToken urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added completed campaign {} for {}", campaign.getName(), urlToken.getToken());

        // Expired campaign
        campaignBuilder = new Campaign.CampaignBuilder("July Promo", getStatus("Expired"), "/redirect");
        campaignBuilder.withBegin(LocalDateTime.parse("2023-07-01T00:00:00"));
        campaignBuilder.withEnd(LocalDateTime.parse("2023-07-22T00:00:00"));
        campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        tokenBuilder = new UrlToken.UrlTokenBuilder("vasVrrzc", campaign);
        urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added expired campaign {} for {}", campaign.getName(), urlToken.getToken());

        // Active campaign with acquisitions
        campaignBuilder = new Campaign.CampaignBuilder("Free trial", getStatus("Active"), "/redirect");
        campaignBuilder.withEnd(LocalDateTime.now(ZoneId.of("UTC")).plusMonths(3));
        campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        Acquisition.AcquisitionBuilder acquisitionBuilder = new Acquisition.AcquisitionBuilder("Free trial");
        acquisitionBuilder.withMedium("cpc");
        Acquisition cpcAcquisition = acquisitionBuilder.build();
        acquisitionRepository.save(cpcAcquisition);

        tokenBuilder = new UrlToken.UrlTokenBuilder("GGkHqgbt", campaign);
        tokenBuilder.withAcquisition(cpcAcquisition);
        urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added active campaign {} for {} with acquisition {}", campaign.getName(), urlToken.getToken(), cpcAcquisition.getCampaign());

        acquisitionBuilder = new Acquisition.AcquisitionBuilder("Free trial");
        acquisitionBuilder.withMedium("organic");
        Acquisition organicAcquisition = acquisitionBuilder.build();
        acquisitionRepository.save(organicAcquisition);

        tokenBuilder = new UrlToken.UrlTokenBuilder("pBbLEjYV", campaign);
        tokenBuilder.withAcquisition(organicAcquisition);
        urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added active campaign {} for {} with acquisition {}", campaign.getName(), urlToken.getToken(), organicAcquisition.getCampaign());

        // Active campaign w/o acquisitions
        campaignBuilder = new Campaign.CampaignBuilder("Email campaign", getStatus("Active"), "/redirect");
        campaignBuilder.withEnd(LocalDateTime.now(ZoneId.of("UTC")).plusMonths(6));
        campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        tokenBuilder = new UrlToken.UrlTokenBuilder("LbTAwqAw", campaign);
        urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added active campaign {} for {}", campaign.getName(), urlToken.getToken());

        // Draft campaign
        campaignBuilder = new Campaign.CampaignBuilder("Test", getStatus("Draft"), "/redirect");
        campaign = campaignBuilder.build();
        campaignRepository.save(campaign);

        tokenBuilder = new UrlToken.UrlTokenBuilder("upGpGxvM", campaign);
        urlToken = tokenBuilder.build();
        urlTokenRepository.save(urlToken);

        logger.info("Added draft campaign {} for {}", campaign.getName(), urlToken.getToken());
    }

    private Status getStatus(String name) {
        return statusRepository.findByName(name);
    }

    private void populateStatus() {
        Arrays.asList("Draft", "Active", "Complete", "Inactive", "Expired").forEach((name) -> statusRepository.save(new Status(name)));
    }

    @Override
    public void run(String... params) {
        populateStatus();
        logger.info("Populated statuses");
        buildCampaigns();
    }
}
