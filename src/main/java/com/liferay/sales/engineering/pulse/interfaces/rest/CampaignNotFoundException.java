package com.liferay.sales.engineering.pulse.interfaces.rest;

class CampaignNotFoundException extends RuntimeException {
    CampaignNotFoundException(Long id) {
        super("Could not find campaign " + id);
    }
}
