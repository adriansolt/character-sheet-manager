package com.adi.cms.item.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.adi.cms.item.domain.WeaponManeuver} entity.
 */
public class WeaponManeuverDTO implements Serializable {

    private Long id;

    private WeaponDTO weaponId;

    private ManeuverDTO maneuverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeaponDTO getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(WeaponDTO weaponId) {
        this.weaponId = weaponId;
    }

    public ManeuverDTO getManeuverId() {
        return maneuverId;
    }

    public void setManeuverId(ManeuverDTO maneuverId) {
        this.maneuverId = maneuverId;
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
            ", weaponId=" + getWeaponId() +
            ", maneuverId=" + getManeuverId() +
            "}";
    }
}
