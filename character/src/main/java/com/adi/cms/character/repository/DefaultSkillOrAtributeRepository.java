package com.adi.cms.character.repository;

import com.adi.cms.character.domain.DefaultSkillOrAtribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DefaultSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefaultSkillOrAtributeRepository extends JpaRepository<DefaultSkillOrAtribute, Long> {}
