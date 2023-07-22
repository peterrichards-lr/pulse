package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Campaign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CampaignRepository extends PagingAndSortingRepository<Campaign, Long>, CrudRepository<Campaign, Long> {
}
