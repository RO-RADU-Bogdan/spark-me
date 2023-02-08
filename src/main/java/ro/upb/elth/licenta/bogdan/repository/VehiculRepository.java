package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Vehicul;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Vehicul entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehiculRepository extends JpaRepository<Vehicul, Long> {

    @Query("select vehicul from Vehicul vehicul where vehicul.user.login = ?#{principal.username}")
    List<Vehicul> findByUserIsCurrentUser();

    @Query("select vehicul from Vehicul vehicul where vehicul.user.login = ?#{principal.username}")
    Page<Vehicul> findByUserIsCurrentUser(Pageable pageable);

    @Query("select vehicul from Vehicul vehicul where vehicul.id = :id and vehicul.user.login = ?#{principal.username}")
    Optional<Vehicul> findByUserIsCurrentUserById(@Param("id") Long id);
}
