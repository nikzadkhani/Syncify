package com.syncify.app.repository;

import com.syncify.app.domain.UserDetails;

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

    @Query(value = "select distinct userDetails from UserDetails userDetails left join fetch userDetails.playlistIds",
        countQuery = "select count(distinct userDetails) from UserDetails userDetails")
    Page<UserDetails> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userDetails from UserDetails userDetails left join fetch userDetails.playlistIds")
    List<UserDetails> findAllWithEagerRelationships();

    @Query("select userDetails from UserDetails userDetails left join fetch userDetails.playlistIds where userDetails.id =:id")
    Optional<UserDetails> findOneWithEagerRelationships(@Param("id") Long id);

}
