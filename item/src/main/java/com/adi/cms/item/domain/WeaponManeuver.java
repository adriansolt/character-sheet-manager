package com.adi.cms.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WeaponManeuver.
 */
@Entity
@Table(name = "weapon_maneuver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WeaponManeuver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers" }, allowSetters = true)
    private Weapon weapon;

    @ManyToOne
    @JsonIgnoreProperties(value = { "weaponManeuvers" }, allowSetters = true)
    private Maneuver maneuver;

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
    }

    public WeaponManeuver maneuver(Maneuver maneuver) {
        this.setManeuver(maneuver);
        return this;
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
