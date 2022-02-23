package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A WeaponManeuver.
 */
@Table("weapon_maneuver")
public class WeaponManeuver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Transient
    @JsonIgnoreProperties(value = { "xaracterEquippedWeapons", "weaponManeuvers" }, allowSetters = true)
    private Weapon weaponId;

    @Transient
    @JsonIgnoreProperties(value = { "weaponManeuvers" }, allowSetters = true)
    private Maneuver maneuverId;

    @Column("weapon_id_id")
    private Long weaponIdId;

    @Column("maneuver_id_id")
    private Long maneuverIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeaponManeuver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weapon getWeaponId() {
        return this.weaponId;
    }

    public void setWeaponId(Weapon weapon) {
        this.weaponId = weapon;
        this.weaponIdId = weapon != null ? weapon.getId() : null;
    }

    public WeaponManeuver weaponId(Weapon weapon) {
        this.setWeaponId(weapon);
        return this;
    }

    public Maneuver getManeuverId() {
        return this.maneuverId;
    }

    public void setManeuverId(Maneuver maneuver) {
        this.maneuverId = maneuver;
        this.maneuverIdId = maneuver != null ? maneuver.getId() : null;
    }

    public WeaponManeuver maneuverId(Maneuver maneuver) {
        this.setManeuverId(maneuver);
        return this;
    }

    public Long getWeaponIdId() {
        return this.weaponIdId;
    }

    public void setWeaponIdId(Long weapon) {
        this.weaponIdId = weapon;
    }

    public Long getManeuverIdId() {
        return this.maneuverIdId;
    }

    public void setManeuverIdId(Long maneuver) {
        this.maneuverIdId = maneuver;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeaponManeuver)) {
            return false;
        }
        return id != null && id.equals(((WeaponManeuver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeaponManeuver{" +
            "id=" + getId() +
            "}";
    }
}
