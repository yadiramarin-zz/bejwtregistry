package be.jwtregistry.repository;

import be.jwtregistry.domain.Industries;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Industries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndustriesRepository extends JpaRepository<Industries, Long> {
}
