package com.adi.cms.character.repository;

import com.adi.cms.character.domain.CharacterSkill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CharacterSkill entity.
 */
@Repository
public interface CharacterSkillRepository extends JpaRepository<CharacterSkill, Long> {
    default Optional<CharacterSkill> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CharacterSkill> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CharacterSkill> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct characterSkill from CharacterSkill characterSkill left join fetch characterSkill.character left join fetch characterSkill.skill",
        countQuery = "select count(distinct characterSkill) from CharacterSkill characterSkill"
    )
    Page<CharacterSkill> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct characterSkill from CharacterSkill characterSkill left join fetch characterSkill.character left join fetch characterSkill.skill"
    )
    List<CharacterSkill> findAllWithToOneRelationships();

    @Query(
        "select characterSkill from CharacterSkill characterSkill left join fetch characterSkill.character left join fetch characterSkill.skill where characterSkill.id =:id"
    )
    Optional<CharacterSkill> findOneWithToOneRelationships(@Param("id") Long id);
}
