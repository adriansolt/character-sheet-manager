package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CampaignUser.
 */
@Table("campaign_user")
public class CampaignUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    @JsonIgnoreProperties(value = { "campaignUsers" }, allowSetters = true)
    private Campaign campaignId;

    @Transient
    private User user;

    @Column("campaign_id_id")
    private Long campaignIdId;

    @Column("user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CampaignUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Campaign getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(Campaign campaign) {
        this.campaignId = campaign;
        this.campaignIdId = campaign != null ? campaign.getId() : null;
    }

    public CampaignUser campaignId(Campaign campaign) {
        this.setCampaignId(campaign);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userId = user != null ? user.getId() : null;
    }

    public CampaignUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Long getCampaignIdId() {
        return this.campaignIdId;
    }

    public void setCampaignIdId(Long campaign) {
        this.campaignIdId = campaign;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignUser)) {
            return false;
        }
        return id != null && id.equals(((CampaignUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampaignUser{" +
            "id=" + getId() +
            "}";
    }
}
