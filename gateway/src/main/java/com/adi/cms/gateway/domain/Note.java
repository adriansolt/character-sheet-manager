package com.adi.cms.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Note.
 */
@Table("note")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "notes", "xaracterAttributes", "xaracterSkills", "user" }, allowSetters = true)
    private Xaracter xaracterId;

    @Column("xaracter_id_id")
    private Long xaracterIdId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Note description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Xaracter getXaracterId() {
        return this.xaracterId;
    }

    public void setXaracterId(Xaracter xaracter) {
        this.xaracterId = xaracter;
        this.xaracterIdId = xaracter != null ? xaracter.getId() : null;
    }

    public Note xaracterId(Xaracter xaracter) {
        this.setXaracterId(xaracter);
        return this;
    }

    public Long getXaracterIdId() {
        return this.xaracterIdId;
    }

    public void setXaracterIdId(Long xaracter) {
        this.xaracterIdId = xaracter;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
