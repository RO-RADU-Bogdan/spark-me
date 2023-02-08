package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Statie;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Statie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatieRepository extends JpaRepository<Statie, Long> {

    @Query("select statie from Statie statie where statie.locatie.id = :id")
    List<Statie> findStatiiByLocatieId(@Param("id") Long id);

    @Query("select statie from Statie statie where statie.retea.id = :id")
    List<Statie> findStatiiByReteaId(@Param("id") Long id);

 }
