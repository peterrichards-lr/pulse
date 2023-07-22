package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Campaign {
    private @Id
    @GeneratedValue Long id;
    @Column(unique=true)
    private String name;
    private String description;
    private LocalDateTime begin;
    private LocalDateTime end;
    private String campaignUrl;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Campaign() {}

    private Campaign(CampaignBuilder campaignBuilder) {
        this.name = campaignBuilder.name;
        this.description = campaignBuilder.description;
        this.begin = campaignBuilder.begin;
        this.end = campaignBuilder.end;
        this.campaignUrl = campaignBuilder.campaignUrl;
        this.status = campaignBuilder.status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(final LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(final LocalDateTime end) {
        this.end = end;
    }

    public String getCampaignUrl() {
        return campaignUrl;
    }

    public void setCampaignUrl(final String campaignUrl) {
        this.campaignUrl = campaignUrl;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign)) return false;
        final Campaign campaign = (Campaign) o;
        return Objects.equal(getId(), campaign.getId()) && Objects.equal(getName(), campaign.getName()) && Objects.equal(getDescription(), campaign.getDescription()) && Objects.equal(getBegin(), campaign.getBegin()) && Objects.equal(getEnd(), campaign.getEnd()) && Objects.equal(campaignUrl, campaign.campaignUrl) && getStatus() == campaign.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getName(), getDescription(), getBegin(), getEnd(), campaignUrl, getStatus());
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", begin=" + begin +
                ", end=" + end +
                ", campaignUrl='" + campaignUrl + '\'' +
                ", status=" + status +
                '}';
    }

    public static class CampaignBuilder {
        String name;
        String description;
        LocalDateTime begin;
        LocalDateTime end;
        String campaignUrl;
        Status status;

        public CampaignBuilder(String name, Status status, String campaignUrl) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("Name must have a value");
            }
            if (status == null) {
                throw new IllegalArgumentException("Status cannot be null");
            }
            if (StringUtils.isBlank(campaignUrl)) {
                throw new IllegalArgumentException("CampaignUrl must have a value");
            }
            this.name = name;
            this.status = status;
            this.campaignUrl = campaignUrl;
            this.begin = LocalDateTime.now(ZoneId.of("UTC"));
        }

        public CampaignBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CampaignBuilder withBegin(LocalDateTime begin) {
            if (begin == null) {
                throw new IllegalArgumentException("Begin cannot be null");
            }
            this.begin = begin;
            return this;
        }

        public CampaignBuilder withEnd(LocalDateTime end) {
            this.end = end;
            return this;
        }

        public Campaign build() {
            return new Campaign(this);
        }
    }
}
