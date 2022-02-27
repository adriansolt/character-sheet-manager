package com.adi.cms.character.repository;

import com.adi.cms.character.domain.CharacterAttribute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CharacterAttribute entity.
 */
@Repository
public interface CharacterAttributeRepository extends JpaRepository<CharacterAttribute, Long> {
    default Optional<CharacterAttribute> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CharacterAttribute> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CharacterAttribute> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct characterAttribute from CharacterAttribute characterAttribute left join fetch characterAttribute.character",
        countQuery = "select count(distinct characterAttribute) from CharacterAttribute characterAttribute"
    )
    Page<CharacterAttribute> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct characterAttribute from CharacterAttribute characterAttribute left join fetch characterAttribute.character")
    List<CharacterAttribute> findAllWithToOneRelationships();

    @Query(
        "select characterAttribute from CharacterAttribute characterAttribute left join fetch characterAttribute.character where characterAttribute.id =:id"
    )
    Optional<CharacterAttribute> findOneWithToOneRelationships(@Param("id") Long id);
}
