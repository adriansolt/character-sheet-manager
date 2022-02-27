package com.adi.cms.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CharacterEquippedArmor.
 */
@Entity
@Table(name = "character_equipped_armor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CharacterEquippedArmor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "characterEquippedArmors" }, allowSetters = true)
    private ArmorPiece armorPiece;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CharacterEquippedArmor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArmorPiece getArmorPiece() {
        return this.armorPiece;
    }

    public void setArmorPiece(ArmorPiece armorPiece) {
        this.armorPiece = armorPiece;
    }

    public CharacterEquippedArmor armorPiece(ArmorPiece armorPiece) {
        this.setArmorPiece(armorPiece);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterEquippedArmor)) {
            return false;
        }
        return id != null && id.equals(((CharacterEquippedArmor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CharacterEquippedArmor{" +
            "id=" + getId() +
            "}";
    }
}
