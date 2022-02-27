package com.adi.cms.character.repository;

import com.adi.cms.character.domain.PrereqSkillOrAtribute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PrereqSkillOrAtribute entity.
 */
@Repository
public interface PrereqSkillOrAtributeRepository extends JpaRepository<PrereqSkillOrAtribute, Long> {
    default Optional<PrereqSkillOrAtribute> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PrereqSkillOrAtribute> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PrereqSkillOrAtribute> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct prereqSkillOrAtribute from PrereqSkillOrAtribute prereqSkillOrAtribute left join fetch prereqSkillOrAtribute.skill",
        countQuery = "select count(distinct prereqSkillOrAtribute) from PrereqSkillOrAtribute prereqSkillOrAtribute"
    )
    Page<PrereqSkillOrAtribute> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct prereqSkillOrAtribute from PrereqSkillOrAtribute prereqSkillOrAtribute left join fetch prereqSkillOrAtribute.skill"
    )
    List<PrereqSkillOrAtribute> findAllWithToOneRelationships();

    @Query(
        "select prereqSkillOrAtribute from PrereqSkillOrAtribute prereqSkillOrAtribute left join fetch prereqSkillOrAtribute.skill where prereqSkillOrAtribute.id =:id"
    )
    Optional<PrereqSkillOrAtribute> findOneWithToOneRelationships(@Param("id") Long id);
}
