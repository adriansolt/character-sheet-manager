package com.adi.cms.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.CampaignUser} entity.
 */
public class CampaignUserDTO implements Serializable {

    private Long id;

    private CampaignDTO campaignId;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CampaignDTO getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(CampaignDTO campaignId) {
        this.campaignId = campaignId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignUserDTO)) {
            return false;
        }

        CampaignUserDTO campaignUserDTO = (CampaignUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, campaignUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampaignUserDTO{" +
            "id=" + getId() +
            ", campaignId=" + getCampaignId() +
            ", user=" + getUser() +
            "}";
    }
}
