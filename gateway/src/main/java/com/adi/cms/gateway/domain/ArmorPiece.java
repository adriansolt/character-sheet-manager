package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ArmorPiece.
 */
@Table("armor_piece")
public class ArmorPiece implements Serializable {

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

    @Column("character_id")
    private Long characterId;

    @Column("campaign_id")
    private Long campaignId;

    @Column("location")
    private ArmorLocation location;

    @Column("defense_modifier")
    private Integer defenseModifier;

    @Transient
    @JsonIgnoreProperties(value = { "armorPiece" }, allowSetters = true)
    private Set<CharacterEquippedArmor> characterEquippedArmors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmorPiece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ArmorPiece name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArmorPiece description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public ArmorPiece weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQuality() {
        return this.quality;
    }

    public ArmorPiece quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public ArmorPiece picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public ArmorPiece pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public ArmorPiece characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public ArmorPiece campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public ArmorLocation getLocation() {
        return this.location;
    }

    public ArmorPiece location(ArmorLocation location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(ArmorLocation location) {
        this.location = location;
    }

    public Integer getDefenseModifier() {
        return this.defenseModifier;
    }

    public ArmorPiece defenseModifier(Integer defenseModifier) {
        this.setDefenseModifier(defenseModifier);
        return this;
    }

    public void setDefenseModifier(Integer defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public Set<CharacterEquippedArmor> getCharacterEquippedArmors() {
        return this.characterEquippedArmors;
    }

    public void setCharacterEquippedArmors(Set<CharacterEquippedArmor> characterEquippedArmors) {
        if (this.characterEquippedArmors != null) {
            this.characterEquippedArmors.forEach(i -> i.setArmorPiece(null));
        }
        if (characterEquippedArmors != null) {
            characterEquippedArmors.forEach(i -> i.setArmorPiece(this));
        }
        this.characterEquippedArmors = characterEquippedArmors;
    }

    public ArmorPiece characterEquippedArmors(Set<CharacterEquippedArmor> characterEquippedArmors) {
        this.setCharacterEquippedArmors(characterEquippedArmors);
        return this;
    }

    public ArmorPiece addCharacterEquippedArmor(CharacterEquippedArmor characterEquippedArmor) {
        this.characterEquippedArmors.add(characterEquippedArmor);
        characterEquippedArmor.setArmorPiece(this);
        return this;
    }

    public ArmorPiece removeCharacterEquippedArmor(CharacterEquippedArmor characterEquippedArmor) {
        this.characterEquippedArmors.remove(characterEquippedArmor);
        characterEquippedArmor.setArmorPiece(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmorPiece)) {
            return false;
        }
        return id != null && id.equals(((ArmorPiece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmorPiece{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", characterId=" + getCharacterId() +
            ", campaignId=" + getCampaignId() +
            ", location='" + getLocation() + "'" +
            ", defenseModifier=" + getDefenseModifier() +
            "}";
    }
}
