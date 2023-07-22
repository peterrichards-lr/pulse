package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Acquisition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AcquisitionRepository extends PagingAndSortingRepository<Acquisition, Long>, CrudRepository<Acquisition, Long> {
}
