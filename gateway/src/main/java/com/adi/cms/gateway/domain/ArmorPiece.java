package com.adi.cms.gateway.domain;

import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.PrimaryKeyJoinColumn;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ArmorPiece.
 */
@Table("armor_piece")
@PrimaryKeyJoinColumn(name="id")
public class ArmorPiece extends Item {

    @Column("location")
    private ArmorLocation location;

    @Column("defense_modifier")
    private Integer defenseModifier;

    @Transient
    @JsonIgnoreProperties(value = { "armorPiece" }, allowSetters = true)
    private Set<CharacterEquippedArmor> characterEquippedArmors = new HashSet<>();

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

    public ArmorPiece id(Long id) {
        this.setId(id);
        return this;
    }

    public ArmorPiece name(String name) {
        this.setName(name);
        return this;
    }

    public ArmorPiece description(String description) {
        this.setDescription(description);
        return this;
    }

    public ArmorPiece weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public ArmorPiece quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public ArmorPiece picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public ArmorPiece pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public ArmorPiece characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
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

    public Campaign getCampaign() {
        return this.campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
        this.campaignId = campaign != null ? campaign.getId() : null;
    }

    public ArmorPiece campaign(Campaign campaign) {
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

    public ArmorPiece character(Character character) {
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
            ", location='" + getLocation() + "'" +
            ", defenseModifier=" + getDefenseModifier() +
            "}";
    }
}
