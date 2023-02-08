package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Rezervare;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Rezervare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RezervareRepository extends JpaRepository<Rezervare, Long> {

    //
    @Query("select rezervare from Rezervare rezervare where rezervare.user.login = ?#{principal.username}")
    Page<Rezervare> findByUserIsCurrentUser(Pageable pageable);

    @Query("select rezervare from Rezervare rezervare where rezervare.user.login = ?#{principal.username}")
    List<Rezervare> findByUserIsCurrentUser();

    @Query("select rezervare from Rezervare rezervare where rezervare.id = :id and rezervare.user.login = ?#{principal.username}")
    Optional<Rezervare> findByUserIsCurrentUserById(@Param("id") Long id);
}
