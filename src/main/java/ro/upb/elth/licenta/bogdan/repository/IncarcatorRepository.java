package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Incarcator;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Incarcator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncarcatorRepository extends JpaRepository<Incarcator, Long> {
}
