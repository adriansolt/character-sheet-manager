package com.adi.cms.character.repository;

import com.adi.cms.character.domain.DefaultSkillOrAtribute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DefaultSkillOrAtribute entity.
 */
@Repository
public interface DefaultSkillOrAtributeRepository extends JpaRepository<DefaultSkillOrAtribute, Long> {
    default Optional<DefaultSkillOrAtribute> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DefaultSkillOrAtribute> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DefaultSkillOrAtribute> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct defaultSkillOrAtribute from DefaultSkillOrAtribute defaultSkillOrAtribute left join fetch defaultSkillOrAtribute.skill",
        countQuery = "select count(distinct defaultSkillOrAtribute) from DefaultSkillOrAtribute defaultSkillOrAtribute"
    )
    Page<DefaultSkillOrAtribute> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct defaultSkillOrAtribute from DefaultSkillOrAtribute defaultSkillOrAtribute left join fetch defaultSkillOrAtribute.skill"
    )
    List<DefaultSkillOrAtribute> findAllWithToOneRelationships();

    @Query(
        "select defaultSkillOrAtribute from DefaultSkillOrAtribute defaultSkillOrAtribute left join fetch defaultSkillOrAtribute.skill where defaultSkillOrAtribute.id =:id"
    )
    Optional<DefaultSkillOrAtribute> findOneWithToOneRelationships(@Param("id") Long id);
}
