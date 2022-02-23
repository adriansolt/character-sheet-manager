package com.adi.cms.xaracter.repository;

import com.adi.cms.xaracter.domain.Xaracter;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Xaracter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface XaracterRepository extends JpaRepository<Xaracter, Long> {
    @Query("select xaracter from Xaracter xaracter where xaracter.user.login = ?#{principal.preferredUsername}")
    List<Xaracter> findByUserIsCurrentUser();
}
