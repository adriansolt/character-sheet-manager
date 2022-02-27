package com.adi.cms.campaign.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.campaign.domain.Campaign} entity.
 */
public class CampaignDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @Lob
    private byte[] map;

    private String mapContentType;

    @NotNull
    private Long masterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMap() {
        return map;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return mapContentType;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignDTO)) {
            return false;
        }

        CampaignDTO campaignDTO = (CampaignDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, campaignDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampaignDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", map='" + getMap() + "'" +
            ", masterId=" + getMasterId() +
            "}";
    }
}
