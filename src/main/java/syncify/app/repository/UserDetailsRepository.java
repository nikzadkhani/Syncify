package syncify.app.repository;

import syncify.app.domain.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UserDetails entity.
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    @Query(value = "select distinct userDetails from UserDetails userDetails left join fetch userDetails.playlists",
        countQuery = "select count(distinct userDetails) from UserDetails userDetails")
    Page<UserDetails> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userDetails from UserDetails userDetails left join fetch userDetails.playlists")
    List<UserDetails> findAllWithEagerRelationships();

    @Query("select userDetails from UserDetails userDetails left join fetch userDetails.playlists where userDetails.id =:id")
    Optional<UserDetails> findOneWithEagerRelationships(@Param("id") Long id);

}
