package ro.upb.elth.licenta.bogdan.repository;

import ro.upb.elth.licenta.bogdan.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
