package com.task.assignment.repository;

import com.task.assignment.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The User repository.
 *
 * @author mukhtiar.ahmed
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

   Page<User> findByIsActiveTrue(Pageable pageable);

    @Query(value = "select p from person p where (lower(p.firstName) like %:search% or lower(p.lastName) like %:search%)" +
            " and isActive = true ")
    Page<User> searchActivePersons(@Param("search") String search, Pageable pageable);

    @Query(value = "select p from person p where (lower(p.firstName) like %:search% or lower(p.lastName) like %:search%)")
    Page<User> searchPersons(@Param("search") String search, Pageable pageable);

}
