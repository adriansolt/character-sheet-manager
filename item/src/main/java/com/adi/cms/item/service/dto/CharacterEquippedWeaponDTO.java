package com.adi.cms.item.service.dto;

import com.adi.cms.item.domain.enumeration.Handedness;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.item.domain.CharacterEquippedWeapon} entity.
 */
public class CharacterEquippedWeaponDTO implements Serializable {

    private Long id;

    private Handedness hand;

    private WeaponDTO weapon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Handedness getHand() {
        return hand;
    }

    public void setHand(Handedness hand) {
        this.hand = hand;
    }

    public WeaponDTO getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponDTO weapon) {
        this.weapon = weapon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterEquippedWeaponDTO)) {
            return false;
        }

        CharacterEquippedWeaponDTO characterEquippedWeaponDTO = (CharacterEquippedWeaponDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, characterEquippedWeaponDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedWeaponDTO{" +
            "id=" + getId() +
            ", hand='" + getHand() + "'" +
            ", weapon=" + getWeapon() +
            "}";
    }
}
