package com.adi.cms.item.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.item.domain.WeaponManeuver} entity.
 */
public class WeaponManeuverDTO implements Serializable {

    private Long id;

    private WeaponDTO weapon;

    private ManeuverDTO maneuver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeaponDTO getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponDTO weapon) {
        this.weapon = weapon;
    }

    public ManeuverDTO getManeuver() {
        return maneuver;
    }

    public void setManeuver(ManeuverDTO maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeaponManeuverDTO)) {
            return false;
        }

        WeaponManeuverDTO weaponManeuverDTO = (WeaponManeuverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weaponManeuverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeaponManeuverDTO{" +
            "id=" + getId() +
            ", weapon=" + getWeapon() +
            ", maneuver=" + getManeuver() +
            "}";
    }
}
