package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Campaign;
import com.liferay.sales.engineering.pulse.model.UrlToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlTokenRepository extends JpaRepository<UrlToken, String> {
    Page<UrlToken> findUrlTokensByCampaign(Campaign campaign, Pageable pageable);
}
