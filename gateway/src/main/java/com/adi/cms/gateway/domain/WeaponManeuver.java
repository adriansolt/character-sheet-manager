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
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers", "campaign", "character" }, allowSetters = true)
    private Weapon weapon;

    @Transient
    @JsonIgnoreProperties(value = { "weaponManeuvers" }, allowSetters = true)
    private Maneuver maneuver;

    @Column("weapon_id")
    private Long weaponId;

    @Column("maneuver_id")
    private Long maneuverId;

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

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
        this.weaponId = weapon != null ? weapon.getId() : null;
    }

    public WeaponManeuver weapon(Weapon weapon) {
        this.setWeapon(weapon);
        return this;
    }

    public Maneuver getManeuver() {
        return this.maneuver;
    }

    public void setManeuver(Maneuver maneuver) {
        this.maneuver = maneuver;
        this.maneuverId = maneuver != null ? maneuver.getId() : null;
    }

    public WeaponManeuver maneuver(Maneuver maneuver) {
        this.setManeuver(maneuver);
        return this;
    }

    public Long getWeaponId() {
        return this.weaponId;
    }

    public void setWeaponId(Long weapon) {
        this.weaponId = weapon;
    }

    public Long getManeuverId() {
        return this.maneuverId;
    }

    public void setManeuverId(Long maneuver) {
        this.maneuverId = maneuver;
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
