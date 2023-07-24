package com.liferay.sales.engineering.pulse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Interaction {
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
    private @Id
    @GeneratedValue Long id;
    private String ipAddress;
    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String userAgent;

    public Interaction() {
    }

    public Interaction(final Type type) {
        this.type = type;
        this.timestamp = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(final Campaign campaign) {
        this.campaign = campaign;
    }

    public Long getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    public enum Type {
        OPEN, CLICK
    }
}
