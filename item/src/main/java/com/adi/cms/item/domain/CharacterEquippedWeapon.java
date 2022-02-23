package com.adi.cms.item.domain;

import com.adi.cms.item.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CharacterEquippedWeapon.
 */
@Entity
@Table(name = "character_equipped_weapon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CharacterEquippedWeapon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "character_id")
    private Long characterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "hand")
    private Handedness hand;

    @ManyToOne
    @JsonIgnoreProperties(value = { "characterEquippedWeapons", "weaponManeuvers" }, allowSetters = true)
    private Weapon weapon;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterEquippedWeapon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public CharacterEquippedWeapon characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Handedness getHand() {
        return this.hand;
    }

    public CharacterEquippedWeapon hand(Handedness hand) {
        this.setHand(hand);
        return this;
    }

    public void setHand(Handedness hand) {
        this.hand = hand;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public CharacterEquippedWeapon weapon(Weapon weapon) {
        this.setWeapon(weapon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterEquippedWeapon)) {
            return false;
        }
        return id != null && id.equals(((CharacterEquippedWeapon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedWeapon{" +
            "id=" + getId() +
            ", characterId=" + getCharacterId() +
            ", hand='" + getHand() + "'" +
            "}";
    }
}
