package com.adi.cms.character.repository;

import com.adi.cms.character.domain.Character;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Character entity.
 */
@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query("select character from Character character where character.user.login = ?#{principal.preferredUsername}")
    List<Character> findByUserIsCurrentUser();

    default Optional<Character> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Character> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Character> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct character from Character character left join fetch character.user",
        countQuery = "select count(distinct character) from Character character"
    )
    Page<Character> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct character from Character character left join fetch character.user")
    List<Character> findAllWithToOneRelationships();

    @Query("select character from Character character left join fetch character.user where character.id =:id")
    Optional<Character> findOneWithToOneRelationships(@Param("id") Long id);
}
