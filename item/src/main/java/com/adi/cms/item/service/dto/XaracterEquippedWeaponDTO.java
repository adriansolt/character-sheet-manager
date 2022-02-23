package com.adi.cms.item.service.dto;

import com.adi.cms.item.domain.enumeration.Handedness;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.item.domain.XaracterEquippedWeapon} entity.
 */
public class XaracterEquippedWeaponDTO implements Serializable {

    private Long id;

    private Long xaracterId;

    private Handedness hand;

    private WeaponDTO weaponId;

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

    public Handedness getHand() {
        return hand;
    }

    public void setHand(Handedness hand) {
        this.hand = hand;
    }

    public WeaponDTO getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(WeaponDTO weaponId) {
        this.weaponId = weaponId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof XaracterEquippedWeaponDTO)) {
            return false;
        }

        XaracterEquippedWeaponDTO xaracterEquippedWeaponDTO = (XaracterEquippedWeaponDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, xaracterEquippedWeaponDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "XaracterEquippedWeaponDTO{" +
            "id=" + getId() +
            ", xaracterId=" + getXaracterId() +
            ", hand='" + getHand() + "'" +
            ", weaponId=" + getWeaponId() +
            "}";
    }
}
