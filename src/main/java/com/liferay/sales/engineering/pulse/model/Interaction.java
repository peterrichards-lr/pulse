package com.liferay.sales.engineering.pulse.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Interaction {
    private @Id @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private String ipAddress;

    private String userAgent;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private Type type;

    public Interaction() {}

    public Interaction(final Type type) {
        this.type = type;
        this.timestamp = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public Long getId() { return id; }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(final Campaign campaign) {
        this.campaign = campaign;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }


    public enum Type {
        OPEN, CLICK
    }
}
