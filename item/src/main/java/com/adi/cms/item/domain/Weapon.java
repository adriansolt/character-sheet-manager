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
 * A Weapon.
 */
@Entity
@Table(name = "weapon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Weapon implements Serializable {

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

    @Column(name = "character_id")
    private Long characterId;

    @Column(name = "campaign_id")
    private Long campaignId;

    @NotNull
    @Column(name = "reach", nullable = false)
    private Integer reach;

    @NotNull
    @Min(value = 0)
    @Column(name = "base_damage", nullable = false)
    private Integer baseDamage;

    @NotNull
    @Min(value = 1)
    @Column(name = "required_st", nullable = false)
    private Integer requiredST;

    @Column(name = "damage_modifier")
    private Integer damageModifier;

    @OneToMany(mappedBy = "weapon")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "weapon" }, allowSetters = true)
    private Set<CharacterEquippedWeapon> characterEquippedWeapons = new HashSet<>();

    @OneToMany(mappedBy = "weapon")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "weapon", "maneuver" }, allowSetters = true)
    private Set<WeaponManeuver> weaponManeuvers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Weapon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Weapon name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Weapon description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Weapon weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQuality() {
        return this.quality;
    }

    public Weapon quality(Integer quality) {
        this.setQuality(quality);
        return this;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Weapon picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Weapon pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Long getCharacterId() {
        return this.characterId;
    }

    public Weapon characterId(Long characterId) {
        this.setCharacterId(characterId);
        return this;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public Weapon campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getReach() {
        return this.reach;
    }

    public Weapon reach(Integer reach) {
        this.setReach(reach);
        return this;
    }

    public void setReach(Integer reach) {
        this.reach = reach;
    }

    public Integer getBaseDamage() {
        return this.baseDamage;
    }

    public Weapon baseDamage(Integer baseDamage) {
        this.setBaseDamage(baseDamage);
        return this;
    }

    public void setBaseDamage(Integer baseDamage) {
        this.baseDamage = baseDamage;
    }

    public Integer getRequiredST() {
        return this.requiredST;
    }

    public Weapon requiredST(Integer requiredST) {
        this.setRequiredST(requiredST);
        return this;
    }

    public void setRequiredST(Integer requiredST) {
        this.requiredST = requiredST;
    }

    public Integer getDamageModifier() {
        return this.damageModifier;
    }

    public Weapon damageModifier(Integer damageModifier) {
        this.setDamageModifier(damageModifier);
        return this;
    }

    public void setDamageModifier(Integer damageModifier) {
        this.damageModifier = damageModifier;
    }

    public Set<CharacterEquippedWeapon> getCharacterEquippedWeapons() {
        return this.characterEquippedWeapons;
    }

    public void setCharacterEquippedWeapons(Set<CharacterEquippedWeapon> characterEquippedWeapons) {
        if (this.characterEquippedWeapons != null) {
            this.characterEquippedWeapons.forEach(i -> i.setWeapon(null));
        }
        if (characterEquippedWeapons != null) {
            characterEquippedWeapons.forEach(i -> i.setWeapon(this));
        }
        this.characterEquippedWeapons = characterEquippedWeapons;
    }

    public Weapon characterEquippedWeapons(Set<CharacterEquippedWeapon> characterEquippedWeapons) {
        this.setCharacterEquippedWeapons(characterEquippedWeapons);
        return this;
    }

    public Weapon addCharacterEquippedWeapon(CharacterEquippedWeapon characterEquippedWeapon) {
        this.characterEquippedWeapons.add(characterEquippedWeapon);
        characterEquippedWeapon.setWeapon(this);
        return this;
    }

    public Weapon removeCharacterEquippedWeapon(CharacterEquippedWeapon characterEquippedWeapon) {
        this.characterEquippedWeapons.remove(characterEquippedWeapon);
        characterEquippedWeapon.setWeapon(null);
        return this;
    }

    public Set<WeaponManeuver> getWeaponManeuvers() {
        return this.weaponManeuvers;
    }

    public void setWeaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        if (this.weaponManeuvers != null) {
            this.weaponManeuvers.forEach(i -> i.setWeapon(null));
        }
        if (weaponManeuvers != null) {
            weaponManeuvers.forEach(i -> i.setWeapon(this));
        }
        this.weaponManeuvers = weaponManeuvers;
    }

    public Weapon weaponManeuvers(Set<WeaponManeuver> weaponManeuvers) {
        this.setWeaponManeuvers(weaponManeuvers);
        return this;
    }

    public Weapon addWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.add(weaponManeuver);
        weaponManeuver.setWeapon(this);
        return this;
    }

    public Weapon removeWeaponManeuver(WeaponManeuver weaponManeuver) {
        this.weaponManeuvers.remove(weaponManeuver);
        weaponManeuver.setWeapon(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weapon)) {
            return false;
        }
        return id != null && id.equals(((Weapon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weapon{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", quality=" + getQuality() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", characterId=" + getCharacterId() +
            ", campaignId=" + getCampaignId() +
            ", reach=" + getReach() +
            ", baseDamage=" + getBaseDamage() +
            ", requiredST=" + getRequiredST() +
            ", damageModifier=" + getDamageModifier() +
            "}";
    }
}
