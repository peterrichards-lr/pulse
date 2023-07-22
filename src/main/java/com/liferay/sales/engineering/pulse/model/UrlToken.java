package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import jakarta.persistence.*;

@Entity
public class UrlToken {
    private @Id String token;
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
    @ManyToOne
    @JoinColumn(name = "acquisition_id")
    private Acquisition acquisition;

    public UrlToken() {}

    private UrlToken(UrlTokenBuilder builder) {
        this.token = builder.token;
        this.campaign = builder.campaign;
        this.acquisition = builder.acquisition;
    }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Acquisition getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(Acquisition acquisition) {
        this.acquisition = acquisition;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final UrlToken urlToken)) return false;
        return Objects.equal(token, urlToken.token) && Objects.equal(getCampaign(), urlToken.getCampaign()) && Objects.equal(getAcquisition(), urlToken.getAcquisition());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token, getCampaign(), getAcquisition());
    }

    @Override
    public String toString() {
        return "UrlToken{" +
                ", token='" + token + '\'' +
                ", campaign=" + campaign +
                ", acquisition=" + acquisition +
                '}';
    }

    public static class UrlTokenBuilder {
        private String token;
        private Campaign campaign;
        private Acquisition acquisition;

        public UrlTokenBuilder(String token, Campaign campaign) {
            if (StringUtils.isBlank(token)) {
                throw new IllegalArgumentException("Token must have a value");
            }
            if (campaign == null) {
                throw new IllegalArgumentException("Campaign cannot be null");
            }
            this.token = token;
            this.campaign = campaign;
        }

        public UrlTokenBuilder withAcquisition(Acquisition acquisition) {
            if (acquisition == null) {
                throw new IllegalArgumentException("Acquisition cannot be null");
            }
            this.acquisition = acquisition;
            return this;
        }

        public UrlToken build() { return new UrlToken(this); }
    }
}
