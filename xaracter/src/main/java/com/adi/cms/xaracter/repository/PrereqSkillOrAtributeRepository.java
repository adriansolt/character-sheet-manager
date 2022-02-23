package com.adi.cms.xaracter.repository;

import com.adi.cms.xaracter.domain.PrereqSkillOrAtribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrereqSkillOrAtribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrereqSkillOrAtributeRepository extends JpaRepository<PrereqSkillOrAtribute, Long> {}
