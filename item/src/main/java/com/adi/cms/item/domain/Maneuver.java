package com.adi.cms.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Maneuver.
 */
@Entity
@Table(name = "maneuver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Maneuver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "modifier")
    private Integer modifier;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "maneuver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "weapon", "maneuver" }, allowSetters = true)
    private Set<WeaponManeuver> weaponManeuvers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Maneuver id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Maneuver name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModifier() {
        return this.modifier;
    }

    public Maneuver modifier(Integer modifier) {
        this.setModifier(modifier);
        return this;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public String getDescription() {
        return this.description;
    }

    public Maneuver description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WeaponManeuver> getWeaponManeuvers() {
        return this.weaponManeuvers;
    }

    public void setWeaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        if (this.weaponManeuvers != null) {
            this.weaponManeuvers.forEach(i -> i.setManeuver(null));
        }
        if (weaponManeuvers != null) {
            weaponManeuvers.forEach(i -> i.setManeuver(this));
        }
        this.weaponManeuvers = weaponManeuvers;
    }

    public Maneuver weaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        this.setWeaponManeuvers(weaponManeuvers);
        return this;
    }

    public Maneuver addWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.add(weaponManeuver);
        weaponManeuver.setManeuver(this);
        return this;
    }

    public Maneuver removeWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.remove(weaponManeuver);
        weaponManeuver.setManeuver(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Maneuver)) {
            return false;
        }
        return id != null && id.equals(((Maneuver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Maneuver{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", modifier=" + getModifier() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
