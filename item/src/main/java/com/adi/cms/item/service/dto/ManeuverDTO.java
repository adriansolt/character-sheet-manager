package com.adi.cms.item.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.item.domain.Maneuver} entity.
 */
public class ManeuverDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer modifier;

    @NotNull
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModifier() {
        return modifier;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ManeuverDTO)) {
            return false;
        }

        ManeuverDTO maneuverDTO = (ManeuverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maneuverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManeuverDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", modifier=" + getModifier() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
