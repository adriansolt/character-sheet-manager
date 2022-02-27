package com.adi.cms.gateway.service.dto;

import com.adi.cms.gateway.domain.enumeration.ArmorLocation;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.ArmorPiece} entity.
 */
public class ArmorPieceDTO extends ItemDTO {

    private ArmorLocation location;

    private Integer defenseModifier;

    public ArmorLocation getLocation() {
        return location;
    }

    public void setLocation(ArmorLocation location) {
        this.location = location;
    }

    public Integer getDefenseModifier() {
        return defenseModifier;
    }

    public void setDefenseModifier(Integer defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public CampaignDTO getCampaign() {
        return campaign;
    }

    public void setCampaign(CampaignDTO campaign) {
        this.campaign = campaign;
    }

    public CharacterDTO getCharacter() {
        return character;
    }

    public void setCharacter(CharacterDTO character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmorPieceDTO)) {
            return false;
        }

        ArmorPieceDTO armorPieceDTO = (ArmorPieceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, armorPieceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmorPieceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", location='" + getLocation() + "'" +
            ", defenseModifier=" + getDefenseModifier() +
            ", campaign=" + getCampaign() +
            ", character=" + getCharacter() +
            "}";
    }
}
