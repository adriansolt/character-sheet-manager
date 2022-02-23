package com.adi.cms.item.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.item.domain.XaracterEquippedArmor} entity.
 */
public class XaracterEquippedArmorDTO implements Serializable {

    private Long id;

    private Long xaracterId;

    private ArmorPieceDTO armorPiece;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getXaracterId() {
        return xaracterId;
    }

    public void setXaracterId(Long xaracterId) {
        this.xaracterId = xaracterId;
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
        if (!(o instanceof XaracterEquippedArmorDTO)) {
            return false;
        }

        XaracterEquippedArmorDTO xaracterEquippedArmorDTO = (XaracterEquippedArmorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, xaracterEquippedArmorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterEquippedArmorDTO{" +
            "id=" + getId() +
            ", xaracterId=" + getXaracterId() +
            ", armorPiece=" + getArmorPiece() +
            "}";
    }
}
