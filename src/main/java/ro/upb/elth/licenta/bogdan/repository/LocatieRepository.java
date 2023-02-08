package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Locatie;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Locatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocatieRepository extends JpaRepository<Locatie, Long> {

    @Query("select locatie from Locatie locatie where locatie.user.login = ?#{principal.username}")
    List<Locatie> findByUserIsCurrentUser();
}
