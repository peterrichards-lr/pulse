package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.UrlToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UrlTokenRepository extends PagingAndSortingRepository<UrlToken, String>, CrudRepository<UrlToken, String> {
}
