package com.adi.cms.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.CharacterEquippedArmor} entity.
 */
public class CharacterEquippedArmorDTO implements Serializable {

    private Long id;

    private ArmorPieceDTO armorPiece;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArmorPieceDTO getArmorPiece() {
        return armorPiece;
    }

    public void setArmorPiece(ArmorPieceDTO armorPiece) {
        this.armorPiece = armorPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterEquippedArmorDTO)) {
            return false;
        }

        CharacterEquippedArmorDTO characterEquippedArmorDTO = (CharacterEquippedArmorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, characterEquippedArmorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedArmorDTO{" +
            "id=" + getId() +
            ", armorPiece=" + getArmorPiece() +
            "}";
    }
}
