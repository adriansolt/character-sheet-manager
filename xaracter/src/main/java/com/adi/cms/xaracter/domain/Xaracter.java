package com.adi.cms.xaracter.domain;

import com.adi.cms.xaracter.domain.enumeration.Handedness;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Xaracter.
 */
@Entity
@Table(name = "xaracter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Xaracter implements Serializable {

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

    @OneToMany(mappedBy = "xaracterId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "xaracterId" }, allowSetters = true)
    private Set<Note> notes = new HashSet<>();

    @OneToMany(mappedBy = "xaracterId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "xaracterId" }, allowSetters = true)
    private Set<XaracterAttribute> xaracterAttributes = new HashSet<>();

    @OneToMany(mappedBy = "xaracterId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "xaracterId", "skillId" }, allowSetters = true)
    private Set<XaracterSkill> xaracterSkills = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Xaracter id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Xaracter name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Xaracter weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return this.height;
    }

    public Xaracter height(Integer height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Xaracter points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Xaracter picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Xaracter pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public Handedness getHandedness() {
        return this.handedness;
    }

    public Xaracter handedness(Handedness handedness) {
        this.setHandedness(handedness);
        return this;
    }

    public void setHandedness(Handedness handedness) {
        this.handedness = handedness;
    }

    public Long getCampaignId() {
        return this.campaignId;
    }

    public Xaracter campaignId(Long campaignId) {
        this.setCampaignId(campaignId);
        return this;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Xaracter active(Boolean active) {
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
            this.notes.forEach(i -> i.setXaracterId(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setXaracterId(this));
        }
        this.notes = notes;
    }

    public Xaracter notes(Set<Note> notes) {
        this.setNotes(notes);
        return this;
    }

    public Xaracter addNote(Note note) {
        this.notes.add(note);
        note.setXaracterId(this);
        return this;
    }

    public Xaracter removeNote(Note note) {
        this.notes.remove(note);
        note.setXaracterId(null);
        return this;
    }

    public Set<XaracterAttribute> getXaracterAttributes() {
        return this.xaracterAttributes;
    }

    public void setXaracterAttributes(Set<XaracterAttribute> xaracterAttributes) {
        if (this.xaracterAttributes != null) {
            this.xaracterAttributes.forEach(i -> i.setXaracterId(null));
        }
        if (xaracterAttributes != null) {
            xaracterAttributes.forEach(i -> i.setXaracterId(this));
        }
        this.xaracterAttributes = xaracterAttributes;
    }

    public Xaracter xaracterAttributes(Set<XaracterAttribute> xaracterAttributes) {
        this.setXaracterAttributes(xaracterAttributes);
        return this;
    }

    public Xaracter addXaracterAttribute(XaracterAttribute xaracterAttribute) {
        this.xaracterAttributes.add(xaracterAttribute);
        xaracterAttribute.setXaracterId(this);
        return this;
    }

    public Xaracter removeXaracterAttribute(XaracterAttribute xaracterAttribute) {
        this.xaracterAttributes.remove(xaracterAttribute);
        xaracterAttribute.setXaracterId(null);
        return this;
    }

    public Set<XaracterSkill> getXaracterSkills() {
        return this.xaracterSkills;
    }

    public void setXaracterSkills(Set<XaracterSkill> xaracterSkills) {
        if (this.xaracterSkills != null) {
            this.xaracterSkills.forEach(i -> i.setXaracterId(null));
        }
        if (xaracterSkills != null) {
            xaracterSkills.forEach(i -> i.setXaracterId(this));
        }
        this.xaracterSkills = xaracterSkills;
    }

    public Xaracter xaracterSkills(Set<XaracterSkill> xaracterSkills) {
        this.setXaracterSkills(xaracterSkills);
        return this;
    }

    public Xaracter addXaracterSkill(XaracterSkill xaracterSkill) {
        this.xaracterSkills.add(xaracterSkill);
        xaracterSkill.setXaracterId(this);
        return this;
    }

    public Xaracter removeXaracterSkill(XaracterSkill xaracterSkill) {
        this.xaracterSkills.remove(xaracterSkill);
        xaracterSkill.setXaracterId(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Xaracter user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Xaracter)) {
            return false;
        }
        return id != null && id.equals(((Xaracter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Xaracter{" +
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
