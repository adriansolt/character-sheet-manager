package com.adi.cms.character.repository;

import com.adi.cms.character.domain.Character;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Character entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query("select character from Character character where character.user.login = ?#{principal.preferredUsername}")
    List<Character> findByUserIsCurrentUser();
}
