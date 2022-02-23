package com.adi.cms.item.repository;

import com.adi.cms.item.domain.ArmorPiece;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ArmorPiece entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArmorPieceRepository extends JpaRepository<ArmorPiece, Long> {}
