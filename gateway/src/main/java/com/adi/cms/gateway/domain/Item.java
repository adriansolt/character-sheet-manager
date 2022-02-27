package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Item.
 */
@Table("item")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @NotNull(message = "must not be null")
    @Column("weight")
    private Integer weight;

    @NotNull(message = "must not be null")
    @Column("quality")
    private Integer quality;

    @Column("picture")
    private byte[] picture;

    @Column("picture_content_type")
    private String pictureContentType;

    @Transient
    @JsonIgnoreProperties(value = { "campaignUsers", "characters", "items", "weapons", "armorPieces" }, allowSetters = true)
    private Campaign campaign;

    @Transient
    @JsonIgnoreProperties(
        value = { "notes", "characterAttributes", "characterSkills", "items", "weapons", "armorPieces", "user", "campaign" },
        allowSetters = true
    )
    private Character character;

    @Column("campaign_id")
    private Long campaignId;

    @Column("character_id")
    private Long characterId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Item description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Item weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQuality() {
        return this.quality;
    }

    public Item quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Item picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Item pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
        this.campaignId = campaign != null ? campaign.getId() : null;
    }

    public Item campaign(Campaign campaign) {
        this.setCampaign(campaign);
        return this;
    }

    public Character getCharacter() {
        return this.character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.characterId = character != null ? character.getId() : null;
    }

    public Item character(Character character) {
        this.setCharacter(character);
        return this;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(Long campaign) {
        this.campaignId = campaign;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public void setCharacterId(Long character) {
        this.characterId = character;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            "}";
    }
}
