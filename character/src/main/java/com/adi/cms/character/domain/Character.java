package com.adi.cms.character.domain;

import com.adi.cms.character.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Character.
 */
@Entity
@Table(name = "character")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "weight", nullable = false)
    private Integer weight;

    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "handedness")
    private Handedness handedness;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    private Set<CharacterAttribute> characterAttributes = new HashSet<>();

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "character", "skill" }, allowSetters = true)
    private Set<CharacterSkill> characterSkills = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Character id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Character name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Character weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Character height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Character points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Character picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Character pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Handedness getHandedness() {
        return this.handedness;
    }

    public Character handedness(Handedness handedness) {
        this.setHandedness(handedness);
        return this;
    }

    public void setHandedness(Handedness handedness) {
        this.handedness = handedness;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public Character campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Character active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setCharacter(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setCharacter(this));
        }
        this.notes = notes;
    }

    public Character notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Character addNote(Note note) {
        this.notes.add(note);
        note.setCharacter(this);
        return this;
    }

    public Character removeNote(Note note) {
        this.notes.remove(note);
        note.setCharacter(null);
        return this;
    }

    public Set<CharacterAttribute> getCharacterAttributes() {
        return this.characterAttributes;
    }

    public void setCharacterAttributes(Set<CharacterAttribute> characterAttributes) {
        if (this.characterAttributes != null) {
            this.characterAttributes.forEach(i -> i.setCharacter(null));
        }
        if (characterAttributes != null) {
            characterAttributes.forEach(i -> i.setCharacter(this));
        }
        this.characterAttributes = characterAttributes;
    }

    public Character characterAttributes(Set<CharacterAttribute> characterAttributes) {
        this.setCharacterAttributes(characterAttributes);
        return this;
    }

    public Character addCharacterAttribute(CharacterAttribute characterAttribute) {
        this.characterAttributes.add(characterAttribute);
        characterAttribute.setCharacter(this);
        return this;
    }

    public Character removeCharacterAttribute(CharacterAttribute characterAttribute) {
        this.characterAttributes.remove(characterAttribute);
        characterAttribute.setCharacter(null);
        return this;
    }

    public Set<CharacterSkill> getCharacterSkills() {
        return this.characterSkills;
    }

    public void setCharacterSkills(Set<CharacterSkill> characterSkills) {
        if (this.characterSkills != null) {
            this.characterSkills.forEach(i -> i.setCharacter(null));
        }
        if (characterSkills != null) {
            characterSkills.forEach(i -> i.setCharacter(this));
        }
        this.characterSkills = characterSkills;
    }

    public Character characterSkills(Set<CharacterSkill> characterSkills) {
        this.setCharacterSkills(characterSkills);
        return this;
    }

    public Character addCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.add(characterSkill);
        characterSkill.setCharacter(this);
        return this;
    }

    public Character removeCharacterSkill(CharacterSkill characterSkill) {
        this.characterSkills.remove(characterSkill);
        characterSkill.setCharacter(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Character user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Character)) {
            return false;
        }
        return id != null && id.equals(((Character) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Character{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", height=" + getHeight() +
            ", points=" + getPoints() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", handedness='" + getHandedness() + "'" +
            ", campaignId=" + getCampaignId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
