package com.adi.cms.character.repository;

import com.adi.cms.character.domain.PrereqSkillOrAtribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrereqSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrereqSkillOrAtributeRepository extends JpaRepository<PrereqSkillOrAtribute, Long> {}
