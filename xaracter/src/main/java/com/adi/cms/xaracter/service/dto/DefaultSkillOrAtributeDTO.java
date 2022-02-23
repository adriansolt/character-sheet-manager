package com.adi.cms.xaracter.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.xaracter.domain.DefaultSkillOrAtribute} entity.
 */
public class DefaultSkillOrAtributeDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer modifier;

    private SkillDTO skillId;

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

    public SkillDTO getSkillId() {
        return skillId;
    }

    public void setSkillId(SkillDTO skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSkillOrAtributeDTO)) {
            return false;
        }

        DefaultSkillOrAtributeDTO defaultSkillOrAtributeDTO = (DefaultSkillOrAtributeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, defaultSkillOrAtributeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DefaultSkillOrAtributeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", modifier=" + getModifier() +
            ", skillId=" + getSkillId() +
            "}";
    }
}
