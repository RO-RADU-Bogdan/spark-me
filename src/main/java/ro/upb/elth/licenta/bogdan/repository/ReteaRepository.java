package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Retea;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Retea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReteaRepository extends JpaRepository<Retea, Long> {
}
