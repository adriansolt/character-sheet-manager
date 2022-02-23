package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Campaign.
 */
@Table("campaign")
public class Campaign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("map")
    private byte[] map;

    @Column("map_content_type")
    private String mapContentType;

    @Column("master_id")
    private Long masterId;

    @Transient
    @JsonIgnoreProperties(value = { "campaignId", "user" }, allowSetters = true)
    private Set<CampaignUser> campaignUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Campaign id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Campaign name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Campaign description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getMap() {
        return this.map;
    }

    public Campaign map(byte[] map) {
        this.setMap(map);
        return this;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }

    public String getMapContentType() {
        return this.mapContentType;
    }

    public Campaign mapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
        return this;
    }

    public void setMapContentType(String mapContentType) {
        this.mapContentType = mapContentType;
    }

    public Long getMasterId() {
        return this.masterId;
    }

    public Campaign masterId(Long masterId) {
        this.setMasterId(masterId);
        return this;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public Set<CampaignUser> getCampaignUsers() {
        return this.campaignUsers;
    }

    public void setCampaignUsers(Set<CampaignUser> campaignUsers) {
        if (this.campaignUsers != null) {
            this.campaignUsers.forEach(i -> i.setCampaignId(null));
        }
        if (campaignUsers != null) {
            campaignUsers.forEach(i -> i.setCampaignId(this));
        }
        this.campaignUsers = campaignUsers;
    }

    public Campaign campaignUsers(Set<CampaignUser> campaignUsers) {
        this.setCampaignUsers(campaignUsers);
        return this;
    }

    public Campaign addCampaignUser(CampaignUser campaignUser) {
        this.campaignUsers.add(campaignUser);
        campaignUser.setCampaignId(this);
        return this;
    }

    public Campaign removeCampaignUser(CampaignUser campaignUser) {
        this.campaignUsers.remove(campaignUser);
        campaignUser.setCampaignId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campaign)) {
            return false;
        }
        return id != null && id.equals(((Campaign) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Campaign{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", map='" + getMap() + "'" +
            ", mapContentType='" + getMapContentType() + "'" +
            ", masterId=" + getMasterId() +
            "}";
    }
}
