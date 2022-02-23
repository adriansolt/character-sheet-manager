package com.adi.cms.gateway.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.adi.cms.gateway.domain.PrereqSkillOrAtribute} entity.
 */
public class PrereqSkillOrAtributeDTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private Integer level;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
        if (!(o instanceof PrereqSkillOrAtributeDTO)) {
            return false;
        }

        PrereqSkillOrAtributeDTO prereqSkillOrAtributeDTO = (PrereqSkillOrAtributeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prereqSkillOrAtributeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrereqSkillOrAtributeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            ", skillId=" + getSkillId() +
            "}";
    }
}
