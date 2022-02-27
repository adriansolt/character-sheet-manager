package com.adi.cms.item.domain;

import com.adi.cms.item.domain.enumeration.ArmorLocation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArmorPiece.
 */
@Entity
@Table(name = "armor_piece")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArmorPiece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Integer weight;

    @NotNull
    @Column(name = "quality", nullable = false)
    private Integer quality;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private ArmorLocation location;

    @Column(name = "defense_modifier")
    private Integer defenseModifier;

    @OneToMany(mappedBy = "armorPiece")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "armorPiece" }, allowSetters = true)
    private Set<CharacterEquippedArmor> characterEquippedArmors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArmorPiece id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ArmorPiece name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArmorPiece description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public ArmorPiece weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQuality() {
        return this.quality;
    }

    public ArmorPiece quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public ArmorPiece picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public ArmorPiece pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public ArmorLocation getLocation() {
        return this.location;
    }

    public ArmorPiece location(ArmorLocation location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(ArmorLocation location) {
        this.location = location;
    }

    public Integer getDefenseModifier() {
        return this.defenseModifier;
    }

    public ArmorPiece defenseModifier(Integer defenseModifier) {
        this.setDefenseModifier(defenseModifier);
        return this;
    }

    public void setDefenseModifier(Integer defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public Set<CharacterEquippedArmor> getCharacterEquippedArmors() {
        return this.characterEquippedArmors;
    }

    public void setCharacterEquippedArmors(Set<CharacterEquippedArmor> characterEquippedArmors) {
        if (this.characterEquippedArmors != null) {
            this.characterEquippedArmors.forEach(i -> i.setArmorPiece(null));
        }
        if (characterEquippedArmors != null) {
            characterEquippedArmors.forEach(i -> i.setArmorPiece(this));
        }
        this.characterEquippedArmors = characterEquippedArmors;
    }

    public ArmorPiece characterEquippedArmors(Set<CharacterEquippedArmor> characterEquippedArmors) {
        this.setCharacterEquippedArmors(characterEquippedArmors);
        return this;
    }

    public ArmorPiece addCharacterEquippedArmor(CharacterEquippedArmor characterEquippedArmor) {
        this.characterEquippedArmors.add(characterEquippedArmor);
        characterEquippedArmor.setArmorPiece(this);
        return this;
    }

    public ArmorPiece removeCharacterEquippedArmor(CharacterEquippedArmor characterEquippedArmor) {
        this.characterEquippedArmors.remove(characterEquippedArmor);
        characterEquippedArmor.setArmorPiece(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArmorPiece)) {
            return false;
        }
        return id != null && id.equals(((ArmorPiece) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArmorPiece{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", location='" + getLocation() + "'" +
            ", defenseModifier=" + getDefenseModifier() +
            "}";
    }
}
