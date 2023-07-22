package com.liferay.sales.engineering.pulse.persistence;

import com.liferay.sales.engineering.pulse.model.Interaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InteractionRepository extends PagingAndSortingRepository<Interaction, Long>, CrudRepository<Interaction, Long>  {
}
